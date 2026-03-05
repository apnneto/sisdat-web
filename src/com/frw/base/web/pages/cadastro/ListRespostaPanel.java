package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Resposta;
import com.frw.base.negocio.quiz.RespostaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportResposta;

public class ListRespostaPanel extends AbstractEntityListPanelNew<Resposta> {

	private static final long serialVersionUID = 1L;

	private String id;
	private Pergunta pergunta;
	@EJB
	private RespostaFacade respostaFacade;

	public ListRespostaPanel(String id, final UpdatableModalWindow confirmationModal) {
		super(id, confirmationModal);
		this.id = id;
		setOutputMarkupId(true);
	}
	
	public ListRespostaPanel(String id, final UpdatableModalWindow confirmationModal, boolean selectedPanel) {
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

	public ListRespostaPanel(String id, final UpdatableModalWindow confirmationModal, boolean selectedPanel, Pergunta pergunta) {
		super(id, confirmationModal);
		setOutputMarkupId(true);

		if (selectedPanel == true) {
			enableDeleteLink = false;
			enableEditLink = false;
			enableSelectLink = true;
			newLink.setVisible(false);
		}
		this.pergunta = pergunta;

	}
	
	public ListRespostaPanel(String id, final UpdatableModalWindow confirmationModal, Pergunta pergunta) {
		super(id, confirmationModal);
		this.id = id;
		this.pergunta = pergunta;
		setOutputMarkupId(true);
	}

	@Override
	public boolean isContentExportableToExcel() {
		return true;
	}

	@Override
	protected void addTableItems(Resposta pojo, EntityLabelMap componentMap) {
		componentMap.add("id", new LabelFrw("label", String.valueOf(pojo.getId())));
		componentMap.add("descricao", new LabelFrw("label", pojo.getDescricao()));
		componentMap.add("pergunta", new LabelFrw("label", pojo.getPergunta().getDescricao()));
		componentMap.add("ordem", new LabelFrw("label", String.valueOf(pojo.getOrdem())));
		
	}

	@Override
	protected Panel getEditPanel(Resposta entity, AjaxRequestTarget target) {
		return new EditRespostaPanel(id, confirmationModal,entity, this);
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("id", new FrwResourceModel( "resposta.label.id")));
		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel( "resposta.label.descricao")));
		columns.add(new EntityColumnInfo("pergunta", new FrwResourceModel( "resposta.label.pergunta")));
		columns.add(new EntityColumnInfo("ordem", new FrwResourceModel( "resposta.label.ordem")));

		return columns;
	}

	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportResposta();
	}

	@Override
	protected List<Resposta> loadList() {
		if(pergunta != null && pergunta.getId() != null){
			return respostaFacade.buscarRespostasPorPergunta(pergunta);
		}
		return new ArrayList<Resposta>();
	}

	@Override
	protected void removeEntity(Resposta entity, AjaxRequestTarget target) {
		SistemaSession.setUserAndCurrentDate(entity);
		respostaFacade.excluirResposta(entity);
	}

}
