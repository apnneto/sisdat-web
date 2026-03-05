/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.frw.base.validation.ValidationInputMask;

/**
 *
 * @author juliano
 */
public class ValidationMaskBehavior extends Behavior {

    private boolean acceptsNegative;
    private String centsSeparator = ",";
    private int maxCents;
    private int maxInteger;
    private String pattern;
    private String thousandsSeparator = ".";

    public ValidationMaskBehavior(int maxInteger, int maxCents) {
        this.maxInteger = maxInteger;
        this.maxCents = maxCents;
    }

    public ValidationMaskBehavior(int maxInteger, int maxCents, String centsSeparator, String thousandsSeparator, boolean acceptsNegative) {
        this.maxInteger = maxInteger;
        this.maxCents = maxCents;
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
        }

    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {


        if (pattern != null && !pattern.equals("")) {
            response.render(JavaScriptReferenceHeaderItem.forReference(
                new PackageResourceReference(ValidationMaskBehavior.class, "inputTextMask.js")));
        }

    }
}