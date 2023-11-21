package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.UserNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(SignupRequest signupDto) throws UserAlreadyExistsException{
        if(emailExists(signupDto.getEmail())){
            throw new UserAlreadyExistsException();
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
        }throw new UserNotFound();
    }

    @Override
    public User getByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFound();
        }
        return user;
    }

//    @Override
//    public List<Activity> getAttendedActivities(Integer id) {
//        return this.getById(id).getAttendedActivities();
//    }

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
