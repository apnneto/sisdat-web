/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.web.util.LabelFrw;

/**
 * Página inicial da aplicação Parcerias
 * @author Leonardo
 */
@AuthorizeInstantiation("valid_user")
public class HomePage extends BasePage {

    public HomePage() {

        initPage(null);

    }

    public HomePage(PageParameters p) {
        initPage(p);

    }

    private void initPage(PageParameters params) {

        LabelFrw message = new LabelFrw("msg");
        add(message);

        if (params != null) {
            if (params.containsKey("mensagem")) {
                message.setDefaultModel(new ResourceModel(params.getString("mensagem", "message.page.tentenovamente")));
            }
        }

    }


}
