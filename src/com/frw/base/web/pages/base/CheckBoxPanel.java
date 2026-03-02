package com.frw.base.web.pages.base;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Carlos Santos
 */
public class CheckBoxPanel extends Panel {

    private AjaxCheckBox field;

    public CheckBoxPanel(String id, IModel model) {
        super(id);
        field = new AjaxCheckBox("checkbox", model) {

            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                onUpdateAjaxEvent(art);
            }
        };

        add(field);
    }

    public CheckBox getField() {
        return field;
    }

    public void onUpdateAjaxEvent(AjaxRequestTarget art) {
    }

    public void setField(AjaxCheckBox field) {
        this.field = field;
    }

}
