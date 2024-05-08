package sk.stuba.fei.uim.dp.attendanceapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sk.stuba.fei.uim.dp.attendanceapi.email.EmailSender;
import sk.stuba.fei.uim.dp.attendanceapi.entity.*;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.GoogleLoginException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.repository.PasswordResetTokenRepository;
import sk.stuba.fei.uim.dp.attendanceapi.repository.RoleRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.*;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;
import sk.stuba.fei.uim.dp.attendanceapi.security.Constants;
import sk.stuba.fei.uim.dp.attendanceapi.security.JWTGenerator;

@Service
public class UserService implements IUserService{

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final JWTGenerator jwtGenerator;
    private final CardService cardService;
    private final GoogleIdTokenVerifier verifier;

    private final EmailSender emailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       ActivityRepository activityRepository,
                       JWTGenerator jwtGenerator,
                       CardService cardService,
                       EmailSender emailSender,
                       PasswordResetTokenRepository passwordResetTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.jwtGenerator = jwtGenerator;
        this.cardService = cardService;
        this.emailSender = emailSender;

        String clientId = Constants.CLIENT_ID.trim();
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();

        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void create(SignupRequest signupRequest) {
        if(this.userRepository.existsByEmail(signupRequest.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        if(this.cardService.existsWithUser(signupRequest.getCard().getSerialNumber())){
            throw new CardAlreadyExistsException("User with this card already exists.");
        }
        User user = new User(
                signupRequest.getName(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                User.Type.classic
        );
        Role role = this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(role));
        User savedUser = this.userRepository.save(user);

        try{
            Card card = this.cardService.getBySerialNumber(signupRequest.getCard().getSerialNumber());
            this.cardService.addUser(savedUser, card);
        }catch(CardNotFoundException e){
            this.cardService.createCard(signupRequest.getCard(), savedUser);
        }
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = this.userRepository.findByEmail(loginRequest.getEmail());
        String token = jwtGenerator.generateToken(user);
        String refreshToken = jwtGenerator.generateRefreshToken(user);
        return new AuthResponse(token, refreshToken);
    }

    @Override
    public AuthResponse googleLogin(GoogleRequest request){
        try {
            GoogleIdToken idToken = verifier.verify(request.getGoogleToken());
            if (idToken == null) {
                System.out.println("Invalid google token");
                throw new GoogleLoginException("Token is invalid");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            System.out.println(email);
            System.out.println(payload.get("name").toString());
            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User(
                        payload.get("name").toString(),
                        email,
                        "",
                        User.Type.google
                );
                user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
                user = this.userRepository.save(user);
            }
            String token = this.jwtGenerator.generateToken(user);
            String refreshToken = this.jwtGenerator.generateRefreshToken(user);
            return new AuthResponse(token, refreshToken);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new GoogleLoginException("Failed to verify token");
        }
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }
    @Override
    public User getById(Integer id) {
        Optional<User> user= this.userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }throw new UserNotFoundException("User not found.");
    }

    @Override
    public User getByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found.");
        }
        return user;
    }

    @Override
    public List<Activity> getAttendedActivities(Integer id) {
        return this.activityRepository.getAllUserAttendedActivities(id);
    }
    @Override
    public List<Activity> getUserCreatedActivities(Integer id) {
        List<Activity> activities = this.getById(id).getMyActivities();
        activities.sort(Comparator.comparing(Activity::getTime));
        return activities;
    }

    @Override
    public List<Activity> getAllActivities(Integer id){
        return Stream.concat(this.getUserCreatedActivities(id).stream(), this.getAttendedActivities(id).stream()).toList();
    }

    @Override
    public List<Card> getAllCards(Integer id) {
        return this.getById(id).getCards();
    }

    @Override
    public List<Card> getActiveCards(Integer id) {
        return this.getById(id).getCards().stream().filter(Card::getActive).collect(Collectors.toList());
    }

    @Override
    public List<Card> getInactiveCards(Integer id) {
        return this.getById(id).getCards().stream().filter(card -> !card.getActive()).collect(Collectors.toList());
    }

    @Override
    public void addCard(CardRequest request, Integer id) {
        User user = this.getById(id);
        this.cardService.createCard(request, user);
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        User user = this.getById(id);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        this.userRepository.save(user);
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = this.getById(id);
        this.userRepository.delete(user);
    }

    @Override
    public void resetPassword(HttpServletRequest httpServletRequest, EmailRequest request) {
        User user = this.getByEmail(request.getEmail());
        PasswordResetToken token = passwordResetTokenRepository.findByUser(user);

        if(token != null){
            if(isTokenExpired(token)){
                token.extendExpiryDate();
                token.setToken(UUID.randomUUID().toString());
            }
        }else{
            token = new PasswordResetToken(user, UUID.randomUUID().toString());
        }

        passwordResetTokenRepository.save(token);
        String url = getAppUrl(httpServletRequest) + "/resetPassword?token=" + token.getToken();
        emailSender.sendEmail(request.getEmail(), "Password reset", url);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String email;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtGenerator.getEmailFromJWT(refreshToken);
        if(email != null){
            User user = this.getByEmail(email);
            String accessToken = jwtGenerator.generateToken(user);
            AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    public void save(User user) {
        if (user.getId() == null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public boolean validatePasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null) return false;
        return !isTokenExpired(passwordResetToken);
    }

    public User getUserByPasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return passwordResetToken.getUser();
    }

    private boolean isTokenExpired(PasswordResetToken token){
        Calendar calendar = Calendar.getInstance();
        return token.getExpiry().before(calendar.getTime());
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
