package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;


@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<Integer> signupUser(@Valid @RequestBody SignupRequest request){
        return new ResponseEntity<>(this.userService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Integer> loginUser(@Valid @RequestBody LoginRequest request){
        return new ResponseEntity<>(this.userService.login(request), HttpStatus.OK);
    }
}
