/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import com.frw.base.web.ValidationMaskBehavior;

/**
 *
 * @author juliano
 */
public class NumberTextFieldOLD<T extends Number> extends TextFieldFrw<T> {

    

    public static class NumberTextFieldConfiguration {
        private boolean acceptsNegativeNumbers;
        private Integer centsLimit=2;
        private String centsSeparator=",";
        private Integer limit=99;
        private String thousandsSeparator=".";

        public Integer getCentsLimit() {
            return centsLimit;
        }

        public String getCentsSeparator() {
            return centsSeparator;
        }

        public Integer getLimit() {
            return limit;
        }

        public String getThousandsSeparator() {
            return thousandsSeparator;
        }

        public boolean isAcceptsNegativeNumbers() {
            return acceptsNegativeNumbers;
        }

        public void setAcceptsNegativeNumbers(boolean acceptsNegativeNumbers) {
            this.acceptsNegativeNumbers = acceptsNegativeNumbers;
        }

        public void setCentsLimit(Integer centsLimit) {
            this.centsLimit = centsLimit;
        }

        public void setCentsSeparator(String centsSeparator) {
            this.centsSeparator = centsSeparator;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public void setThousandsSeparator(String thousandsSeparator) {
            this.thousandsSeparator = thousandsSeparator;
        }
        
    }

    private NumberTextFieldConfiguration configuration;

    private DecimalFormat df;

    public NumberTextFieldOLD(String id) {
        super(id);
        configuration=new NumberTextFieldConfiguration();
        init();

    }



    public NumberTextFieldOLD(String id, IModel<T> model) {
        super(id,model);
        configuration=new NumberTextFieldConfiguration();
        init();

    }
    public NumberTextFieldOLD(String id, IModel<T> model,NumberTextFieldConfiguration config) {
        super(id,model);
        configuration=config;
        init();
    }

    public NumberTextFieldOLD(String id,NumberTextFieldConfiguration config) {
        super(id);
        configuration=config;
        init();
    }
    @Override
    public <C> org.apache.wicket.util.convert.IConverter<C> getConverter(Class<C> type) {
        return (org.apache.wicket.util.convert.IConverter<C>) new org.apache.wicket.util.convert.IConverter() {

            @Override
            public Object convertToObject(String string, Locale locale) {
                try {
                    if(configuration.getThousandsSeparator()!=null && !configuration.getThousandsSeparator().equals(""))
                        string=string.replace(configuration.getThousandsSeparator(), "");
                    if(string.equals(""))
                        return 0;

                    return df.parse(string);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                   throw new ConversionException(ex);
                }
                catch (Exception e){
                    e.printStackTrace();
                    throw new ConversionException(e);
                }
            }

            @Override
            public String convertToString(Object o, Locale locale) {
                return df.format(o);
            }
        };

    }

    private void init() {

        ValidationMaskBehavior maskBehavior=new ValidationMaskBehavior(configuration.getLimit(),configuration.getCentsLimit(),configuration.getCentsSeparator(),configuration.getThousandsSeparator(),configuration.acceptsNegativeNumbers);
        add(maskBehavior);

         df=new DecimalFormat("###,##0");
         if(configuration.getCentsSeparator()!=null && !configuration.getCentsSeparator().equals(""))
             df=new DecimalFormat("###,##0.00");

        DecimalFormatSymbols symbols=new DecimalFormatSymbols();


        if(configuration.getThousandsSeparator()!=null && !configuration.getThousandsSeparator().equals("")) {
            symbols.setGroupingSeparator(configuration.getThousandsSeparator().charAt(0));
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
        }

        if(configuration.getCentsSeparator()!=null && !configuration.getCentsSeparator().equals("")) {
            symbols.setDecimalSeparator(configuration.getCentsSeparator().charAt(0));
            df.setDecimalSeparatorAlwaysShown(true);
        }

        df.setDecimalFormatSymbols(symbols);

    }



    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if(this.isEnabled() == false){
            this.add(AttributeModifier.replace("style", "background-color:#EEEEEE"));
        }
    }


/*
    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        Number number=getModelObject();
        if(number!=null) {

            tag.getAttributes().put("value", df.format(number));
        }
    }

    @Override
    protected T convertValue(String[] value) throws ConversionException {

        try {
            return (T) df.parse(value[0]);
        }
        catch(Exception e) {
            throw new ConversionException("Invalid number value");
            
        }
    }



*/


}
