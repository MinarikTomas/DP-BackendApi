package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class FilterTextField extends TextField {
    public FilterTextField(){
        setValueChangeMode(ValueChangeMode.LAZY);
        getStyle().set("max-width", "100%");
        setClearButtonVisible(true);
        setWidthFull();
    }
}
