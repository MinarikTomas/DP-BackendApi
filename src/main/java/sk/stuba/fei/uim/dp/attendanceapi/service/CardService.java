package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardAlreadyExists;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.repository.CardRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;

import java.util.Optional;

@Service
public class CardService implements ICardService{

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void createCard(CardRequest request, User user) {
        if(this.serialNumberExists(request.getSerialNumber())){
            throw new CardAlreadyExists("Card with this serial number already exists.");
        }
        Card card = new Card(
                user,
                request.getName(),
                request.getSerialNumber()
        );
        this.cardRepository.save(card);
    }

    @Override
    public Card getById(Integer id) {
        Optional<Card> card = this.cardRepository.findById(id);
        if(card.isPresent()){
            return card.get();
        }throw new CardNotFound("Card not found");
    }

    @Override
    public Card getBySerialNumber(String serialNumber) {
        Card card = this.cardRepository.findBySerialNumber(serialNumber);
        if(card == null){
            throw new CardNotFound("Card not found.");
        }
        return card;
    }

    @Override
    public void deleteCard(Integer id) {
        Card card = this.getById(id);
        this.cardRepository.delete(card);
    }

    public Card createCardWithoutUser(String serialNumber){
        if(this.serialNumberExists(serialNumber)){
            throw new CardAlreadyExists("Card with this serial number already exists.");
        }
        Card card = new Card(
                null,
                null,
                serialNumber
        );
        return this.cardRepository.save(card);
    }

    public boolean existsWithUser(String serialNumber){
        Card card = this.cardRepository.findBySerialNumber(serialNumber);
        return card != null && card.getUser() != null;
    }

    public void addUser(User user, Card card){
        card.setUser(user);
        this.cardRepository.save(card);
    }

    private boolean serialNumberExists(String serialNumber){
        return this.cardRepository.findBySerialNumber(serialNumber) != null;
    }
}
