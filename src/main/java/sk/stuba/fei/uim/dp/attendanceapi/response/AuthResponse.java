package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;

@Data
public class AuthResponse {
    private Integer id;
    private String accessToken;

    public AuthResponse(Integer id, String accessToken){
        this.id = id;
        this.accessToken = accessToken;
    }
}
