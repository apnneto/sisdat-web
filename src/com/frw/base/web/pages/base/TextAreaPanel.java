package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Carlos Santos
 */
public class TextAreaPanel extends Panel {

    private TextArea field;

    public TextAreaPanel(String id, IModel model) {
        super(id);
        field = new TextArea("textarea", model);
        add(field);
    }

    public TextArea getField() {
        return field;
    }

    public void setField(TextArea field) {
        this.field = field;
    }

}
