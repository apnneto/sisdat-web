package com.frw.base.web.pages.cadastro;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AjaxAbasPanel;
import com.frw.base.web.util.TextFieldFrw;


@SuppressWarnings("serial")
public class EditQuestionarioPanel extends AbstractEntityEditPanel<Questionario> {

    private AjaxAbasPanel abas;
	private boolean hasPermissionDelete;
	private String id;
	@EJB
    private QuestionarioFacade questionarioFacade;
	protected UpdatableModalWindow confirmationModal;
        

    public EditQuestionarioPanel(String id, UpdatableModalWindow confirmationModal, Questionario entity, AjaxAbasPanel abas, boolean hasPermissionDelete) {
        super(id, confirmationModal, entity);
        this.id = id;
        this.confirmationModal = confirmationModal;
        this.abas = abas;
        this.hasPermissionDelete = hasPermissionDelete;
        feedback.setOutputMarkupId(true);
		feedback.setOutputMarkupPlaceholderTag(true);
		returnButton.setVisible(true);
		
		deleteButton.setVisible(hasPermissionDelete);
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Questionario> form, final Questionario entity) {

    	TextFieldFrw<String> txtDescricao = new TextFieldFrw<String>("descricao");
      	txtDescricao.setRequired(true);
        TextFieldFrw<String> txtId = new TextFieldFrw<String>("id");
    	TextArea<String> txtrResumo = new TextArea<String>("resumo");
    	TextFieldFrw<String> txtOrdem = new TextFieldFrw<String>("ordem");
    	TextFieldFrw<String> txtCodigo = new TextFieldFrw<String>("codigo");
    	TextFieldFrw<String> txtVersao = new TextFieldFrw<String>("versao");
    	TextFieldFrw<String> txtOrientacao = new TextFieldFrw<String>("orientacao");
    	
    	CheckBox chkFoto = new CheckBox("foto");
    	CheckBox chkEditavel = new CheckBox("editavel");
    	CheckBox chkCapturaCordenada = new CheckBox("capturaCordenada");
        txtId.setEnabled(false);
        
        form.add(txtId);
        form.add(txtDescricao);
        form.add(txtrResumo);
        form.add(txtOrdem);
        form.add(txtCodigo);
        form.add(txtVersao);
        form.add(txtOrientacao);
        form.add(chkFoto);
        form.add(chkEditavel);
        form.add(chkCapturaCordenada);

    }

    @Override
    public Questionario deleteEntity(Questionario entity, AjaxRequestTarget target) throws SistemaException {
    	questionarioFacade.excluirQuestionario(entity);
        return entity;
    }

    @Override
    public Panel getEditEntityPanel(Questionario entity) {
    	return new EditQuestionarioPanel(id, confirmationModal, entity, abas, hasPermissionDelete);
    }
    
    @Override
    public String getEntityDeleteSuccessMessage() {
        return "questionario.message.excluir.sucesso";
    }

    public String getEntityPublicSuccessMessage() {
        return "questionario.message.publicar.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "questionario.message.salvar.sucesso";
    }
    
    
    public Questionario getQuestionario() {
		return entity;
	}
    
	@Override
    public Questionario newEntity(Questionario entity, AjaxRequestTarget target) throws SistemaException {
        final Questionario pojo = new Questionario();
        target.add(abas);
        
        return pojo;
    }
	

	@Override
    public Questionario saveEntity(Questionario entity, AjaxRequestTarget target) throws SistemaException {
    	 SistemaSession.setUserAndCurrentDate(entity);
    	 Questionario questionario = questionarioFacade.salvarQuestionario(entity);
		 target.add(abas);
    	 return questionario;
    }

	@Override
	protected void onAfterDeletEntity(AjaxRequestTarget target) {
		abas.setVisible(false);
	}
    
}
