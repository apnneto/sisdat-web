/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web;

import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 *
 * @author Marcelo Alves
 */
public class DefaultAjaxCallDecorator implements IAjaxCallDecorator {

    private static DefaultAjaxCallDecorator decorator;
    public static DefaultAjaxCallDecorator getInstance(String webContext) {
        if(decorator == null) {
            decorator = new DefaultAjaxCallDecorator(webContext);
        }

        return decorator;
    }

    private String webContext;

    private DefaultAjaxCallDecorator(String webContext) {
        this.webContext = webContext;
    }

    @Override
    public CharSequence decorateOnFailureScript(CharSequence cs) {

        StringBuilder builder = new StringBuilder();
        builder.append("alert('Ocorreu um erro durante a comunicação com o servidor. O sistema será redirecionado para a página inicial.');");
        builder.append("window.location.href='"+getWebContext()+"/pages/HomePage';");


        return cs + ";" + builder.toString();
    }

    @Override
    public CharSequence decorateOnSuccessScript(CharSequence cs) {
        return cs;
    }

    @Override
    public CharSequence decorateScript(CharSequence cs) {
        return cs;
    }

    private String getWebContext() {
        return webContext;
    }
}
