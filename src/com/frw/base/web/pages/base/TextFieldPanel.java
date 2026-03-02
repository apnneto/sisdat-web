package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Carlos Santos
 */
public class TextFieldPanel extends Panel {

    protected TextFieldFrw field;

    public TextFieldPanel(String id, IModel model) {
        super(id);
        field = new TextFieldFrw("textfield", model);
        add(field);
    }

    public TextField getField() {
        return field;
    }

    public void setField(TextFieldFrw field) {
        this.field = field;
    }
    
}
