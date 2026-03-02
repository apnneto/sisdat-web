package com.frw.base.web.pages.cadastro;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AjaxAbasPanel;

public class AbasQuestionarioPanel extends Panel{

	/**
	 *
	 * @author Igor Pessanha
	 */
	private static final long serialVersionUID = 1L;
	public UpdatableModalWindow modal;
	private AjaxAbasPanel abas;
	private boolean hasPermissionDelete;
	private List<ITab> listAbas;
	
	public AbasQuestionarioPanel(String id,final UpdatableModalWindow confirmationModal, final Questionario entity, boolean hasPermissionDelete) {
		super(id);
		this.hasPermissionDelete = hasPermissionDelete;
		addAbas(confirmationModal, entity);
	}

	public void addAbas(final UpdatableModalWindow confirmationModal, final Questionario entity) {
		listAbas = new ArrayList<ITab>();
        
        listAbas.add(new AbstractTab(new Model<String>("aba1")) {
            @Override
            public Panel getPanel(String string) {
                return new EditQuestionarioPanel(string, confirmationModal, entity, abas, hasPermissionDelete);
            }

            @Override
            public IModel<String> getTitle() {
                return new Model<String>("Questionário");
            }
        });

        listAbas.add(new AbstractTab(new Model<String>("aba2")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String string) {
				return new ListPerguntaPanel(string, confirmationModal, entity, hasPermissionDelete);
			}

			@Override
			public IModel<String> getTitle() {
				return new Model<String>("Pergunta");
			}
			
			@Override
			public boolean isVisible() {
				return entity.getId() != null;
			}
		});
        
        abas = new AjaxAbasPanel("abas", listAbas);
        add(abas);
	}

	public AjaxAbasPanel getAbas() {
		return abas;
	}

}
