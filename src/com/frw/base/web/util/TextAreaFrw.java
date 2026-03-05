/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Leo
 */
public class TextAreaFrw<T extends Object> extends TextArea<T> implements IAjaxIndicatorAware {

    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
    AttributeModifier modifier;

    public TextAreaFrw(String id) {
        super(id);
        setOutputMarkupId(true);
        add(indicatorAppender);
    }

    public TextAreaFrw(String id, IModel<T> model) {
        super(id, model);
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
        if (!this.isEnabled()) {
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