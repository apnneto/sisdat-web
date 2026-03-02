/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.util;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Leo
 */
public class DateTimeTextFieldCustom extends DateTextField {

    public DateTimeTextFieldCustom(String id, IModel model) {
        this(id, model, "dd/MM/yyyy");
    }

    public DateTimeTextFieldCustom(String id, IModel model, String pattern) {
        super(id, model, pattern);
        add(new DatePicker() {
        	
            @Override
            protected boolean notifyComponentOnDateSelected() {
                return Boolean.TRUE;
            }

            
        });
        
    }

}
