package sk.stuba.fei.uim.dp.attendanceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EditActivityRequest {
    private String name;
    private String location;
    @Schema(
            type = "string",
            pattern = "dd.MM.yyyy HH:mm",
            example = "17.05.2024 10:30"
    )
    private String time;
    private Boolean editAllUpcoming;
}
