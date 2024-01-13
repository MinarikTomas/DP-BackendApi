package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ActivityResponse {
    private Integer id;
    private String name;
    private String location;
    private String time;
    private Integer createdBy;
    private String startTime;
    private String endTime;

    public ActivityResponse(Activity activity){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = activity.getId();
        this.name = activity.getName();
        this.location = activity.getLocation();
        this.time = formatter.format(activity.getTime());
        this.createdBy = activity.getCreatedBy().getId();
        this.startTime = activity.getStartTime() != null ? formatter.format(activity.getStartTime()) : "";
        this.endTime = activity.getEndTime() != null ? formatter.format(activity.getEndTime()) : "";
    }
}
