package com.frw.base.web.pages.cadastro;

import com.frw.base.web.pages.base.BasePageWithPanelContent;

/**
 * @author Miller
 */
public class ListEmpresaPage extends BasePageWithPanelContent {

    public ListEmpresaPage() {
        add(new ListEmpresaPanel("content", confirmationModal));
    }
    
    @Override
    protected String getTituloKey() {
        return "titulo.page.empresa";
    }

}
