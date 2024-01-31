package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.ChangePasswordRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.LoginRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SignupRequest;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.response.AuthResponse;

import java.util.List;

public interface IUserService {
    void create(SignupRequest signupRequest);
    AuthResponse login(LoginRequest loginRequest);
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
}
