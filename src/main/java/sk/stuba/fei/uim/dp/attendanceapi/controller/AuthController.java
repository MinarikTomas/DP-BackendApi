package sk.stuba.fei.uim.dp.attendanceapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.dto.SignupDto;
import sk.stuba.fei.uim.dp.attendanceapi.exception.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignupDto signupDto){
        this.userService.create(signupDto);
        return new ResponseEntity<>("Successfully signed-up", HttpStatus.CREATED);
    }
}
