/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;


import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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

        if (params != null && params.get("mensagem") != null && !params.get("mensagem").isNull()) {
            message.setDefaultModel(new ResourceModel(params.get("mensagem").toString("message.page.tentenovamente")));
        }

    }


}