package sk.stuba.fei.uim.dp.attendanceapi.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.web.bind.MethodArgumentNotValidException;
import sk.stuba.fei.uim.dp.attendanceapi.dto.SignupDto;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.UserRepository;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(SignupDto signupDto) {
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

    private boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
    }
}
