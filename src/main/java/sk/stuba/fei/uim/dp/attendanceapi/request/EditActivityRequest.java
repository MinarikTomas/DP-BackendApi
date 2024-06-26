package sk.stuba.fei.uim.dp.attendanceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditActivityRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Location is mandatory")
    private String location;
    @Schema(
            type = "string",
            pattern = "dd.MM.yyyy HH:mm",
            example = "17.05.2024 10:30"
    )
    @NotBlank(message = "Time is mandatory")
    private String time;
    private Boolean editAllUpcoming;
}
