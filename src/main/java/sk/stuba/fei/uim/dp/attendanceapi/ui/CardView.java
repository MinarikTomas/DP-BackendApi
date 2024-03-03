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
import com.vaadin.flow.server.auth.AnonymousAllowed;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;
import sk.stuba.fei.uim.dp.attendanceapi.service.CardService;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;
import sk.stuba.fei.uim.dp.attendanceapi.ui.component.CardForm;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "cards", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@AnonymousAllowed
public class CardView extends VerticalLayout {

    Grid<Card> grid = new Grid<>(Card.class);
    private final CardForm form;
    TextField idFilterText = createFilterHeader();
    TextField nameFilterText = createFilterHeader();
    TextField activeFilterText = createFilterHeader();
    TextField serialNumberFilterText = createFilterHeader();
    TextField createdAtFilterText = createFilterHeader();
    TextField ownerFilterText = createFilterHeader();


    private CardService cardService;
    private List<Card> cards;

    public CardView(CardService cardService, UserService userService){
        this.cardService = cardService;
        cards = this.cardService.getAll();
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new CardForm(userService);
        form.addListener(CardForm.CloseEvent.class, e -> closeEditor());
        form.addListener(CardForm.SaveEvent.class, this::saveCard);
        form.addListener(CardForm.DeleteEvent.class, this::deleteCard);

        Button add = new Button("Add card", click -> addCard());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        add(add, content);
        updateList(cards);
        closeEditor();
    }

    private void deleteCard(CardForm.DeleteEvent event) {
        cardService.delete(event.getCard());
        updateList(null);
        closeEditor();
    }

    private void saveCard(CardForm.SaveEvent event) {
        try {
            cardService.save(event.getCard());
            updateList(null);
            closeEditor();
        } catch (Exception e) {
            Notification.show("Failed to save card");
        }
    }

    private void addCard() {
        grid.asSingleSelect().clear();
        editCard(new Card());
    }

    private void updateList(List<Card> list) {
        if(list == null){
            cards = this.cardService.getAll();
            grid.setItems(cards);
        }else{
            grid.setItems(list);
        }
    }

    private void configureGrid() {
        grid.addClassName("grid");
        setSizeFull();
        grid.setColumns("id", "active", "serialNumber", "createdAt");
        grid.addColumn(Card::getFormattedOwner).setHeader("Owner").setKey("owner").setSortable(true);
        grid.addColumn(Card::getFormattedName).setHeader("Name").setKey("name").setSortable(true);


        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("id")).setComponent(idFilterText);
        headerRow.getCell(grid.getColumnByKey("name")).setComponent(nameFilterText);
        headerRow.getCell(grid.getColumnByKey("active")).setComponent(activeFilterText);
        headerRow.getCell(grid.getColumnByKey("serialNumber")).setComponent(serialNumberFilterText);
        headerRow.getCell(grid.getColumnByKey("createdAt")).setComponent(createdAtFilterText);
        headerRow.getCell(grid.getColumnByKey("owner")).setComponent(ownerFilterText);

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("serialNumber"),
                grid.getColumnByKey("owner"),
                grid.getColumnByKey("active"),
                grid.getColumnByKey("createdAt")
        );

        grid.asSingleSelect().addValueChangeListener(e -> editCard(e.getValue()));
    }

    private void editCard(Card card) {
        if(card == null){
            closeEditor();
        }else{
            form.setCard(card);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCard(null);
        form.setVisible(false);
        removeClassName("editing");
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

    private List<Card> filterList(){
        return cards.stream()
            .filter(card ->
                filterName(card, nameFilterText.getValue())
                    && filterOwner(card, ownerFilterText.getValue())
                    && card.getActive().toString().toUpperCase().contains(activeFilterText.getValue().toUpperCase())
                    && card.getSerialNumber().toUpperCase().contains(serialNumberFilterText.getValue().toUpperCase())
                    && card.getCreatedAt().toString().contains(createdAtFilterText.getValue())
                    && card.getId().toString().contains(idFilterText.getValue())
            )
            .collect(Collectors.toList());
    }

    private boolean filterName(Card card, String filter){
        if(card.getName() == null){
            return filter.equals("-") || filter.isEmpty();
        }
        return card.getName().toUpperCase().contains(filter.toUpperCase());
    }

    private boolean filterOwner(Card card, String filter){
        if(card.getUser() == null){
            return filter.equals("-") || filter.isEmpty();
        }
        String ownerString = card.getUser().getId() + " " + card.getUser().getFullName();
        return ownerString.toUpperCase().contains(filter.toUpperCase());
    }
}
