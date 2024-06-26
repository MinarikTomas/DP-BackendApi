package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Participant;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyEndedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyStartedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotStartedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ActivityRepository;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ParticipantRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.EditActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SerialNumberRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService implements IActivityService{
    private final UserService userService;
    private final CardService cardService;
    private final ActivityRepository activityRepository;
    private final ParticipantRepository participantRepository;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public ActivityService(UserService userService, CardService cardService, ActivityRepository activityRepository, ParticipantRepository participantRepository) {
        this.userService = userService;
        this.cardService = cardService;
        this.activityRepository = activityRepository;
        this.participantRepository = participantRepository;
    }

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
        throw new ActivityNotFoundException("Activity not found.");
    }

    @Override
    public void startActivity(Integer id) {
        Activity activity = this.getById(id);
        if(activity.getStartTime() != null){
            throw new ActivityAlreadyStartedException("The Activity already started.");
        }
        activity.setStartTime(LocalDateTime.now());
        this.activityRepository.save(activity);
    }

    @Override
    public void endActivity(Integer id) {
        Activity activity = this.getById(id);
        if(activity.getEndTime() != null){
            throw new ActivityAlreadyEndedException("The Activity already ended.");
        }
        if(activity.getStartTime() == null){
            throw new ActivityNotStartedException("The Activity has not started yet.");
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

    public void deleteActivity(Activity activity){
        this.activityRepository.delete(activity);
    }

    @Override
    public User addParticipant(Integer id, SerialNumberRequest request) {
        Activity activity = this.getById(id);
        if(activity.getStartTime() == null){
            throw new ActivityNotStartedException("The activity has not started yet.");
        }
        if(activity.getEndTime() != null){
            throw new ActivityAlreadyEndedException("The activity already ended.");
        }
        User user;
        Card card;
        try{
            card = this.cardService.getBySerialNumber(request.getSerialNumber());
            if(card.getUser() == null){
                user = new User("Unknown", "", "",null);
            }else{
                user = card.getUser();
            }
        }catch (CardNotFoundException e){
            System.out.println("catch block");
            card = this.cardService.createCardWithoutUser(request.getSerialNumber());
            user = new User("Unknown", "", "",null);
        }
        Participant participant = new Participant(activity, card);
        this.participantRepository.save(participant);
        return user;
    }

    public List<Activity> getAll() {
        return this.activityRepository.findAll();
    }

    public void save(Activity activity) {
        this.activityRepository.save(activity);
    }
}
