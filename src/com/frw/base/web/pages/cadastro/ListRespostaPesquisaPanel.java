package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.pages.base.AbstractListRespostaPesquisa;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

public class ListRespostaPesquisaPanel extends AbstractListRespostaPesquisa<Questionario> {

	private static final long serialVersionUID = 1L;

	@EJB
	private QuestionarioFacade questionarioFacade;

	public ListRespostaPesquisaPanel(String id, final UpdatableModalWindow confirmationModal) {
		super(id, confirmationModal);
		setOutputMarkupId(true);

		enableDeleteLink = false;
		enableEditLink = false;
		enableSelectLink = true;
		newLink.setVisible(false);
	}

	@Override
	protected void addTableItems(Questionario pojo, EntityLabelMap componentMap) {

		componentMap.add("codigo", new LabelFrw("label", pojo.getCodigo() != null ? pojo.getCodigo() : ""));
		componentMap.add("descricao", new LabelFrw("label", pojo.getDescricao() != null ? pojo.getDescricao() : ""));
		componentMap.add("orientacao", new LabelFrw("label", pojo.getOrientacao() != null ? resumeString(pojo.getOrientacao()) : ""));
	}

	@Override
	protected Panel getEditPanel(Questionario entity, AjaxRequestTarget target) {
		return new EditRespostaPesquisaPanel("content", confirmationModal, entity);
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("codigo", new FrwResourceModel("questionario.label.id")));
		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel("questionario.label.descricao")));
		columns.add(new EntityColumnInfo("orientacao", new FrwResourceModel("questionario.label.orientacao")));

		return columns;
	}

	@Override
	protected List<Questionario> loadList() {
		return questionarioFacade.buscarQuestionarioComPesquisaPorUsuario(getUsuarioLogado());

	}

	@Override
	protected void removeEntity(Questionario entity, AjaxRequestTarget target) throws SistemaException {
		super.removeEntity(entity, target);
	}

}
