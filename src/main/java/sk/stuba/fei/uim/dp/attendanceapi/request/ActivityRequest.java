package sk.stuba.fei.uim.dp.attendanceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ActivityRequest {
    private Integer uid;
    private String name;
    private String location;
    @Schema(
            type = "string",
            pattern = "yyyy-MM-dd HH:mm",
            example = "2023-05-17 10:00"
    )
    private String time;
    private Integer weeks;
}
