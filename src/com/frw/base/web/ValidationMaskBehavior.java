/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.frw.base.validation.ValidationInputMask;

/**
 *
 * @author juliano
 */
public class ValidationMaskBehavior extends AbstractBehavior implements IHeaderContributor {

    private boolean acceptsNegative;
    private String centsSeparator = ",";
    private int maxCents;
    private int maxInteger;
    private String pattern;
    private String thousandsSeparator = ".";

    public ValidationMaskBehavior(int maxInteger, int maxCents) {
        this.maxInteger = maxInteger;
        this.maxCents = maxCents;
        pattern = null;
    }

    public ValidationMaskBehavior(int maxInteger, int maxCents, String centsSeparator, String thousandsSeparator, boolean acceptsNegative) {
        this.maxInteger = maxInteger;
        this.maxCents = maxCents;
        pattern = null;
        this.centsSeparator = centsSeparator;
        this.thousandsSeparator = thousandsSeparator;
        this.acceptsNegative = acceptsNegative;


    }

    public ValidationMaskBehavior(String pattern) {
        this.pattern = pattern;
    }

    ValidationMaskBehavior(ValidationInputMask mask) {
        this.maxInteger = mask.maxDecimalDigits();
        this.maxCents = mask.maxDigits();
        this.pattern = mask.pattern();
        this.centsSeparator = mask.centsSeparator();
        this.thousandsSeparator = mask.thousandsSeparator();
        this.acceptsNegative = mask.acceptsNegative();
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);

    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {

        if (pattern != null && !pattern.equals("")) {
            tag.getAttributes().put("onfocus", "javascript:InputTextMask.processMaskFocus(this, '" + pattern + "', true);");
        } else {
           // tag.getAttributes().put("onfocus", "javascript:activatePriceMask('#" + component.getMarkupId() + "'," + (maxInteger + maxCents) + "," + maxCents + ",'" + centsSeparator + "','" + thousandsSeparator + "','" + acceptsNegative + "');");
        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {


        if (pattern != null && !pattern.equals("")) {
            response.renderJavascriptReference(new ResourceReference(ValidationMaskBehavior.class, "inputTextMask.js"));
        } else {
          //  response.renderJavascriptReference(new ResourceReference(ValidationMaskBehavior.class, "priceformat_jquery.js"));

           // response.renderJavascript("function activatePriceMask(id,max,cents,centsSeparator,thousandsSeparator,acceptsNegative) { $(id).priceFormat({prefix: '',centsSeparator:centsSeparator,thousandsSeparator:thousandsSeparator,limit:max,centslimit:cents,acceptsNegative: acceptsNegative}) }", "activatePriceMaskFunction");
        }



    }
}
