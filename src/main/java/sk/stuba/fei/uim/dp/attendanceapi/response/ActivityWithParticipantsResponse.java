package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ActivityWithParticipantsResponse {
    private Integer id;
    private String name;
    private String location;
    private String time;
    private Integer createdBy;
    private List<UserResponse> participants;
    private String startTime;
    private String endTime;

    public ActivityWithParticipantsResponse(Activity activity){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = activity.getId();
        this.name = activity.getName();
        this.location = activity.getLocation();
        this.time = formatter.format(activity.getTime());
        this.participants = activity.getParticipants().stream().map(participant -> {
            if(participant.getCard().getUser() == null){
                return new UserResponse(new User("Unknown", "", ""));
            }
            return new UserResponse(participant.getCard().getUser());
        }).collect(Collectors.toList());
        this.createdBy = activity.getCreatedBy().getId();
        this.startTime = activity.getStartTime() != null ? formatter.format(activity.getStartTime()) : "";
        this.endTime = activity.getEndTime() != null ? formatter.format(activity.getEndTime()) : "";
    }
}
