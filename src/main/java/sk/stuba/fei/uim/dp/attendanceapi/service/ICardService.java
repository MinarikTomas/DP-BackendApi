package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.NameRequest;

public interface ICardService {
    void createCard(CardRequest request, User user);
    Card getById(Integer id);
    Card getBySerialNumber(String serialNumber);
    void update(NameRequest request, Integer id);
    void deactivateCard(Integer id);
}
