/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import java.math.BigDecimal;

import org.apache.wicket.AttributeModifier;
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
        super(id, new Model<>(value));
    }

    public LabelNumeric(String id, IModel<?> model) {
        super(id, model);
    }

    public LabelNumeric(String id, Number value) {
        super(id, new Model<>(value));
    }

    public LabelNumeric(String id, Number value, boolean isMilhar, boolean isDecimal, int decimalDigits) {
        super(id, new Model<>(value));
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
        add(AttributeModifier.replace("class", "alignNumero"));
        Object value = getDefaultModelObject();
        if (value != null) {
            String formatted;
            if (value instanceof Integer) {
                formatted = SistemaUtil.formatNumeric(value, isMilhar, false, 0);
            } else {
                formatted = SistemaUtil.formatNumeric(value, isMilhar, isDecimal, decimalDigits);
            }
            setDefaultModelObject(formatted);
        }
    }
}