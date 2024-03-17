package sk.stuba.fei.uim.dp.attendanceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Serial number is mandatory")
    private String serialNumber;

}
