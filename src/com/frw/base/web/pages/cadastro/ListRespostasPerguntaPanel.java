package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Resposta;
import com.frw.base.dominio.sisdat.TipoDado;
import com.frw.base.dominio.sisdat.TipoPergunta;
import com.frw.base.negocio.quiz.RespostaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 * @author Igor Pessanha
 * 
 */
public class ListRespostasPerguntaPanel extends
		AbstractEntityListPanelNew<Resposta> {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private Pergunta pergunta;
	@EJB
	private RespostaFacade respostaFacade;
	private TipoDado tipoDado;
	private TipoPergunta tipoPergunta;

	public ListRespostasPerguntaPanel(String id, final UpdatableModalWindow confirmationModal, Pergunta pergunta, TipoPergunta tipoPergunta, TipoDado tipoDado, boolean hasPermissionDelete ) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		this.id = id;
		this.pergunta = pergunta;
		this.tipoDado = tipoDado;
		if (pergunta.getId() == null){
			newLink.setVisible(false);
		}
		enableDeleteLink = hasPermissionDelete;
		
	}

	@Override
	protected void addTableItems(Resposta pojo, EntityLabelMap componentMap) {
		componentMap.add("ordem",new LabelFrw("label", String.valueOf(pojo.getOrdem())));
		componentMap.add("descricao",new LabelFrw("label", pojo.getDescricao()));
	}

	@Override
	protected Panel getEditPanel(Resposta entity, AjaxRequestTarget target) {
		return new EditRespostaPerguntaPanel(id, confirmationModal, entity, pergunta, this);
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("ordem", new FrwResourceModel("resposta.label.ordem")));
		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel("resposta.label.descricao")));

		return columns;
	}

	protected AjaxLink getNewLinkButton() {
		return newLink;
	}

	@Override
	protected List<Resposta> loadList() {
		if (pergunta.getId() != null) {
			newLink.setVisible(true);
		}else {
			newLink.setVisible(false);
		}
		return respostaFacade.buscarRespostasPorPergunta(pergunta);
	}

	@Override
	protected void removeEntity(Resposta entity, AjaxRequestTarget target) {
		SistemaSession.setUserAndCurrentDate(entity);
		respostaFacade.excluirResposta(entity);
	}


}
