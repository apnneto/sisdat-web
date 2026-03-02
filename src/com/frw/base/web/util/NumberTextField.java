/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.DoubleConverter;

import com.frw.base.web.DefaultAjaxCallDecorator;

/**
 *
 * @author Leo
 */
public class NumberTextField<T extends Object> extends TextField<T> {

    private static final int DEFAULT_FRACTION_DIGITS = 2;
    private static DoubleConverter doubleConverterDefault;

    private static DoubleConverter integerConverterDefault;

    private synchronized static DoubleConverter getDoubleConverterDefault(Locale locale) {
        if (doubleConverterDefault == null) {
            doubleConverterDefault = new DoubleConverter();
            NumberFormat format = doubleConverterDefault.getNumberFormat(locale);
            format.setMaximumFractionDigits(DEFAULT_FRACTION_DIGITS);
            format.setMinimumFractionDigits(DEFAULT_FRACTION_DIGITS);
            doubleConverterDefault.setNumberFormat(locale, format);
        }
        return doubleConverterDefault;
    }

    private synchronized static DoubleConverter getIntegerConverterDefault(Locale locale) {
        if (integerConverterDefault == null) {
            integerConverterDefault = new DoubleConverter();
            NumberFormat format = integerConverterDefault.getNumberFormat(locale);
            format.setMaximumFractionDigits(0);
            format.setMinimumFractionDigits(0);
            integerConverterDefault.setNumberFormat(locale, format);
        }
        return integerConverterDefault;
    }

    private DoubleConverter doubleConverterCustom;

    private int fractionDigits = DEFAULT_FRACTION_DIGITS;

    AttributeModifier modifier;

    public NumberTextField(String id) {
        super(id);
        setOutputMarkupId(true);
        addFormarBehavior();
    }

    public NumberTextField(String id, Class<T> type) {
        super(id, type);
         setOutputMarkupId(true);
        addFormarBehavior();
    }

    public NumberTextField(String id, IModel<T> model) {
        super(id, model);
         setOutputMarkupId(true);
        addFormarBehavior();
    }

    public NumberTextField(String id, IModel<T> model, Class<T> type) {
        super(id, model, type);
         setOutputMarkupId(true);
        addFormarBehavior();
    }

    public NumberTextField(String id, IModel<T> model, int numeroCasasDecimais) {
        super(id, model);
         setOutputMarkupId(true);
        fractionDigits = numeroCasasDecimais;
        addFormarBehavior();
    }

    @Override
    public IConverter getConverter(Class<?> type) {
        if (type.equals(Integer.class) || type.equals(Long.class)) {
            return getIntegerConverterDefault(getLocale());
        } else {
            if(fractionDigits != DEFAULT_FRACTION_DIGITS) {
                return getDoubleCustomConverter(getLocale());
            } else {
                return getDoubleConverterDefault(getLocale());
            }
        }
    }

    private void addFormarBehavior(){


        if(isSubmitBehavior()){
            addFormatSubmitBehavior();
        }
        else
            addFormatComponenteBehavior();
    }

    private synchronized DoubleConverter getDoubleCustomConverter(Locale locale) {
        if (doubleConverterCustom == null) {
            doubleConverterCustom = new DoubleConverter();
            NumberFormat format = doubleConverterCustom.getNumberFormat(locale);
            format.setMaximumFractionDigits(fractionDigits);
            format.setMinimumFractionDigits(DEFAULT_FRACTION_DIGITS);
            doubleConverterCustom.setNumberFormat(locale, format);
        }
        return doubleConverterCustom;
    }

    protected void addFormatComponenteBehavior() {

        add(new AjaxFormComponentUpdatingBehavior("onblur") {

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                ServletContext servletContext = WebApplication.get().getServletContext();
                return DefaultAjaxCallDecorator.getInstance(servletContext.getContextPath());
            }

            @Override
            protected void onError(final AjaxRequestTarget target, RuntimeException e) {
                getPage().visitChildren(FeedbackPanel.class, new Component.IVisitor<FeedbackPanel>() {

                    @Override
                    public Object component(FeedbackPanel t) {
                        target.addComponent(t);
                        onAfterUpateError(target);
                        return Component.IVisitor.STOP_TRAVERSAL;
                    }
                });
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(this.getComponent());
                onAfterUpate(target);
            }
        });
    }

    protected void addFormatSubmitBehavior() {

        add(new AjaxFormSubmitBehavior("onblur") {

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                ServletContext servletContext = WebApplication.get().getServletContext();
                return DefaultAjaxCallDecorator.getInstance(servletContext.getContextPath());
            }

            @Override
            protected void onError(final AjaxRequestTarget target) {
                getPage().visitChildren(FeedbackPanel.class, new Component.IVisitor<FeedbackPanel>() {

                    @Override
                    public Object component(FeedbackPanel t) {
                        target.addComponent(t);
                        onAfterUpateError(target);
                        return Component.IVisitor.STOP_TRAVERSAL;
                    }
                });
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                target.addComponent(this.getComponent());
                onAfterUpate(target);
            }
        });
    }

    protected boolean isSubmitBehavior() {
        return false;
    }

    protected void onAfterUpate(AjaxRequestTarget target) {
    }

    protected void onAfterUpateError(AjaxRequestTarget target) {
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
         if (this.isEnabled() == false) {
               modifier = new AttributeModifier("style", true, new AbstractReadOnlyModel() {
                @Override
                public String getObject() {
                    return "background:#CCCCC;height:16px; font-family:Verdana, Arial, Helvetica;font-size:1em;color:#A69F94;border:1px #CDCDCD solid;padding-top:3px";
                }
            });

            this.add(modifier);
        }else{
           if (modifier != null) {
                if(this.getBehaviorsRawList().contains(modifier))
                this.remove(modifier);
            }
        }

    }
}
