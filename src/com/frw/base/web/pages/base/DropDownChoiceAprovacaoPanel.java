package com.frw.base.web.pages.base;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Marcelo Alves
 */
public class DropDownChoiceAprovacaoPanel<T> extends Panel {

    private DropDownChoice<T> comboBox;

    public DropDownChoiceAprovacaoPanel(final String id, IModel<T> model, List<? extends T> choices) {
       super(id);
       comboBox = new DropDownChoice<T>("combobox", model, choices) {

            @Override
            protected String getNullValidKey() {
                return id + ".nullValid";
            }
           
       };
       comboBox.setNullValid(true);
       add(comboBox);

       setOutputMarkupId(true);
    }

    public DropDownChoice<T> getField() {
        return comboBox;
    }

}
