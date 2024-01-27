package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;

public interface ICardService {
    void createCard(CardRequest request, User user);
    Card getById(Integer id);
    Card getBySerialNumber(String serialNumber);
    void deactivateCard(Integer id);
}
