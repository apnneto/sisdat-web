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
public class LabelNumeric extends LabelFrw {

    private int decimalDigits = 2;
    private boolean isDecimal = true;
    private boolean isMilhar = true;

    public LabelNumeric(String id) {
        super(id);
    }

    public LabelNumeric(String id, BigDecimal value) {
        super(id, new Model<BigDecimal>(value));
    }

    public LabelNumeric(String id, IModel<?> model) {
        super(id, model);
    }

    public LabelNumeric(String id, Number value) {
        super(id, new Model<Number>(value));
    }

    public LabelNumeric(String id, Number value, boolean isMilhar, boolean isDecimal, int decimalDigits) {
        super(id, new Model<Number>(value));
        this.isMilhar = isMilhar;
        this.isDecimal = isDecimal;
        this.decimalDigits = decimalDigits;
    }

    public LabelNumeric(String id, String label) {
        super(id, label);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        AttributeModifier modifier = new AttributeModifier("class", true, new Model<String>("alignNumero"));
        this.add(modifier);
    }

    @Override
    protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        Object value = getDefaultModelObject();
        if (value instanceof Integer) {
            replaceComponentTagBody(markupStream, openTag, value == null ? "" : SistemaUtil.formatNumeric(value, isMilhar, false, 0));
        } else {
            replaceComponentTagBody(markupStream, openTag, value == null ? "" : SistemaUtil.formatNumeric(value, isMilhar, isDecimal, decimalDigits));
        }
    }
}
