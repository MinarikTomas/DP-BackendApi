package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;


public class AddRoleDialog extends Dialog {

    TextField name = new TextField("Name");
    public AddRoleDialog(){
        setHeaderTitle("Add role");
        name.setRequired(true);
        name.setErrorMessage("Cannot be empty");
        Button close = new Button("Close", click -> this.close());
        Button add = new Button("Add", click -> validateAndAdd());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VerticalLayout layout = new VerticalLayout(name);

        getFooter().add(close);
        getFooter().add(add);

        add(layout);
    }

    private void validateAndAdd() {
        if(name.getValue().isEmpty()){
            name.setInvalid(true);
        }else{
            name.setInvalid(false);
            fireEvent(new AddEvent(this, name.getValue()));
        }
    }

    public void setRoleName(String roleName){
        name.setValue(roleName);
    }

    @Getter
    public static abstract class AddRoleDialogEvent extends ComponentEvent<AddRoleDialog> {
        private String role;

        protected AddRoleDialogEvent(AddRoleDialog source, String role){
            super(source, false);
            this.role = role;
        }
    }

    public static class AddEvent extends AddRoleDialogEvent {
        AddEvent(AddRoleDialog source, String role){
            super(source, role);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
