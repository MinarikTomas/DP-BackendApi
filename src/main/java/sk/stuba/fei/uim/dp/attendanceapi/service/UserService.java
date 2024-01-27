package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardAlreadyExists;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.repository.RoleRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;
import sk.stuba.fei.uim.dp.attendanceapi.security.JWTGenerator;

@Service
public class UserService implements IUserService{

    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ActivityRepository activityRepository;
    private JWTGenerator jwtGenerator;

    private CardService cardService;
    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       ActivityRepository activityRepository,
                       JWTGenerator jwtGenerator,
                       CardService cardService) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.jwtGenerator = jwtGenerator;
        this.cardService = cardService;
    }

    @Override
    public void create(SignupRequest signupRequest) {
        if(this.userRepository.existsByEmail(signupRequest.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        if(this.cardService.existsWithUser(signupRequest.getCard().getSerialNumber())){
            throw new CardAlreadyExists("User with this card already exists.");
        }
        User user = new User(
                signupRequest.getName(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        Role role = this.roleRepository.findByName("USER");
        user.setRoles(Collections.singletonList(role));
        User savedUser = this.userRepository.save(user);

        try{
            Card card = this.cardService.getBySerialNumber(signupRequest.getCard().getSerialNumber());
            this.cardService.addUser(savedUser, card);
        }catch(CardNotFound e){
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
        String token = jwtGenerator.generateToken(authentication, user);
        return new AuthResponse(token);
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
        Collections.sort(activities, Comparator.comparing(Activity::getTime));
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
    public void deleteUser(Integer id) {
        User user = this.getById(id);
        this.userRepository.delete(user);
    }
}
