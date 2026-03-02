/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Leo
 */
public class MyErrorPage extends BasePage {

    public MyErrorPage() {

        LabelFrw label = new LabelFrw("msg", "Infelizmente occoreu um erro no sistema, tente executar esta tarefa mais tarde.");
        add(label);

    }

    public MyErrorPage(RuntimeException ex) {

        LabelFrw label = new LabelFrw("msg", "Infelizmente occoreu um erro no sistema, tente executar esta tarefa mais tarde.");
        add(label);

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);

        StringBuilder message = new StringBuilder();

        if(ex.getCause() != null){

        }

        Throwable erro = ExceptionUtils.getRootCause(ex);
        StringBuilder messageErro = new StringBuilder("");

        if (erro != null) {
            messageErro.append("Exception : ").append(erro.toString())
                    .append("<br/> ## Message: ").append(erro.getMessage())
                    .append("<br/> ## Message: ").append(ex.toString());
        }

        final LabelFrw lblExcecao = new LabelFrw("exececao", messageErro.toString());
        lblExcecao.setVisible(false);
        lblExcecao.setEscapeModelStrings(false);

        container.add(lblExcecao);

        AjaxLink exibirDetalhes = new AjaxLink("exibirDetalhesLink") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                if (lblExcecao.isVisible()) {
                    lblExcecao.setVisible(false);
                } else {
                    lblExcecao.setVisible(true);
                }

                art.addComponent(container);
            }
        };

        container.add(exibirDetalhes);

        add(container);
    }

    @Override
    public boolean isErrorPage() {
        return true;
    }

    private Throwable getRootException(Throwable ex){

        if(ex.getCause() != null)
            return getRootException(ex.getCause());
        else
            return ex;
        
    }

    @Override
    protected String getTituloKey() {
        return "titulo.error.page";
    }
}
