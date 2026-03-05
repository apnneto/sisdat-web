package com.frw.base.web.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * @author Carlos Santos
 */
public class AjaxCheckBoxFrw extends AjaxCheckBox {

    public AjaxCheckBoxFrw(final String id) {
        this(id, null);
    }

    public AjaxCheckBoxFrw(final String id, IModel model) {
        super(id, model);
    }

    public boolean isChecked() {
        final String value = getValue();

        if (value != null) {

            try {
                return Strings.isTrue(value);
            } catch (StringValueConversionException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget art) {
        
    }

}
