/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

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
        }
        return doubleConverterDefault;
    }

    private synchronized static DoubleConverter getIntegerConverterDefault(Locale locale) {
        if (integerConverterDefault == null) {
            integerConverterDefault = new DoubleConverter();
            NumberFormat format = integerConverterDefault.getNumberFormat(locale);
            format.setMaximumFractionDigits(0);
            format.setMinimumFractionDigits(0);
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
    public <C> IConverter<C> getConverter(Class<C> type) {
        if (type.equals(Integer.class) || type.equals(Long.class)) {
            return (IConverter<C>) getIntegerConverterDefault(getLocale());
        } else {
            if (fractionDigits != DEFAULT_FRACTION_DIGITS) {
                return (IConverter<C>) getDoubleCustomConverter(getLocale());
            } else {
                return (IConverter<C>) getDoubleConverterDefault(getLocale());
            }
        }
    }

    private void addFormarBehavior() {
        if (isSubmitBehavior()) {
            addFormatSubmitBehavior();
        } else {
            addFormatComponenteBehavior();
        }
    }

    private synchronized DoubleConverter getDoubleCustomConverter(Locale locale) {
        if (doubleConverterCustom == null) {
            doubleConverterCustom = new DoubleConverter();
            NumberFormat format = doubleConverterCustom.getNumberFormat(locale);
            format.setMaximumFractionDigits(fractionDigits);
            format.setMinimumFractionDigits(DEFAULT_FRACTION_DIGITS);
        }
        return doubleConverterCustom;
    }

    protected void addFormatComponenteBehavior() {
        add(new AjaxFormComponentUpdatingBehavior("blur") {
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                String ctx = WebApplication.get().getServletContext().getContextPath();
                attributes.getAjaxCallListeners().add(DefaultAjaxCallDecorator.getInstance(ctx).getAjaxCallListener());
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                getPage().visitChildren(FeedbackPanel.class, (fp, visit) -> {
                    target.add(fp);
                    onAfterUpateError(target);
                    visit.stop();
                });
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getComponent());
                onAfterUpate(target);
            }
        });
    }

    protected void addFormatSubmitBehavior() {
        add(new AjaxFormSubmitBehavior("blur") {
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                String ctx = WebApplication.get().getServletContext().getContextPath();
                attributes.getAjaxCallListeners().add(DefaultAjaxCallDecorator.getInstance(ctx).getAjaxCallListener());
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                getPage().visitChildren(FeedbackPanel.class, (fp, visit) -> {
                    target.add(fp);
                    onAfterUpateError(target);
                    visit.stop();
                });
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                target.add(getComponent());
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