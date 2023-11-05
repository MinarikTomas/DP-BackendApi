package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;

import java.time.Instant;

@Data
public class ActivityResponse {
    private Integer id;
    private String name;
    private String location;
    private Instant time;
    private Integer createdBy;
    private Instant startTime;
    private Instant endTime;

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
