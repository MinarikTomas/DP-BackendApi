package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Override
    public void create(SignupRequest signupDto) throws UserAlreadyExistsException {
        if(emailExists(signupDto.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        User user = new User(
                signupDto.getName(),
                signupDto.getEmail(),
                signupDto.getPassword()
        );
        this.userRepository.save(user);
    }

    @Override
    public User getById(Integer id) {
        Optional<User> user= this.userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }throw new UserNotFoundException("USer not found.");
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
        return this.getById(id).getMyActivities();
    }

    @Override
    public List<Activity> getAllActivities(Integer id){
        return Stream.concat(this.getUserCreatedActivities(id).stream(), this.getAttendedActivities(id).stream()).toList();
    }

    @Override
    public void deleteUser(Integer id) {
        User user = this.getById(id);
        this.userRepository.delete(user);
    }

    private boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
    }
}
