/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.menu;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.Modulo;
import com.frw.base.negocio.SystemFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author juliano
 */
public class TopMenu extends Panel {

    @EJB
    private SystemFacade systemFacade;

    public TopMenu(String id) {
        
        super(id);
        
        SistemaSession session=(SistemaSession) Session.get();
        if(session.getModuloSelecionado() == null){
        	if(session.getModulosUsuarioLogado() != null)
        		session.setModuloSelecionado(session.getModulosUsuarioLogado().get(0));
        }
        

        LoadableDetachableModel<List<Modulo>> modulesModel=new LoadableDetachableModel<List<Modulo>>() {

            @Override
            protected List<Modulo> load() {

                SistemaSession session=(SistemaSession) Session.get();
                if(session.getModulosUsuarioLogado()!=null)
                    return session.getModulosUsuarioLogado();
                
                return new ArrayList<Modulo>();
            }
        };

        ListView<Modulo> modulos=new ListView<Modulo>("modulesList",modulesModel) {


            private void refreshModel() {
                getModel().detach();
                modelChanged();
            }

            @Override
            protected void populateItem(final ListItem<Modulo> li) {

                SistemaSession session=(SistemaSession)Session.get();
                Modulo moduloSelectionado=session.getModuloSelecionado();
                
                IndicatingAjaxLink link=new IndicatingAjaxLink("moduleLink") {

                    @Override
                    public void onClick(AjaxRequestTarget art) {

                        SistemaSession session=(SistemaSession)Session.get();
                        session.setModuloSelecionado(li.getModelObject());
                        refreshModel();
                        SideMenu sideMenu=((BasePage)getPage()).getSideMenu();
                        sideMenu.refreshModel();
                        art.addComponent(TopMenu.this);
                        art.addComponent(sideMenu);
                    }
                };
                li.add(link);
                link.add(new LabelFrw("moduleName",li.getModelObject().getNome()));
                
                if(li.getModelObject().equals(moduloSelectionado)){
                	 link.add(new AttributeModifier("class", true, new Model<String>("at")));
                }

            }

        };

        add(modulos);
        setOutputMarkupId(true);

    }

}
