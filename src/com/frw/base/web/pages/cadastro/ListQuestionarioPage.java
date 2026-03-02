/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.cadastro;

import com.frw.base.web.pages.base.BasePageWithPanelContent;


public class ListQuestionarioPage extends BasePageWithPanelContent {

    public ListQuestionarioPage() {
        add(new ListQuestionarioPanel("content", confirmationModal));
    }

    @Override
    protected String getTituloKey() {
        return "titulo.page.prova";
    }

}
