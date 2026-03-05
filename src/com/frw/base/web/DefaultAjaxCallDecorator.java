/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;

/**
 * Wicket 9: IAjaxCallDecorator replaced by AjaxCallListener / IAjaxCallListener.
 */
public class DefaultAjaxCallDecorator {

    private static DefaultAjaxCallDecorator decorator;

    public static DefaultAjaxCallDecorator getInstance(String webContext) {
        if (decorator == null) {
            decorator = new DefaultAjaxCallDecorator(webContext);
        }
        return decorator;
    }

    private String webContext;

    private DefaultAjaxCallDecorator(String webContext) {
        this.webContext = webContext;
    }

    /** Returns a Wicket 9 AjaxCallListener equivalent to the old IAjaxCallDecorator. */
    public IAjaxCallListener getAjaxCallListener() {
        final String ctx = webContext;
        return new AjaxCallListener()
            .onFailure("alert('Ocorreu um erro durante a comunicação com o servidor. O sistema será redirecionado para a página inicial.');"
                + "window.location.href='" + ctx + "/pages/HomePage';");
    }
}