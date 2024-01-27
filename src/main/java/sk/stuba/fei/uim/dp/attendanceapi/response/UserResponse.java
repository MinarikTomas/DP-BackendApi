package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;

import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

@Data
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;

    public UserResponse(User user){
        if(user.getId() == null){
            this.id = -1;
        }else{
            this.id = user.getId();
        }
        this.fullName = user.getFullName();
        this.email = user.getEmail();
    }
}
