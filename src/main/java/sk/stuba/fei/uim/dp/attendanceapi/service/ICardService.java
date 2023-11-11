package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;

public interface ICardService {
    void createCard(CardRequest request);
    Card getById(Integer id);
    Card getBySerialNumber(String serialNumber);
    void deleteCard(Integer id);
}
