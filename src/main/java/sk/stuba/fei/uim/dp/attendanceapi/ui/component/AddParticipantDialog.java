package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class AddParticipantDialog extends Dialog {

    IntegerField cardIdField = new IntegerField("Card id");
    public AddParticipantDialog(){
        setHeaderTitle("Add participant");
        cardIdField.setRequired(true);
        cardIdField.setErrorMessage("Cannot be empty");
        Button close = new Button("Close", click -> this.close());
        Button add = new Button("Add", click -> validateAndAdd());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VerticalLayout layout = new VerticalLayout(cardIdField);

        getFooter().add(close);
        getFooter().add(add);

        add(layout);
    }

    private void validateAndAdd() {
        if(cardIdField.getValue() == null){
            cardIdField.setInvalid(true);
        }else{
            cardIdField.setInvalid(false);
            fireEvent(new AddEvent(this, cardIdField.getValue()));
        }
    }

    public void setCardIdField(Integer value){
        cardIdField.setValue(value);
    }

    @Getter
    public static abstract class AddParticipantDialogEvent extends ComponentEvent<AddParticipantDialog> {
        private final Integer cardId;

        protected AddParticipantDialogEvent(AddParticipantDialog source, Integer cardId){
            super(source, false);
            this.cardId = cardId;
        }
    }

    public static class AddEvent extends AddParticipantDialogEvent {
        AddEvent(AddParticipantDialog source, Integer cardId){
            super(source, cardId);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
