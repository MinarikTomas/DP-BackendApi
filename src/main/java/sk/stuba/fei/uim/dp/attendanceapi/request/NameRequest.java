package sk.stuba.fei.uim.dp.attendanceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NameRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
}
