package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.frw.base.web.util.DateTextFieldCustom;

/**
 * @author Marcelo
 */
public class DateTextFieldCustomPanel extends Panel {

    protected DateTextFieldCustom field;

    public DateTextFieldCustomPanel(String id, IModel model) {
        super(id);
        field = new DateTextFieldCustom("datetextfield", model);
        add(field);
    }

    public DateTextFieldCustom getField() {
        return field;
    }

    public void setField(DateTextFieldCustom field) {
        this.field = field;
    }

}
