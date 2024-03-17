package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardAlreadyExists;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardWithoutUser;
import sk.stuba.fei.uim.dp.attendanceapi.repository.CardRepository;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.NameRequest;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService{

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void createCard(CardRequest request, User user) {
        Card card;
        try{
            card = this.getBySerialNumber(request.getSerialNumber());
            if(card.getUser() != null){
                if(card.getUser() == user && !card.getActive()){
                    card.setActive(true);
                    card.setName(request.getName());
                }else{
                    throw new CardAlreadyExists("Card with this serial number already exists");
                }
            }else{
                card.setUser(user);
                card.setName(request.getName());
            }
        }catch(CardNotFound e){
            card = new Card(
                    user,
                    request.getName(),
                    request.getSerialNumber()
            );
        }
        this.cardRepository.save(card);
    }

    public List<Card> getAll(){
        return this.cardRepository.findAll();
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
    public Card update(NameRequest request, Integer id) {
        Card card = this.getById(id);
        card.setName(request.getName());
        return this.cardRepository.save(card);
    }

    @Override
    public void deactivateCard(Integer id) {
        Card card = this.getById(id);
        if(card.getUser() == null){
            throw new CardWithoutUser("Cannot deactivate card without user.");
        }
        card.setActive(false);
        this.cardRepository.save(card);
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

    public void save(Card card){
        this.cardRepository.save(card);
    }

    public void delete(Card card){
        this.cardRepository.delete(card);
    }

    private boolean serialNumberExists(String serialNumber){
        return this.cardRepository.findBySerialNumber(serialNumber) != null;
    }
}
