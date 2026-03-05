package com.frw.base.web;

import java.util.HashMap;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;

import com.frw.base.validation.ValidationInputMask;

/**
 *
 * This is a base default form Web Bean entities.
 * It supports bean validation using the JSR 303 API.
 * @author juliano
 */
public class BaseWebBeanForm<T> extends Form<T> {

    private static transient ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Behavior defaultBehaviour;
    private boolean editMode;

    private T entity;

    private Class entityClass;
    private boolean validatorsAdded = false;

    public BaseWebBeanForm(String id) {

        super(id);

    }

    public Behavior getDefaultBehaviour() {
        return defaultBehaviour;
    }

    public T getEntity() {
        return entity;
    }

    public Class getEntityClass() {
        return entityClass;
    }
    public boolean isEditMode() {
        return editMode;
    }

    public void setDefaultBehaviour(Behavior defaultBehaviour) {
        this.defaultBehaviour = defaultBehaviour;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setEntity(T entity) {
        this.entity = entity;
        setModel(new CompoundPropertyModel(entity));
        this.entityClass = entity.getClass();
    }
    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    private ValidationInputMask findConstraintMask(PropertyDescriptor p) {

//        for (ConstraintDescriptor c : p.getConstraintDescriptors()) {
//
//            Class constraintAnnotationType = c.getAnnotation().annotationType();
//
//            ValidationInputMask inputMaskAnnotation = (ValidationInputMask) constraintAnnotationType.getAnnotation(ValidationInputMask.class);
//            if (inputMaskAnnotation != null) {
//                return inputMaskAnnotation;
//            }
//
//        }

        return null;
    }

    private ValidationInputMask findValidationMask(String propertyName) {
//        try {
//            // Tenta encontrar uma mascara atraves de anotacao da propria entidade
//            Field propertyField = entityClass.getDeclaredField(propertyName);
//            ValidationInputMask inputMaskAnnotation = propertyField.getAnnotation(ValidationInputMask.class);
//            if (inputMaskAnnotation != null) {
//                return inputMaskAnnotation;
//            }
//
//        } catch (NoSuchFieldException ex) {
//            Logger.getLogger(BaseWebBeanForm.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        } catch (SecurityException ex) {
//            Logger.getLogger(BaseWebBeanForm.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }

        return null;

    }

    private Behavior getMaskBehavior(PropertyDescriptor p) {


//        ValidationInputMask mask = findConstraintMask(p);
//
//        if (mask == null) {
//            mask = findValidationMask(p.getPropertyName());
//        }
//
//
//
//        if (mask != null) {
//            return new ValidationMaskBehavior(mask);
//        }

        return null;




    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (!validatorsAdded) {
            final BeanDescriptor entityBeanInfo;


            entityBeanInfo = factory.getValidator().getConstraintsForClass(entityClass);

            final HashMap<String, PropertyDescriptor> entityProperties = new HashMap<String, PropertyDescriptor>();

            for (PropertyDescriptor p : entityBeanInfo.getConstrainedProperties()) {
                entityProperties.put(p.getPropertyName(), p);
            }

            visitChildren(FormComponent.class, (t, visit) -> {
                    FormComponent c = (FormComponent) t;
                    PropertyDescriptor pd = entityProperties.get(t.getId());
                    if (pd != null) {
                        c.add(new JSR302Validator<T>(t.getId(), entityClass, editMode ? c.getModel() : null, BaseWebBeanForm.this));
                    }
                    if (defaultBehaviour != null) {
                        c.add(defaultBehaviour);
                    }
            });

            validatorsAdded = true;
        }

    }
}
