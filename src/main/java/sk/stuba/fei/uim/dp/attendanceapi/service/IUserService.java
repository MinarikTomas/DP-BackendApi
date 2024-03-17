package sk.stuba.fei.uim.dp.attendanceapi.service;

import jakarta.servlet.http.HttpServletRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.request.*;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;

import java.util.List;

public interface IUserService {
    void create(SignupRequest signupRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse googleLogin(GoogleRequest request);
    User getById(Integer id);
    User getByEmail(String email);
    List<Activity> getAttendedActivities(Integer id);
    List<Activity> getUserCreatedActivities(Integer id);
    List<Activity> getAllActivities(Integer id);

    List<Card> getAllCards(Integer id);

    List<Card> getActiveCards(Integer id);
    List<Card> getInactiveCards(Integer id);
    void addCard(CardRequest request, Integer id);

    void changePassword(Integer id, ChangePasswordRequest request);
    void deleteUser(Integer id);

    void resetPassword(HttpServletRequest httpServletRequest, EmailRequest request);
}
