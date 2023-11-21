package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.EmailRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.ActivityResponse;
import sk.stuba.fei.uim.dp.attendanceapi.response.UserResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

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

//    @Operation(
//            description = "Returns all activities that the user with given ID attended",
//            summary = "Get all user attended activities"
//    )
//    @GetMapping(value = "/{id}/attendedactivites")
//    public List<ActivityResponse> getUserAttendedActivities(@PathVariable("id") Integer id){
//        return this.userService.getAttendedActivities(id).stream().map(ActivityResponse::new).collect(Collectors.toList());
//    }
//
//    @Operation(
//            description = "Returns all activities that the user with the given ID created",
//            summary = "Get all user created activities"
//    )
//    @GetMapping(value = "/{id}/createdactivities")
//    public List<ActivityResponse> getUserCreatedActivities(@PathVariable("id") Integer id){
//        return this.userService.getUserCreatedActivities(id).stream().map(ActivityResponse::new).collect(Collectors.toList());
//    }

    @Operation(
            description = "Deletes the user with the given ID",
            summary = "Delete user"
    )
    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        this.userService.deleteUser(id);
    }
}
