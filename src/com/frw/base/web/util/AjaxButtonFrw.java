/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.util;

import javax.servlet.ServletContext;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;

import com.frw.base.web.DefaultAjaxCallDecorator;

/**
 *
 * @author Marcelo Alves
 */
public abstract class AjaxButtonFrw extends IndicatingAjaxButton {

    public AjaxButtonFrw(String id) {
        super(id);
    }

    public AjaxButtonFrw(String id, Form<?> form) {
        super(id, form);
    }

    public AjaxButtonFrw(String id, IModel<String> model) {
        super(id, model);
    }

    public AjaxButtonFrw(String id, IModel<String> model, Form<?> form) {
        super(id, model, form);
    }

    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        ServletContext servletContext = WebApplication.get().getServletContext();
        return DefaultAjaxCallDecorator.getInstance(servletContext.getContextPath());
    }

}
