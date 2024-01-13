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
            pattern = "dd.MM.yyyy HH:mm",
            example = "17.05.2024 10:30"
    )
    private String time;
    private Integer weeks;
}
