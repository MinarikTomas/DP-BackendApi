package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ActivityResponse {
    private Integer id;
    private String name;
    private String location;
    private LocalDateTime time;
    private Integer createdBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ActivityResponse(Activity activity){
        this.id = activity.getId();
        this.name = activity.getName();
        this.location = activity.getLocation();
        this.time = activity.getTime();
        this.createdBy = activity.getCreatedBy().getId();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
    }
}
