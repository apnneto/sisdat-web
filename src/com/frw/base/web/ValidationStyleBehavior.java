/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;

/**
 * This behavior sets a special css class on the component
 * tag in case it is not valid
 * @author juliano
 */
public class ValidationStyleBehavior extends AbstractTransformerBehavior {

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {

        FormComponent c=(FormComponent)component;
        if(!c.isValid()) {
          
            tag.put("class","form-invalid");
        }
        

    }

    @Override
    public CharSequence transform(Component component, CharSequence cs) throws Exception {

        FormComponent c=(FormComponent)component;
        if(!c.isValid()) {

          FeedbackMessage message=c.getFeedbackMessage();

          message.markRendered();
            return cs+"<br/><span class=\"form-invalid-message\" >"+message.getMessage()+"</span>";
        }

        return cs;
    }



}
