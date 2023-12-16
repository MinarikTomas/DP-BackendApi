package sk.stuba.fei.uim.dp.attendanceapi.request;

import lombok.Data;

@Data
public class AddCardRequest {
    private String name;
    private String serialNumber;
    private Integer uid;
}
