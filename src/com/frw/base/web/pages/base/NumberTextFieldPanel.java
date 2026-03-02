package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.frw.base.web.util.NumberTextField;

/**
 * @author Leo
 */
public class NumberTextFieldPanel<T> extends Panel {

    protected NumberTextField<T> field;

    public NumberTextFieldPanel(String id, IModel model) {
        super(id);
        field = new NumberTextField<T>("textfield", model);
        add(field);
    }

    public NumberTextField<T> getField() {
        return field;
    }

    public void setField(NumberTextField<T> field) {
        this.field = field;
    }
   
}
