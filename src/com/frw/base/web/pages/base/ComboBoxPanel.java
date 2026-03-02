package com.frw.base.web.pages.base;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Carlos Santos
 */
public class ComboBoxPanel<T extends Serializable> extends Panel {

    private DropDownChoice<T> comboBox;

    public ComboBoxPanel(String id, IModel model, IModel choices) {
       super(id);
       comboBox = new DropDownChoice<T>("combobox", model, choices);
       add(comboBox);
    }

    public ComboBoxPanel(String id, IModel model, List choices) {
       super(id);
       comboBox = new DropDownChoice<T>("combobox", model, choices);
       add(comboBox);        
    }   
    
    public DropDownChoice<T> getField() {
        return comboBox;
    }

    public void setField(DropDownChoice<T> field) {
        this.comboBox = field;
    }
    
 }
