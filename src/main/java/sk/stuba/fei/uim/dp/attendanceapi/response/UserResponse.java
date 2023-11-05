package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

@Data
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private List<CardResponse> cards;

    public UserResponse(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.cards = user.getCards().stream().map(CardResponse::new).collect(Collectors.toList());
    }
}
