package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Participant;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyEnded;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyStarted;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotStarted;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ParticipantRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.EditActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.ParticipantRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ActivityService implements IActivityService{
    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    @Override
    public void createActivity(ActivityRequest request){
        LocalDateTime time = LocalDateTime.parse(request.getTime(), FORMATTER);
        Activity activity = new Activity(
                userService.getById(request.getUid()),
                request.getName(),
                request.getLocation(),
                time

        );
        Activity createdActivity = this.activityRepository.save(activity);

        if(request.getWeeks() > 0){
            for(int i = 0; i < request.getWeeks(); i++){
                activity = new Activity(
                        userService.getById(request.getUid()),
                        request.getName(),
                        request.getLocation(),
                        time.plusWeeks(i+1)
                );
                activity.setGroupId(createdActivity.getId());
                this.activityRepository.save(activity);
            }
            createdActivity.setGroupId(createdActivity.getId());
            this.activityRepository.save(createdActivity);
        }
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
    public Activity update(Integer id, EditActivityRequest request) {
        if(request.getEditAllUpcoming()){
            return null;
        }else{
            return this.activityRepository.save(
                    updateAttributes(
                            this.getById(id),
                            request.getName(),
                            request.getLocation(),
                            LocalDateTime.parse(request.getTime(), FORMATTER))
            );
        }
    }

    private Activity updateAttributes(
            Activity activity,
            String name,
            String location,
            LocalDateTime time){
        activity.setName(name);
        activity.setLocation(location);
        activity.setTime(time);
        return activity;
    }

    @Override
    public void deleteActivity(Integer id) {
        Activity activity = this.getById(id);
        this.activityRepository.delete(activity);
    }

    @Override
    public User addParticipant(Integer id, ParticipantRequest request) {
        Activity activity = this.getById(id);
        if(activity.getStartTime() == null){
            throw new ActivityNotStarted("The activity has not started yet.");
        }
        if(activity.getEndTime() != null){
            throw new ActivityAlreadyEnded("The activity already ended.");
        }
        User user;
        Card card;
        try{
            card = this.cardService.getBySerialNumber(request.getSerialNumber());
            if(card.getUser() == null){
                user = new User("Unknown", "", "","");
            }else{
                user = card.getUser();
            }
        }catch (CardNotFound e){
            System.out.println("catch block");
            card = this.cardService.createCardWithoutUser(request.getSerialNumber());
            user = new User("Unknown", "", "","");
        }
        Participant participant = new Participant(activity, card);
        this.participantRepository.save(participant);
        return user;
    }

}
