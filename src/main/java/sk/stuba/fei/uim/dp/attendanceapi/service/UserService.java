package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import sk.stuba.fei.uim.dp.attendanceapi.exception.user.WrongPasswordException;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Integer create(SignupRequest request) throws UserAlreadyExistsException{
        if(emailExists(request.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        this.userRepository.save(user);
        return user.getId();
    }

    @Override
    public Integer login(LoginRequest request) {
        User user = this.getByEmail(request.getEmail());
        if(Objects.equals(user.getPassword(), request.getPassword())){
            return user.getId();
        }
        throw new WrongPasswordException("Wrong password.");
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
        return this.getById(id).getAttendedActivities();
    }

    @Override
    public List<Activity> getUserCreatedActivities(Integer id) {
        return this.getById(id).getMyActivities();
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
