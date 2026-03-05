package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.seguranca.filter.FilterListQuestionarioPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.manutencao.dominio.dto.FilterListQuestionarioDTO;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportQuestionario;

@SuppressWarnings("serial")
public class ListQuestionarioPanel extends AbstractEntityListPanelNew<Questionario> {

	private FilterListQuestionarioDTO filterDTO;
	
	@EJB
	private QuestionarioFacade questionarioFacade;

	public ListQuestionarioPanel(String id, final UpdatableModalWindow confirmationModal) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		filterDTO = new FilterListQuestionarioDTO();
		addFilterQuestinario();
		
		enableDeleteLink = questionarioFacade.isUsuarioAdministrator(getUsuarioLogado());
	}
	
	public ListQuestionarioPanel(String id, final UpdatableModalWindow confirmationModal, boolean isModal) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		filterDTO = new FilterListQuestionarioDTO();
		addFilterQuestinario();
		
		enableDeleteLink = questionarioFacade.isUsuarioAdministrator(getUsuarioLogado());
		
		if(isModal)
			modalModeEnable();
	}
	
	@Override
	public boolean isContentExportableToExcel() {
		return true;
	}

	//filtro questionario
	private void addFilterQuestinario() {
		FilterListQuestionarioPanel panelFilter = new FilterListQuestionarioPanel("searchFilters",listContainer,  filterDTO, questionarioFacade);
        panelFilter.setOutputMarkupId(true);
        filtersContainer.replaceWith(panelFilter);
		
	}

	private void modalModeEnable() {
		this.enableEditLink = false;
		this.newLink.setVisible(false);
		this.enableDeleteLink = false;
		this.enableSelectLink = true;
	}

	@Override
	protected void addTableItems(Questionario pojo, EntityLabelMap componentMap) {

		componentMap.add("descricao", new LabelFrw("label", pojo.getDescricao()));
		componentMap.add("orientacao", new LabelFrw("label", pojo.getOrientacao() != null ? pojo.getOrientacao() : "-"));

	
	}

	@Override
	protected void beforeLoadPage() {
		filterDTO = new FilterListQuestionarioDTO();
	}

	@Override
	protected Panel getEditPanel(Questionario entity, AjaxRequestTarget target) {
		return new AbasQuestionarioPanel("content", confirmationModal, entity, enableDeleteLink);
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel( "questionario.label.descricao")));
		columns.add(new EntityColumnInfo("orientacao", new FrwResourceModel( "questionario.label.orientacao")));

		return columns;
	}

	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportQuestionario();
	}

	@Override
	protected List<Questionario> loadList() {
		return questionarioFacade.pesquisarQuestionarioPorFiltro(filterDTO);
	}
	@Override
	protected void removeEntity(Questionario entity, AjaxRequestTarget target) {
		SistemaSession.setUserAndCurrentDate(entity);
		questionarioFacade.excluirQuestionario(entity);
	}

}
