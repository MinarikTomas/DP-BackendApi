package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;

import java.util.List;

public class DeleteRoleDialog extends Dialog {

    ComboBox<Role> role = new ComboBox<>("Select role");

    public DeleteRoleDialog(){
        setHeaderTitle("Delete role");
        role.setRequired(true);
        role.setErrorMessage("Please select role");
        role.setItemLabelGenerator(Role::getName);

        Button close = new Button("Close", click -> this.close());
        Button delete = new Button("Delete", click -> validateAndDelete());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        VerticalLayout layout = new VerticalLayout(role);

        getFooter().add(close);
        getFooter().add(delete);

        add(layout);
    }

    public void open(List<Role> rolesList) {
        role.setItems(rolesList);
        this.open();
    }

    private void validateAndDelete() {
        if(role.getValue() == null){
            role.setInvalid(true);
        }else{
            role.setInvalid(false);
            fireEvent(new DeleteEvent(this, role.getValue()));
        }
    }

    @Getter
    public static abstract class DeleteRoleDialogEvent extends ComponentEvent<DeleteRoleDialog> {
        private Role role;

        protected DeleteRoleDialogEvent(DeleteRoleDialog source, Role role){
            super(source, false);
            this.role = role;
        }
    }

    public static class DeleteEvent extends DeleteRoleDialog.DeleteRoleDialogEvent {
        DeleteEvent(DeleteRoleDialog source, Role role){
            super(source, role);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
