package sk.stuba.fei.uim.dp.attendanceapi.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.service.ActivityService;
import sk.stuba.fei.uim.dp.attendanceapi.service.CardService;
import sk.stuba.fei.uim.dp.attendanceapi.service.ParticipantService;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;
import sk.stuba.fei.uim.dp.attendanceapi.ui.component.ActivityForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "activity", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@RolesAllowed("ROLE_ADMIN")
public class ActivityView extends VerticalLayout {

    Grid<Activity> grid = new Grid<>(Activity.class);
    private final ActivityForm form;
    TextField idFilterText = createFilterHeader();
    TextField nameFilterText = createFilterHeader();
    TextField createdAtFilterText = createFilterHeader();
    TextField locationFilterText = createFilterHeader();
    TextField timeFilterText = createFilterHeader();
    TextField createdByFilterText = createFilterHeader();
    TextField startTimeFilterText = createFilterHeader();
    TextField endTimeFilterText = createFilterHeader();
    private final ActivityService activityService;
    private List<Activity> activities;

    public ActivityView(ActivityService activityService, UserService userService,
                        CardService cardService, ParticipantService participantService){
        this.activityService = activityService;
        activities = this.activityService.getAll();
        addClassName("list-view");
        setSizeFull();
        setHeightFull();
        configureGrid();

        form = new ActivityForm(userService, cardService, participantService);
        form.addListener(ActivityForm.CloseEvent.class, e -> closeEditor());
        form.addListener(ActivityForm.DeleteEvent.class, this::deleteActivity);
        form.addListener(ActivityForm.SaveEvent.class, this::saveActivity);

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        content.setHeightFull();

        Button add = new Button("Add activity", click -> addActivity());

        add(add, content);
        updateList(activities);
        closeEditor();
    }

    private void saveActivity(ActivityForm.SaveEvent event) {
        try{
            activityService.save(event.getActivity());
            updateList(null);
            closeEditor();
        }catch(Exception e){
            Notification.show("Failed to save activity");
        }
    }

    private void addActivity() {
        grid.asSingleSelect().clear();
        editActivity(new Activity());
    }

    private void deleteActivity(ActivityForm.DeleteEvent event) {
        activityService.deleteActivity(event.getActivity());
        updateList(null);
        closeEditor();
    }

    private void updateList(List<Activity> list) {
        if(list == null){
            activities = activityService.getAll();
            grid.setItems(activities);
        }else{
            grid.setItems(list);
        }
    }

    private void configureGrid() {
        grid.addClassName("grid");
        setSizeFull();
        grid.setColumns("id", "name", "location", "time", "createdAt");
        grid.getStyle().set("height", "100%");
        grid.addColumn(Activity::getFormattedCreatedBy)
                .setSortable(true)
                .setHeader("Created by")
                .setKey("createdBy");
        grid.addColumn(Activity::getFormattedStartTime)
                .setSortable(true)
                .setHeader("Start time")
                .setKey("startTime");
        grid.addColumn(Activity::getFormattedEndTime)
                .setSortable(true)
                .setHeader("End time")
                .setKey("endTime");
        configureFilterHeader();
        grid.asSingleSelect().addValueChangeListener(e -> editActivity(e.getValue()));
    }

    private void editActivity(Activity activity) {
        if(activity == null){
            closeEditor();
        }else{
            form.setActivity(activity);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setActivity(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void configureFilterHeader() {
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("id")).setComponent(idFilterText);
        headerRow.getCell(grid.getColumnByKey("name")).setComponent(nameFilterText);
        headerRow.getCell(grid.getColumnByKey("location")).setComponent(locationFilterText);
        headerRow.getCell(grid.getColumnByKey("time")).setComponent(timeFilterText);
        headerRow.getCell(grid.getColumnByKey("createdAt")).setComponent(createdAtFilterText);
        headerRow.getCell(grid.getColumnByKey("createdBy")).setComponent(createdByFilterText);
        headerRow.getCell(grid.getColumnByKey("startTime")).setComponent(startTimeFilterText);
        headerRow.getCell(grid.getColumnByKey("endTime")).setComponent(endTimeFilterText);
    }

    private TextField createFilterHeader() {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.LAZY);
        textField.getStyle().set("max-width", "100%");
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addValueChangeListener(e -> {
            updateList(filterList());
        });

        return textField;
    }

    private List<Activity> filterList() {
        return activities.stream()
            .filter(activity ->
                activity.getId().toString().contains(idFilterText.getValue())
                    && activity.getName().toUpperCase().contains(nameFilterText.getValue().toUpperCase())
                    && activity.getLocation().toUpperCase().contains(locationFilterText.getValue().toUpperCase())
                    && activity.getTime().toString().contains(timeFilterText.getValue())
                    && activity.getCreatedAt().toString().contains(createdAtFilterText.getValue())
                    && filterCreatedBy(activity, createdByFilterText.getValue())
                    && filterNullableDate(activity.getStartTime(), startTimeFilterText.getValue())
                    && filterNullableDate(activity.getEndTime(), endTimeFilterText.getValue())

            ).collect(Collectors.toList());
    }

    private boolean filterNullableDate(LocalDateTime date, String filter) {
        if(date == null){
            return filter.equals("-") || filter.isEmpty();
        }
        return date.toString().contains(filter) && !filter.equals("-");
    }

    private boolean filterCreatedBy(Activity activity, String filter) {
        String createdByString = activity.getCreatedBy().getId() + " " + activity.getCreatedBy().getFullName();
        return createdByString.toUpperCase().contains(filter.toUpperCase());
    }
}
