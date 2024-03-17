package sk.stuba.fei.uim.dp.attendanceapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.GoogleRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupUser(@Valid @RequestBody SignupRequest signupRequest){
        this.userService.create(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(this.userService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/google")
    public AuthResponse googleLogin(@Valid @RequestBody GoogleRequest request){
        return this.userService.googleLogin(request);
    }

    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        this.userService.refreshToken(request, response);
    }
}
