package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

public class ListColetaPesquisaPanel extends AbstractEntityListPanelNew<ColetaPesquisa> {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ColetaPesquisaFacade coletaPesquisaFacade;

	public ListColetaPesquisaPanel(String id, final UpdatableModalWindow confirmationModal) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
	}

	@Override
	protected void addTableItems(ColetaPesquisa pojo, EntityLabelMap componentMap) {

		componentMap.add("pergunta", new LabelFrw("label", pojo.getPesquisa() != null && pojo.getPergunta() != null ? pojo.getPergunta().getDescricao() : ""));
		componentMap.add("id", new LabelFrw("label", String.valueOf(pojo.getId())));
		componentMap.add("pesquisa", new LabelFrw("label", pojo.getPesquisa() != null ? pojo.getPesquisa().getNumero() : ""));
		componentMap.add("questionario", new LabelFrw("label", pojo.getPesquisa() != null && pojo.getPesquisa().getQuestionario() != null ? pojo.getPesquisa().getQuestionario().getDescricao() : ""));
		componentMap.add("campoLivre", new LabelFrw("label", pojo.getCampoLivre()));
	}

	@Override
	protected Panel getEditPanel(ColetaPesquisa entity, AjaxRequestTarget target) {
		return new EditColetaPesquisaPanel("content", confirmationModal, entity);
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("pesquisa", new FrwResourceModel("coleta.pesquisa.label.pesquisa")));
		columns.add(new EntityColumnInfo("id", new FrwResourceModel("coleta.pesquisa.label.id")));
		columns.add(new EntityColumnInfo("questionario",
				new FrwResourceModel("coleta.pesquisa.label.pesquisa.questionario")));
		columns.add(new EntityColumnInfo("pergunta", new FrwResourceModel("coleta.pesquisa.label.pergunta")));
		columns.add(new EntityColumnInfo("campoLivre", new FrwResourceModel("coleta.pesquisa.label.campo.livre")));

		return columns;
	}

	@Override
	protected List<ColetaPesquisa> loadList() {
		return coletaPesquisaFacade.buscarTodasColetasPesquisa();
	}

	@Override
	protected void removeEntity(ColetaPesquisa entity, AjaxRequestTarget target) throws SistemaException {
		super.removeEntity(entity, target);
		SistemaSession.setUserAndCurrentDate(entity);
		coletaPesquisaFacade.excluirColetaPesquisa(entity);
	}

}
