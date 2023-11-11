package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyEnded;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyStarted;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotStarted;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ActivityService implements IActivityService{
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityRepository activityRepository;
    @Override
    public void createActivity(ActivityRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(request.getTime(), formatter);
        Activity activity = new Activity(
                userService.getById(request.getUid()),
                request.getName(),
                request.getLocation(),
                time

        );
        this.activityRepository.save(activity);
    }

    @Override
    public Activity getById(Integer id){
        Optional<Activity> activity = this.activityRepository.findById(id);
        if(activity.isPresent()){
            return activity.get();
        }
        throw new ActivityNotFound("Activity not found.");
    }

    @Override
    public void startActivity(Integer id) {
        Activity activity = this.getById(id);
        if(activity.getStartTime() != null){
            throw new ActivityAlreadyStarted("The Activity already started.");
        }
        activity.setStartTime(LocalDateTime.now());
        this.activityRepository.save(activity);
    }

    @Override
    public void endActivity(Integer id) {
        Activity activity = this.getById(id);
        if(activity.getEndTime() != null){
            throw new ActivityAlreadyEnded("The Activity already ended.");
        }
        if(activity.getStartTime() == null){
            throw new ActivityNotStarted("The Activity has not started yet.");
        }
        activity.setEndTime(LocalDateTime.now());
        this.activityRepository.save(activity);
    }

    @Override
    public void deleteActivity(Integer id) {
        Activity activity = this.getById(id);
        this.activityRepository.delete(activity);
    }

}
