/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Leo
 */
public class TextFieldFrw<T extends Object> extends TextField<T> implements IAjaxIndicatorAware {

    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
    AttributeModifier modifier;

    public TextFieldFrw(String id) {
        super(id);
        setOutputMarkupId(true);
        add(indicatorAppender);
    }

    public TextFieldFrw(String id, Class<T> type) {
        super(id, type);
        setOutputMarkupId(true);
        add(indicatorAppender);
    }

    public TextFieldFrw(String id, IModel<T> model) {
        super(id, model);
        setOutputMarkupId(true);
        add(indicatorAppender);
    }

    public TextFieldFrw(String id, IModel<T> model, Class<T> type) {
        super(id, model, type);
        setOutputMarkupId(true);
        add(indicatorAppender);
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return indicatorAppender.getMarkupId();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if (this.isEnabled() == false) {
            modifier = AttributeModifier.replace("style",
                "background:#CCCCC;height:16px; font-family:Verdana, Arial, Helvetica;font-size:1em;color:#A69F94;border:1px #CDCDCD solid;padding-top:3px");
            this.add(modifier);
        } else {
            if (modifier != null && getBehaviors().contains(modifier)) {
                this.remove(modifier);
            }
        }
    }
}
