package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.ParticipantRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.ActivityWithParticipantsResponse;
import sk.stuba.fei.uim.dp.attendanceapi.response.UserResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.ActivityService;


@RestController
@RequestMapping("/activity")
@Tag(name = "Activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Operation(
            description = "Create a new activity",
            summary = "Create activity"
    )
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> createActivity(@RequestBody ActivityRequest request){
        this.activityService.createActivity(request);
        return new ResponseEntity<>("Activity created", HttpStatus.CREATED);
    }

    @Operation(
            description = "Returns the activity with given ID.",
            summary = "Get activity."
    )
    @GetMapping(value = "/{id}")
    public ActivityWithParticipantsResponse getActivity(@PathVariable("id") Integer id){
        return new ActivityWithParticipantsResponse(this.activityService.getById(id));
    }

    @Operation(
            description = "Add participant to activity with the given id",
            summary = "add participant"
    )
    @PostMapping("/{id}")
    public UserResponse addParticipant(@PathVariable("id")Integer id, @RequestBody ParticipantRequest request){
        User user= this.activityService.addParticipant(id, request);
        if(user == null){
            return null;
        }
        return new UserResponse(user);
    }

    @Operation(
            description = "Set current time as activity start time.",
            summary = "Start activity."
    )
    @PostMapping("/{id}/start")
    public void startActivity(@PathVariable("id")Integer id){
        this.activityService.startActivity(id);
    }

    @Operation(
            description = "Set current time as activity end time.",
            summary = "End activity."
    )
    @PostMapping("/{id}/end")
    public void endActivity(@PathVariable("id")Integer id){
        this.activityService.endActivity(id);
    }

    @Operation(
            description = "Deletes the activity with the given ID",
            summary = "Delete activity"
    )
    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable("id")Integer id){
        this.activityService.deleteActivity(id);
    }
}
