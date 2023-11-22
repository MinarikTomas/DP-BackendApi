package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.request.EmailRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.ActivityResponse;
import sk.stuba.fei.uim.dp.attendanceapi.response.UserResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            description = "Returns the user with the given ID",
            summary = "Get user using ID"
    )
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Integer id){
        return new UserResponse(this.userService.getById(id));
    }

    @Operation(
            description = "Returns the user with the given email",
            summary = "Get user using email"
    )
    @GetMapping(consumes = "application/json")
    public UserResponse getUserByEmail(@RequestBody EmailRequest request){
        return new UserResponse(this.userService.getByEmail(request.getEmail()));
    }

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

    @Operation(
            description = "Deletes the user with the given ID",
            summary = "Delete user"
    )
    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        this.userService.deleteUser(id);
    }
}
