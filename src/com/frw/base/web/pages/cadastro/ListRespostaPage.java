/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.cadastro;

import com.frw.base.web.pages.base.BasePageWithPanelContent;

/**
 *
 * @author Leonardo Barros
 */
public class ListRespostaPage extends BasePageWithPanelContent {

    public ListRespostaPage() {
        add(new ListRespostaPanel("content", confirmationModal));
    }

    @Override
    protected String getTituloKey() {
        return "titulo.page.resposta";
    }

}
