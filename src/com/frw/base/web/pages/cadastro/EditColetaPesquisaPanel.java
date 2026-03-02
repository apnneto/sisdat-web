package com.frw.base.web.pages.cadastro;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew.OnClickHandler;
import com.frw.base.web.pages.util.LookupSelectListLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Leonardo Barros
 * 
 */
public class EditColetaPesquisaPanel extends AbstractEntityEditPanel<ColetaPesquisa> {

    
    @EJB
    private ColetaPesquisaFacade coletaPesquisaFacade;
    
    private byte[] imageArray;
    private WebMarkupContainer markupContainer;
    private Pergunta pergunta;
    private Pesquisa pesquisa;
    private TextFieldFrw txtPergunta;
    private TextFieldFrw<String> txtPesquisa;
    private TextFieldFrw<String> txtResposta;

    public EditColetaPesquisaPanel(String id, UpdatableModalWindow confirmationModal, ColetaPesquisa entity) {
        super(id, confirmationModal, entity);
        if(entity.getPesquisa() != null && entity.getPesquisa().getId() != null){
        	this.pesquisa = entity.getPesquisa();
        	entity.setPesquisa(pesquisa);
        }
    }

    @Override
    public void addFormFields(BaseWebBeanForm<ColetaPesquisa> form, final ColetaPesquisa entity) {

    	TextFieldFrw<String> txtId = new TextFieldFrw<String>("id");
    	txtId.setEnabled(false);
    	
    	TextArea<String> txtrCampoLivre = new TextArea<String>("campoLivre");
    	CheckBox chkExcluido = new CheckBox("excluido");
    	
    	// Lookup Pesquisa
    	txtPesquisa = new TextFieldFrw<String>("pesquisa.numero");
		txtPesquisa.setOutputMarkupId(true);
		txtPesquisa.setEnabled(false);
		form.add(txtPesquisa);
		
        final ListPesquisaPanel listPesquisaPanel = new ListPesquisaPanel(confirmationModal.getContentId(), confirmationModal,true);
        OnClickHandler onClickPesquisa = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity.setPesquisa(listPesquisaPanel.getSelectedEntity());
                target.addComponent(txtPesquisa);
                ModalWindow.closeCurrent(target);
            }
        };
        listPesquisaPanel.setOnClickHandler(onClickPesquisa);
        
        LookupSelectListLink selectPesquisa = new LookupSelectListLink("selectPesquisa", confirmationModal, 500, 900, "Pesquisa de Pesquisas", listPesquisaPanel, false);
        selectPesquisa.setVisible(false);
        form.add(selectPesquisa);
        
        AjaxLink limparPesquisa = new AjaxLink("limparPesquisa") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity.setPesquisa(null);
    			target.addComponent(txtPesquisa);
    		}
    	};
    	limparPesquisa.setVisible(false);
    	form.add(limparPesquisa);
    	
    	// Lookup Pergunta
    	txtPergunta = new TextFieldFrw("pergunta");
		txtPergunta.setOutputMarkupId(true);
		txtPergunta.setEnabled(false);
		form.add(txtPergunta);
		if(entity.getPergunta() != null && entity.getPergunta().getId() != null){
			this.pergunta = entity.getPergunta();
		}
		
        final ListPerguntaPanel listPerguntaPanel = new ListPerguntaPanel(confirmationModal.getContentId(), confirmationModal,true);
        OnClickHandler onClickPergunta = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity.setPergunta(listPerguntaPanel.getSelectedEntity());
            	pergunta = entity.getPergunta();
                target.addComponent(txtPergunta);
                ModalWindow.closeCurrent(target);
            }
        };
        listPerguntaPanel.setOnClickHandler(onClickPergunta);
        
        LookupSelectListLink selectPergunta = new LookupSelectListLink("selectPergunta", confirmationModal, 500, 900, "Pesquisa de Pergunta", listPerguntaPanel, false);
        form.add(selectPergunta);
        
        AjaxLink limparPergunta = new AjaxLink("limparPergunta") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity.setPergunta(null);
    			target.addComponent(txtPergunta);
    		}
    	};
    	form.add(limparPergunta);
    	
    	
    	// Lookup Resposta
    	txtResposta = new TextFieldFrw<String>("resposta.descricao");
		txtResposta.setOutputMarkupId(true);
		txtResposta.setEnabled(false);
		form.add(txtResposta);
		
        final ListRespostaPanel listRespostaPanel = new ListRespostaPanel(confirmationModal.getContentId(), confirmationModal,true, this.pergunta);
        OnClickHandler onClickResposta = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity.setResposta(listRespostaPanel.getSelectedEntity());
                target.addComponent(txtResposta);
                ModalWindow.closeCurrent(target);
            }
        };
        listRespostaPanel.setOnClickHandler(onClickResposta);
        
        LookupSelectListLink selectResposta = new LookupSelectListLink("selectResposta", confirmationModal, 500, 900, "Pesquisa de Resposta", listRespostaPanel, false);
        form.add(selectResposta);
        
        AjaxLink limparResposta = new AjaxLink("limparResposta") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity.setResposta(null);
    			target.addComponent(txtResposta);
    		}
    	};
    	form.add(limparResposta);
    	
        form.add(txtId);
        form.add(txtrCampoLivre);
        form.add(chkExcluido);
        form.add(createImagem());
    
    }

	@Override
    public ColetaPesquisa deleteEntity(ColetaPesquisa entity, AjaxRequestTarget target) throws SistemaException {
    	 coletaPesquisaFacade.excluirColetaPesquisa(entity);
        return entity;
    }

	@Override
    public Panel getEditEntityPanel(ColetaPesquisa entity) {
        return new EditColetaPesquisaPanel("panel", confirmationModal, entity);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "coleta.pesquisa.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "coleta.pesquisa.message.salvar.sucesso";
    }

    @Override
    public ColetaPesquisa newEntity(ColetaPesquisa entity, AjaxRequestTarget target) throws SistemaException {
        final ColetaPesquisa pojo = new ColetaPesquisa();
        pojo.setPesquisa(pesquisa);
        return pojo;
    }

    @Override
    public ColetaPesquisa saveEntity(ColetaPesquisa entity, AjaxRequestTarget target) throws SistemaException {
    	 return coletaPesquisaFacade.salvarColetaPesquisa(entity);
    }

    private WebMarkupContainer createImagem() {
		markupContainer = new WebMarkupContainer("fotoContainer");
		markupContainer.setOutputMarkupId(true);
		imageArray = new byte[1];
		markupContainer.setVisible(false);

		DynamicImageResource imageResource = new DynamicImageResource() {
			@Override
			protected byte[] getImageData() {
				if (imageArray == null) {
					return new byte[1];
				}
				return imageArray;
			}
		};

        NonCachingImage image = new NonCachingImage("image", imageResource);
        image.setOutputMarkupId(true);
        markupContainer.add(image);
        return markupContainer;
	}
    
    @Override
    protected boolean validateEntity(ColetaPesquisa entity, AjaxRequestTarget target) throws SistemaException {
    	Boolean validate = true;
    	
    	if(txtPergunta.getModelObject() == null){
    		validate = false;
    		feedback.getString("campo.pergunta.obrigatorio");
    		feedback.setVisible(true);
    		target.addComponent(feedback);
    	}
    	if(txtResposta.getModelObject() == null){
    		validate = false;
    		feedback.getString("campo.resposta.obrigatorio");
    		feedback.setVisible(true);
    		target.addComponent(feedback);
    	}
    	if(txtPesquisa.getModelObject() == null){
    		validate = false;
    		feedback.getString("campo.pesquisa.obrigatorio");
    		feedback.setVisible(true);
    		target.addComponent(feedback);
    	}
    	
    	return validate;
    	
    	
    }
}
