package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponse(String accessToken){
        this.accessToken = accessToken;
    }
}
