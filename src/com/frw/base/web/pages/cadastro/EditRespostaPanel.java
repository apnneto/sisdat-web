package com.frw.base.web.pages.cadastro;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Resposta;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.RespostaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew.OnClickHandler;
import com.frw.base.web.pages.util.LookupSelectListLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextFieldFrw;

public class EditRespostaPanel extends AbstractEntityEditPanel<Resposta> {
	
	private static final long serialVersionUID = 1L;

	private ListRespostaPanel panelList;

    @EJB
    private RespostaFacade respostaFacade;
        

    public EditRespostaPanel(String id, UpdatableModalWindow confirmationModal, Resposta entity, ListRespostaPanel panelList) {
        super(id, confirmationModal, entity);
        this.panelList = panelList;
       
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Resposta> form, final Resposta entity) {

    	TextFieldFrw txtId = new TextFieldFrw("id");
    	TextFieldFrw txtDescricao = new TextFieldFrw("descricao");
    	TextFieldFrw txtOrdem = new TextFieldFrw("ordem");
    	TextFieldFrw txtValor = new TextFieldFrw("valor");
    	
    	
    	
    	// lookup pergunta
    	final TextFieldFrw txtPergunta = new TextFieldFrw("pergunta");
		txtPergunta.setOutputMarkupId(true);
		txtPergunta.setEnabled(false);
		form.add(txtPergunta);
		
        final ListPerguntaPanel listPerguntaPanel = new ListPerguntaPanel(confirmationModal.getContentId(), confirmationModal,true);
        OnClickHandler onClickPergunta = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity.setPergunta(listPerguntaPanel.getSelectedEntity());
                target.add(txtPergunta);
                UpdatableModalWindow.closeCurrent(target);
            }
        };
        listPerguntaPanel.setOnClickHandler(onClickPergunta);
        
        LookupSelectListLink selectPergunta = new LookupSelectListLink("selectPergunta", confirmationModal, 500, 900, "Pesquisa de Pergunta", listPerguntaPanel, false);
        form.add(selectPergunta);
        
        AjaxLink limparPergunta = new AjaxLink("limparPergunta") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity.setPergunta(null);
    			target.add(txtPergunta);
    		}
    	};
    	form.add(limparPergunta);
    			
    	CheckBox chkCorreta = new CheckBox("correta");
        
        txtId.setEnabled(false);
        txtDescricao.setRequired(true);
    	txtOrdem.setRequired(true);
    	txtValor.setRequired(true);
        
        form.add(txtId);
        form.add(txtDescricao);
        form.add(txtOrdem);
        form.add(txtValor);
        
        form.add(chkCorreta);
    
    }

    @Override
    public Resposta deleteEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
    	 SistemaSession.setUserAndCurrentDate(entity);
    	 respostaFacade.excluirResposta(entity);
        return entity;
    }

    @Override
    public Panel getEditEntityPanel(Resposta entity) {
        return new EditRespostaPanel("panel", confirmationModal, entity, panelList);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "resposta.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "resposta.message.salvar.sucesso";
    }

    @Override
    public Resposta newEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
        final Resposta pojo = new Resposta();
        return pojo;
    }

    @Override
    public Resposta saveEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
    	 SistemaSession.setUserAndCurrentDate(entity);
    	 return respostaFacade.salvarResposta(entity);
    }


}
