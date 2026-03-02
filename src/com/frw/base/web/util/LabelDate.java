/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import java.util.Date;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.util.SistemaUtil;
import com.frw.base.util.enumeration.FormatoDataEnum;

/**
 *
 * @author Leo
 */
public class LabelDate extends LabelFrw {


    private FormatoDataEnum dateEnum = FormatoDataEnum.DDMMYYYY;

    public LabelDate(String id) {
        super(id);
    }

    public LabelDate(String id, Date value) {
        super(id, new Model<Date>(value));
    }

    public LabelDate(String id, Date value,  FormatoDataEnum enumDate) {
        super(id, new Model<Date>(value));
        this.dateEnum = enumDate;
    }


     public LabelDate(String id, IModel<Date> model) {
        super(id, model);
    }

    public LabelDate(String id, String label) {
        super(id, label);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
//        AttributeModifier modifier = new AttributeModifier("class", true , new Model<String>("alignNumero"));
//        this.add(modifier);
    }

    @Override
    protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
         Date value = (Date)getDefaultModelObject();
         replaceComponentTagBody(markupStream, openTag, SistemaUtil.formatDate(value, dateEnum));

    }
    
}
