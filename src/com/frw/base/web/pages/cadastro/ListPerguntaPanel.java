package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.TipoPergunta;
import com.frw.base.negocio.quiz.PerguntaFacade;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.negocio.quiz.TipoPerguntaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.seguranca.filter.FilterListPerguntaDescricaoPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.manutencao.dominio.dto.FilterPerguntaDescricaoDTO;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportQuestionario;

/**
 * 
 * @author Leonardo Barros
 */
public class ListPerguntaPanel extends AbstractEntityListPanelNew<Pergunta> {

	private FilterPerguntaDescricaoDTO filterDTO;
	
	private String id;
	private ListPerguntaPanel listPerguntaPanel;
	
	private List<TipoPergunta> listTipoPergunta;
	@EJB
	private PerguntaFacade perguntaFacade;
	private Questionario questionario;
	@EJB
	private QuestionarioFacade questionarioFacade;
	@EJB
	private TipoPerguntaFacade tipoPerguntaFacade;


	public ListPerguntaPanel(String id, final UpdatableModalWindow confirmationModal, boolean selectedPanel) {
		super(id, confirmationModal);
		setOutputMarkupId(true);

		if (selectedPanel == true) {
			enableDeleteLink = false;
			enableEditLink = false;
			enableSelectLink = true;
			newLink.setVisible(false);
			exportarExcelLink.setVisible(false);
		}

	}
	
	

	public ListPerguntaPanel(String id, final UpdatableModalWindow confirmationModal, Questionario questionario, boolean hasPermissionDelete) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		this.id = id;
		filterDTO = new FilterPerguntaDescricaoDTO();
		listTipoPergunta = tipoPerguntaFacade.buscarTodosTipoPerguntas();
		listPerguntaPanel = this;
		this.questionario = questionario;
		addFilterPergunta();
		
		enableDeleteLink = hasPermissionDelete;
		
	}

	@Override
	public boolean isContentExportableToExcel() {
		return true;
	}
	
	// filtro questionario
	private void addFilterPergunta() {
		FilterListPerguntaDescricaoPanel panelFilter = new FilterListPerguntaDescricaoPanel("searchFilters", listContainer, filterDTO, perguntaFacade);
		panelFilter.setOutputMarkupId(true);
		filtersContainer.replaceWith(panelFilter);

	}


	@Override
	protected void addTableItems(Pergunta pojo, EntityLabelMap componentMap) {

		componentMap.add("ordem", new LabelFrw("label", pojo.getOrdem().toString()));
		componentMap.add("descricao", new LabelFrw("label", pojo.getDescricao() != null ? resumeString(pojo.getDescricao()) : ""));
		componentMap.add("subdescricao", new LabelFrw("label", pojo.getSubdescricao() != null ? resumeString(pojo.getSubdescricao()) :""));
		componentMap.add("tipo", new LabelFrw("label", pojo.getTipo() != null ? resumeString(pojo.getTipo().getDescricao()) : ""));
	}

	@Override
	protected void beforeLoadPage() {
		filterDTO = new FilterPerguntaDescricaoDTO();
	}

	@Override
	protected Panel getEditPanel(final Pergunta entity, AjaxRequestTarget target) {
		
		if(questionario != null && questionario.getId() != null){
			entity.setQuestionario(questionario);
			entity.setOrdem(perguntaFacade.ultimoIdentificadorOrdem(entity) + 1);
		}
		if (entity != null && entity.getId() != null) {
			if (entity.getTipo().getDescricao().equals("SINGLE FIELD")) {
				return new EditPerguntaSingleFieldPanel("panel", confirmationModal, entity, this, questionario, entity.getTipo(), enableDeleteLink);
				
			} else if (entity.getTipo().getDescricao().equals("MULTIPLE FIELD")) {
				return new EditPerguntaMultipleFieldPanel("panel", confirmationModal, entity, this, questionario, entity.getTipo(), enableDeleteLink);
				
			} else if (entity.getTipo().getDescricao().equals("SINGLE OPTION")) {
				return new EditPerguntaSingleOptionPanel("panel", confirmationModal, entity, this, questionario, entity.getTipo(), enableDeleteLink);
				
			} else {
				return new EditPerguntaMultipleOptionPanel("panel", confirmationModal, entity, this, questionario, entity.getTipo(), enableDeleteLink);
				
			}
			
		} else {

			final EditEscolherTipoPerguntaPanel tipoPerguntaPanel = new EditEscolherTipoPerguntaPanel("content");
			tipoPerguntaPanel.setOutputMarkupId(true);

			confirmationModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

				@Override
				public void onClose(AjaxRequestTarget target) {

					if (tipoPerguntaPanel.wasAvancarSelected()) {

						TipoPergunta tipoPerguntaSelecionada = tipoPerguntaPanel.getTipoPerguntaSelecionada();

						Panel cadastroPergunta = null;

						entity.setTipo(tipoPerguntaSelecionada);
						
						if (tipoPerguntaSelecionada.getDescricao().equals("SINGLE FIELD") ) {
							cadastroPergunta = new EditPerguntaSingleFieldPanel("panel", confirmationModal, entity, listPerguntaPanel , questionario, entity.getTipo(), enableDeleteLink);
							
						}else if (tipoPerguntaSelecionada.getDescricao().equals("MULTIPLE FIELD") ) {
							cadastroPergunta = new EditPerguntaMultipleFieldPanel("panel", confirmationModal, entity, listPerguntaPanel , questionario, entity.getTipo(), enableDeleteLink);
							
						}else if (tipoPerguntaSelecionada.getDescricao().equals("SINGLE OPTION") ) {
							cadastroPergunta = new EditPerguntaSingleOptionPanel("panel", confirmationModal, entity, listPerguntaPanel , questionario, entity.getTipo(), enableDeleteLink);
							
						}else {
							cadastroPergunta = new EditPerguntaMultipleOptionPanel("panel", confirmationModal, entity, listPerguntaPanel , questionario, entity.getTipo(), enableDeleteLink);
						}

						cadastroPergunta.setOutputMarkupId(true);
						editEntity(cadastroPergunta);
						target.addComponent(cadastroPergunta);

						tipoPerguntaPanel.setAvancarSelected(false);
					}
				}
			});

			confirmationModal.setInitialHeight(170);
			confirmationModal.setInitialWidth(350);
			confirmationModal.setContent(tipoPerguntaPanel);
			confirmationModal.show(target);

			return null;

		}
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("ordem", new FrwResourceModel( "pergunta.label.ordem")));
		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel( "pergunta.label.descricao")));
		columns.add(new EntityColumnInfo("subdescricao", new FrwResourceModel( "pergunta.label.subdescricao")));
		columns.add(new EntityColumnInfo("tipo", new FrwResourceModel( "pergunta.label.tipo")));

		return columns;
	}

	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportQuestionario();
	}

	@Override
	protected List<Pergunta> loadList() {
		if (filterDTO.getPerguntaDescricao() != null || filterDTO.getPerguntaSubDescricao() != null) {
			return perguntaFacade.buscarPerguntasPorQuestionarioFilter(questionario, filterDTO);
		}
		if(questionario != null && questionario.getId() != null){
			return perguntaFacade.buscarPerguntasPorQuestionario(questionario);
		}
		return perguntaFacade.buscarTodasPerguntas();
	}

	@Override
	protected void removeEntity(Pergunta entity, AjaxRequestTarget target) {
		SistemaSession.setUserAndCurrentDate(entity);
		perguntaFacade.excluirPergunta(entity);
	}

}
