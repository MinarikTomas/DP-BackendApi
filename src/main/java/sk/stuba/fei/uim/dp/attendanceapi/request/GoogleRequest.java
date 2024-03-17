package sk.stuba.fei.uim.dp.attendanceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleRequest {
    @NotBlank(message = "Token is mandatory")
    private String googleToken;
}
