/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import java.math.BigDecimal;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.util.SistemaUtil;

/**
 *
 * @author Leo
 */
public class LabelCurrency extends LabelFrw {

     public LabelCurrency(String id) {
        super(id);
    }

    public LabelCurrency(String id, BigDecimal value) {
        super(id, new Model<BigDecimal>(value));
    }

    public LabelCurrency(String id, IModel<?> model) {
        super(id, model);
    }

    public LabelCurrency(String id, String label) {
        super(id, label);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        AttributeModifier modifier = new AttributeModifier("class", true , new Model<String>("alignNumero"));
        this.add(modifier);
    }


    @Override
    protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
         Object value = getDefaultModelObject();
         replaceComponentTagBody(markupStream, openTag, SistemaUtil.formatCurrency(value));

    }

}
