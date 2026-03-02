/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.seguranca;

import com.frw.base.web.pages.base.BasePageWithPanelContent;

/**
 *
 * @author Marcelo Alves
 */
public class ListUsuarioPage extends BasePageWithPanelContent {

    public ListUsuarioPage() {
        add(new ListUsuariosPanel("content", confirmationModal));
    }

    @Override
    protected String getTituloKey() {
        return "titulo.page.usuario";
    }

}
