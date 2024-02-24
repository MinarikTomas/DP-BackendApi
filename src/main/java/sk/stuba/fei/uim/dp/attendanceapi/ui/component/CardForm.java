package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

public class CardForm extends FormLayout {
    TextField id = new TextField("Id");
    TextField name = new TextField("Name");
    TextField serialNumber = new TextField("Serial number");
    ComboBox<Boolean> active = new ComboBox<>("Active");
    IntegerField user = new IntegerField("Owner id");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    HorizontalLayout toolbar = new HorizontalLayout();

    private UserService userService;

    Binder<Card> binder = new BeanValidationBinder<>(Card.class);

    public CardForm(UserService userService){
        this.userService = userService;
        addClassName("card-form");
        initBinder();
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
        id.setReadOnly(true);
        active.setItems(true, false);
        binder.bindInstanceFields(this);
        createButtonsLayout();

        add(
                toolbar,
                id,
                name,
                serialNumber,
                active,
                user
        );
        setColspan(toolbar, 2);
    }

    private void initBinder() {
        binder.forField(user)
                .withValidator(this::userValidator, "Wrong user id")
                .bind(c -> {
                    if(c.getUser() == null)return null;
                    return c.getUser().getId();
                }, (card, value) -> {
                    if (value == null){
                        card.setUser(null);
                        return;
                    }
                    User user = this.userService.getById(value);
                    card.setUser(user);
                });

    }

    private boolean userValidator(Integer id){
        if(id == null)return true;
        try{
            this.userService.getById(id);
            return true;
        }catch (UserNotFoundException e){
            return false;
        }
    }

    private void createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new CardForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CardForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        toolbar.add(save, delete, close);
    }

    public void setCard(Card card){
        if(card != null){
            if(card.getId() == null){
                id.setVisible(false);
            }else{
                id.setVisible(true);
            }
        }
        binder.setBean(card);
    }

    private void validateAndSave() {
        if(binder.isValid()){
            fireEvent(new CardForm.SaveEvent(this, binder.getBean()));
        }
    }

    @Getter
    public static abstract class CardFormEvent extends ComponentEvent<CardForm> {
        private Card card;

        protected CardFormEvent(CardForm source, Card card){
            super(source, false);
            this.card = card;
        }
    }

    public static class SaveEvent extends CardForm.CardFormEvent {
        SaveEvent(CardForm source, Card card){
            super(source, card);
        }
    }

    public static class DeleteEvent extends CardForm.CardFormEvent {
        DeleteEvent(CardForm source, Card card){
            super(source, card);
        }
    }

    public static class CloseEvent extends CardForm.CardFormEvent {
        CloseEvent(CardForm source){
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
