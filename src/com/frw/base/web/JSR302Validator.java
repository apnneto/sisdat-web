package com.frw.base.web;

import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

class JSR302Validator<T> implements IValidator<T>, INullAcceptingValidator<T> {

    private static transient ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Component component;
    private final IModel currentValue;
    private Class<T> propertyClass;
    private String propertyName;

    public JSR302Validator(String propertyName, Class<T> propertyClass, IModel currentValue,Component component) {
        this.propertyName = propertyName;
        this.propertyClass = propertyClass;
        this.currentValue = currentValue;
        this.component=component;
    }

    public void validate(IValidatable iv) {
        // Only validates changed values
        if (currentValue != null && iv.getValue() != null && iv.getValue().equals(currentValue.getObject())) {
            return;
        }
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validateValue(propertyClass, propertyName, iv.getValue());
        for (ConstraintViolation<T> v : violations) {
            ValidationError error = new ValidationError();
            String errorMessage;
            try {
                String key = v.getMessageTemplate();
                errorMessage = component.getString(v.getMessageTemplate());
                if (errorMessage != null) {
                    errorMessage = replaceVariables(errorMessage, v);
                }
            } catch (Exception e) {
                errorMessage = v.getMessageTemplate();
            }
            error.setMessage(errorMessage);
            iv.error(error);
        }
    }

    private String replaceVariables(String message, ConstraintViolation<T> violation) {
        message = message.replaceAll("\\{fieldName\\}", component.getString(propertyName, null, propertyName));
        Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes();
        for (String attrName : attributes.keySet()) {
            message = message.replaceAll("\\{" + attrName + "\\}", attributes.get(attrName).toString());
        }
        return message;
    }
}
