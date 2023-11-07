package sk.stuba.fei.uim.dp.attendanceapi.request;

import lombok.Data;

@Data
public class CardRequest {
    private String name;
    private String serialNumber;
    private Integer uid;
}
