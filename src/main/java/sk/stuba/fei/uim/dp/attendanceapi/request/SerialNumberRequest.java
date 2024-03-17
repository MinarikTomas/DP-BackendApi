package sk.stuba.fei.uim.dp.attendanceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SerialNumberRequest {
    @NotBlank(message = "Serial number is mandatory")
    private String serialNumber;
}
