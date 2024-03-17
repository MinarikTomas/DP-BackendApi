package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.ChangePasswordRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.EmailRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.ActivityResponse;
import sk.stuba.fei.uim.dp.attendanceapi.response.CardResponse;
import sk.stuba.fei.uim.dp.attendanceapi.response.UserResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "Returns the user with the given ID",
            summary = "Get user using ID"
    )
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Integer id){
        return new UserResponse(this.userService.getById(id));
    }

    @Operation(
            description = "Updates password of the user with the given id",
            summary = "Change password"
    )
    @PutMapping("/{id}")
    public void changePassword(@PathVariable("id")Integer id, @Valid @RequestBody ChangePasswordRequest request){
        this.userService.changePassword(id, request);
    }

    @Operation(
            description = "Returns the user with the given email",
            summary = "Get user using email"
    )
    @GetMapping(consumes = "application/json")
    public UserResponse getUserByEmail(@Valid @RequestBody EmailRequest request){
        return new UserResponse(this.userService.getByEmail(request.getEmail()));
    }

    @Operation(
            description = "Returns user's activities with optional query parameter created/attended",
            summary = "Get user's activities"
    )
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityResponse>> getActivities(@PathVariable("id")Integer id, @RequestParam(required = false) String type){
        if(type == null){
            return new ResponseEntity<>(
                    this.userService.getAllActivities(id).stream().map(ActivityResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        if(type.equals("created")){
            return new ResponseEntity<>(
                    this.userService.getUserCreatedActivities(id).stream().map(ActivityResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        if(type.equals("attended")){
            return new ResponseEntity<>(
                    this.userService.getAttendedActivities(id).stream().map(ActivityResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<CardResponse>> getCards(@PathVariable("id")Integer id, @RequestParam(required = false)String type){
        if(type == null){
            return new ResponseEntity<>(
                    this.userService.getAllCards(id).stream().map(CardResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        if(type.equals("active")){
            return new ResponseEntity<>(
                    this.userService.getActiveCards(id).stream().map(CardResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        if(type.equals("inactive")){
            return new ResponseEntity<>(
                    this.userService.getInactiveCards(id).stream().map(CardResponse::new).collect(Collectors.toList()),
                    HttpStatus.OK
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            description = "Add card to user",
            summary = "Add card to user"
    )
    @PostMapping("/{id}/cards")
    public ResponseEntity<String> addCard(@PathVariable("id")Integer id, @Valid @RequestBody CardRequest request){
        this.userService.addCard(request, id);
        return new ResponseEntity<>("Card added", HttpStatus.CREATED);
    }

    @PostMapping("/resetPassword")
    public void resetPassword(HttpServletRequest request, @Valid @RequestBody EmailRequest requestBody){
        this.userService.resetPassword(request, requestBody);
    }

    @Operation(
            description = "Deletes the user with the given ID",
            summary = "Delete user"
    )
    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        this.userService.deleteUser(id);
    }
}
