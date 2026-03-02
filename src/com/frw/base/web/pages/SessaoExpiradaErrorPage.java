/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 *
 * @author Marcelo Alves
 */
public class SessaoExpiradaErrorPage extends BasePage {

    public SessaoExpiradaErrorPage() {

//        LabelFrw label = new LabelFrw("msg", "A sessão de acesso ao sistema expirou, por favor realize o login novamente.");
//        add(label);

        AjaxLink homeLink = new AjaxLink("homeLink") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                setResponsePage(HomePage.class);
            }
        };

        add(homeLink);

    }

    @Override
    public boolean isErrorPage() {
        return true;
    }

    @Override
    protected String getTituloKey() {
        return "vazio";
    }
}
