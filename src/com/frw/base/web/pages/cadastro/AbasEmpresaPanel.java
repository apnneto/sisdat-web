package com.frw.base.web.pages.cadastro;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.web.pages.seguranca.EditEmpresaPanel;
import com.frw.base.web.pages.seguranca.ListQuestionarioEmpresaPanel;
import com.frw.base.web.pages.seguranca.ListUsuarioEmpresaPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AjaxAbasPanel;

/**
 * @author Miller
 */
public class AbasEmpresaPanel extends Panel{

	private static final long serialVersionUID = 1L;
	public UpdatableModalWindow modal;
	private AjaxAbasPanel abas;
	private List<ITab> listAbas;
	private Usuario usuario;
	
	public AbasEmpresaPanel(String id,final UpdatableModalWindow confirmationModal, final Empresa entity) {
		super(id);

		addAbas(confirmationModal, entity);
	}

	public AbasEmpresaPanel(String id, UpdatableModalWindow confirmationModal,
			Usuario usuario) {
		super(id);
		
		this.usuario = usuario;
		addAbas(confirmationModal, usuario.getEmpresa());
	}

	public void addAbas(final UpdatableModalWindow confirmationModal, final Empresa entity) {
		listAbas = new ArrayList<ITab>();
        
        listAbas.add(new AbstractTab(new Model<String>("aba1")) {
			private static final long serialVersionUID = 1L;

			@Override
            public Panel getPanel(String string) {
                return new EditEmpresaPanel(string, confirmationModal, entity, abas);
            }

            @Override
            public IModel<String> getTitle() {
                return new Model<String>(getString("label.empresa"));
            }
        });

        listAbas.add(new AbstractTab(new Model<String>("aba2")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String string) {
				if (usuario != null) {
					return new ListUsuarioEmpresaPanel(string, confirmationModal, usuario);
				}
				return new ListUsuarioEmpresaPanel(string, confirmationModal, entity);
			}

			@Override
			public IModel<String> getTitle() {
				return new Model<String>(getString("label.empresa.funcionarios"));
			}
			
			@Override
			public boolean isVisible() {
				return entity == null || entity.getId() != null;
			}
		});
        
        listAbas.add(new AbstractTab(new Model<String>("aba2")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String string) {
				return new ListQuestionarioEmpresaPanel(string, confirmationModal, entity);
			}

			@Override
			public IModel<String> getTitle() {
				return new Model<String>(getString("label.empresa.questionarios"));
			}
			
			@Override
			public boolean isVisible() {
				return entity == null || entity.getId() != null;
			}
		});
        
        abas = new AjaxAbasPanel("abas", listAbas);

        if (usuario != null) {
        	abas.setSelectedTab(1);
		}
        
        add(abas);
	}

	public AjaxAbasPanel getAbas() {
		return abas;
	}

}
