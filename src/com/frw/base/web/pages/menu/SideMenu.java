/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.menu;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.negocio.SystemFacade;
import com.frw.base.web.FuncionalidadesMapper;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author juliano
 */
public class SideMenu extends Panel {

    @EJB
    private SystemFacade facade;
    private ListView<Funcionalidade> funcionalidadesList;
    private Usuario usuarioLogado;

    public SideMenu(String id, Usuario userLogado) {
        super(id);

        usuarioLogado = userLogado;

        LoadableDetachableModel<List<Funcionalidade>> funcionalidades = new LoadableDetachableModel<List<Funcionalidade>>() {

            @Override
            protected List<Funcionalidade> load() {

                SistemaSession session = (SistemaSession) Session.get();
                if (session.getUsuarioLogado() == null) {
                    return new ArrayList<Funcionalidade>();
                }

                if (session.getFuncionalidadesUsuarioLogado(session.getModuloSelecionado()) != null) {
                    return session.getFuncionalidadesUsuarioLogado(session.getModuloSelecionado());
                }

                List<Funcionalidade> funcionalidades = facade.getFuncionalidades(session.getUsuarioLogado(), session.getModuloSelecionado());
                session.setFuncionalidadesUsuarioLogado(session.getModuloSelecionado(), funcionalidades);
                return funcionalidades;

            }
        };


        funcionalidadesList = new ListView<Funcionalidade>("menuItemsList", funcionalidades) {

            @Override
            protected void populateItem(final ListItem<Funcionalidade> li) {

                AjaxLink menuItemLink = new AjaxLink("menuItemLink") {

                    @Override
                    public void onClick(AjaxRequestTarget art) {

                        SistemaSession session = (SistemaSession) Session.get();
                        session.setFuncionalidadeSelecionada(li.getModelObject());
                        redirectToFuncionalidadePage(li.getModelObject());



                    }
                };

                li.add(menuItemLink);
                SistemaSession session = (SistemaSession) Session.get();
                LabelFrw menuLabel = new LabelFrw("menuLabel", li.getModelObject().getDescricao());

                if (((session.getFuncionalidadeSelecionada() != null && li.getModelObject().equals(session.getFuncionalidadeSelecionada())))
                        || (session.getFuncionalidadeSelecionada() == null && li.getIndex() == 0)) {
                	menuItemLink.add(new AttributeModifier("class", true, new Model<String>("at")));
                }

                menuItemLink.add(menuLabel);


            }
        };
        add(funcionalidadesList);

        setOutputMarkupId(true);






    }

    public void refreshModel() {
        funcionalidadesList.getModel().detach();
        funcionalidadesList.modelChanged();
    }

    private void redirectToFuncionalidadePage(Funcionalidade funcionalidade) {
        Class o = FuncionalidadesMapper.getPageForFuncionalidade(funcionalidade);

        PageParameters parameters = new PageParameters();
        parameters.add("funcionalidade", funcionalidade.getId().toString());
        setResponsePage((Class) o, parameters);
    }
}
