/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Sets a special css class on the component tag when invalid,
 * and appends the validation message inline.
 * Wicket 9: AbstractTransformerBehavior removed; use Behavior + onComponentTag.
 */
public class ValidationStyleBehavior extends Behavior {

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        if (component instanceof FormComponent) {
            FormComponent<?> c = (FormComponent<?>) component;
            if (!c.isValid()) {
                tag.put("class", "form-invalid");
            }
        }
    }
}