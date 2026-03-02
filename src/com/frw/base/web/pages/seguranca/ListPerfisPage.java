package com.frw.base.web.pages.seguranca;

import com.frw.base.web.pages.base.BasePageWithPanelContent;

/**
 * @author juliano
 * 
 * 
 * 
 *  */
 
public class ListPerfisPage extends BasePageWithPanelContent{

    public ListPerfisPage() {
       add(new ListPerfilPanel("content", confirmationModal));
    }
    
    @Override
    protected String getTituloKey() {
        return "titulo.page.perfil";
    }
}

