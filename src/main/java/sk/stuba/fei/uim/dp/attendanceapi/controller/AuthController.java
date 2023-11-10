package sk.stuba.fei.uim.dp.attendanceapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignupRequest signupDto){
        this.userService.create(signupDto);
        return new ResponseEntity<>("Successfully signed-up", HttpStatus.CREATED);
    }
}
