/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.cadastro;

import com.frw.base.web.pages.base.BasePageWithPanelContent;

public class ListColetaPesquisaPage extends BasePageWithPanelContent {

    public ListColetaPesquisaPage() {
        add(new ListColetaPesquisaPanel("content", confirmationModal));
    }

    @Override
    protected String getTituloKey() {
        return "titulo.page.coleta.pesquisa";
    }

}
