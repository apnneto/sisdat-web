/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Marcelo Alves
 */
public class ObjetoObsoletoErrorPage extends BasePage {

    public ObjetoObsoletoErrorPage(final Page paginaOrigem, final RuntimeException exception) {
        super();

        LabelFrw label = new LabelFrw("msg", "Não foi possível gravar o registro solicitado pois o mesmo foi atualizado após sua última consulta. Retorne a funcionalidade e tente novamente.");
        add(label);

        AjaxLink retornarOrigem = new AjaxLink("retornar") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                if(paginaOrigem != null)
                    setResponsePage(paginaOrigem);
                else
                    setResponsePage(HomePage.class);
            }
        };
        
        add(retornarOrigem);

    }

    @Override
    protected String getTituloKey() {
        return "titulo.objetoobsoleto.error.page";
    }

}
