package sk.stuba.fei.uim.dp.attendanceapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupUser(@Valid @RequestBody SignupRequest signupRequest){
        this.userService.create(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(this.userService.login(loginRequest), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
