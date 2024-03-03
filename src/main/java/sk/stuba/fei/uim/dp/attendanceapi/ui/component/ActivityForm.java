package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Participant;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.service.CardService;
import sk.stuba.fei.uim.dp.attendanceapi.service.ParticipantService;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.time.Duration;
import java.util.ArrayList;

public class ActivityForm extends FormLayout {
    TextField id = new TextField("Id");
    TextField name = new TextField("Name");
    TextField location = new TextField("Location");
    DateTimePicker time = new DateTimePicker("Time");
    IntegerField user = new IntegerField("User id");
    DateTimePicker startTime = new DateTimePicker("Start time");
    DateTimePicker endTime = new DateTimePicker("End time");

    Grid<Participant> participantsGrid = new Grid<>(Participant.class);
    VerticalLayout gridLayout = new VerticalLayout();

    private final AddParticipantDialog addParticipantDialog;

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    HorizontalLayout toolbar = new HorizontalLayout();
    Binder<Activity> binder = new BeanValidationBinder<>(Activity.class);

    private UserService userService;

    private CardService cardService;
    private ParticipantService participantService;

    public ActivityForm(UserService userService, CardService cardService, ParticipantService participantService){
        this.userService = userService;
        this.cardService = cardService;
        this.participantService = participantService;
        addClassName("form");
        initBinder();
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
        id.setReadOnly(true);
        startTime.setReadOnly(true);
        endTime.setReadOnly(true);
        time.setStep(Duration.ofMinutes(30));
        binder.bindInstanceFields(this);
        createButtonsLayout();

        configureParticipantsGrid();

        addParticipantDialog = new AddParticipantDialog();
        addParticipantDialog.addListener(AddParticipantDialog.AddEvent.class, this::addParticipant);
        addParticipantDialog.addListener(AddParticipantDialog.DeleteEvent.class, this::deleteParticipant);

        gridLayout.add(
                new NativeLabel("Participants"),
                new Button("Add Participant", click -> addParticipantDialog.open()),
                participantsGrid
        );

        add(
                toolbar,
                id,
                name,
                location,
                time,
                user,
                startTime,
                endTime,
                gridLayout
        );
        setColspan(toolbar, 2);
        setColspan(gridLayout, 2);
    }

    private void deleteParticipant(AddParticipantDialog.DeleteEvent deleteEvent) {

    }

    private void addParticipant(AddParticipantDialog.AddEvent event) {
        try{
            Card card = cardService.getById(event.getCardId());
            participantService.save(new Participant(binder.getBean(), card));
            addParticipantDialog.close();
            addParticipantDialog.setCardIdField(null);
            refreshParticipants();
        }catch (CardNotFound e){
            Notification.show("Invalid card id");
        }catch (Exception e){
            Notification.show("Failed to add participant");
        }
    }

    private void configureParticipantsGrid() {
        participantsGrid.removeAllColumns();
        participantsGrid.addColumn(this::getCardId).setHeader("Card id")
                .setKey("cardId")
                .setSortable(true);
        participantsGrid.addColumn(this::getCardUserId).setHeader("User id")
                .setSortable(true)
                .setKey("userId");
        participantsGrid.addColumn(this::getCardUserFullName).setHeader("Name")
                .setSortable(true)
                .setKey("UserName");
        participantsGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, participnat) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeParticipant(participnat));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })
        ).setHeader("Manage").setKey("manage");
        participantsGrid.getColumns().forEach(participantColumn -> participantColumn.setAutoWidth(true));
    }

    private void removeParticipant(Participant participnat) {
        if(participnat == null){
            return;
        }
        participantService.delete(participnat);
        refreshParticipants();
    }

    private void refreshParticipants(){
        participantsGrid.setItems(participantService.getAllByActivityId(
                binder.getBean().getId()
        ));
    }

    private String getCardId(Participant participant) {
        return participant.getCard().getId().toString();
    }

    private String getCardUserFullName(Participant participant) {
        if(participant.getCard().getUser() == null){
            return "-";
        }
        return participant.getCard().getUser().getFullName();
    }

    private String getCardUserId(Participant participant) {
        if(participant.getCard().getUser() == null){
            return "-";
        }
        return participant.getCard().getUser().getId().toString();
    }

    private void createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        toolbar.add(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()){
            fireEvent(new ActivityForm.SaveEvent(this, binder.getBean()));
        }
    }

    private void initBinder() {
        binder.forField(user)
                .withValidator(this::userValidator, "Wrong user id")
                .bind(activity -> {
                    if(activity.getCreatedBy() == null)return null;
                    return activity.getCreatedBy().getId();
                        },
                        (activity, value) -> {
                            User user = this.userService.getById(value);
                            activity.setCreatedBy(user);
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

    public void setActivity(Activity activity){
        if(activity != null){
            if(activity.getId() == null){
                handleNewActivity();
            }else{
                handleExistingActivity(activity);
            }
        }
        binder.setBean(activity);
    }

    private void handleExistingActivity(Activity activity) {
        startTime.setVisible(true);
        endTime.setVisible(true);
        id.setVisible(true);
        if(activity.getStartTime() != null){
            gridLayout.setVisible(true);
            participantsGrid.setItems(activity.getParticipants());
        }else{
            gridLayout.setVisible(false);
            participantsGrid.setItems(new ArrayList<>());
        }
    }

    private void handleNewActivity() {
        id.setVisible(false);
        startTime.setVisible(false);
        endTime.setVisible(false);
    }

    @Getter
    public static abstract class ActivityFormEvent extends ComponentEvent<ActivityForm> {
        private Activity activity;

        protected ActivityFormEvent(ActivityForm source, Activity activity){
            super(source, false);
            this.activity = activity;
        }
    }

    public static class SaveEvent extends ActivityFormEvent {
        SaveEvent(ActivityForm source, Activity activity){
            super(source, activity);
        }
    }

    public static class DeleteEvent extends ActivityFormEvent {
        DeleteEvent(ActivityForm source, Activity activity){
            super(source, activity);
        }
    }

    public static class CloseEvent extends ActivityFormEvent {
        CloseEvent(ActivityForm source){
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
