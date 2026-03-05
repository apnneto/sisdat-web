package com.frw.base.web.pages.cadastro;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Dispositivo;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.DispositivoFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew.OnClickHandler;
import com.frw.base.web.pages.seguranca.ListUsuariosPanel;
import com.frw.base.web.pages.util.LookupSelectListLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Leonardo Barros
 * 
 */
public class EditDispositivoPanel extends AbstractEntityEditPanel<Dispositivo> {

    
    @EJB
    private DispositivoFacade dispositivoFacade;
    

    public EditDispositivoPanel(String id, UpdatableModalWindow confirmationModal, Dispositivo entity) {
        super(id, confirmationModal, entity);
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Dispositivo> form, final Dispositivo entity) {

    	TextFieldFrw txtId = new TextFieldFrw("id");
    	TextFieldFrw txtDevice = new TextFieldFrw("device");
        
    	// lookup usuario
    	final TextFieldFrw txtUsuario = new TextFieldFrw("usuario");
		txtUsuario.setOutputMarkupId(true);
		txtUsuario.setEnabled(false);
		form.add(txtUsuario);
		
        final ListUsuariosPanel listUsuarioPanel = new ListUsuariosPanel(confirmationModal.getContentId(), confirmationModal,true);
        OnClickHandler onClickUsuario = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity.setUsuario(listUsuarioPanel.getSelectedEntity());
                target.addComponent(txtUsuario);
                ModalWindow.closeCurrent(target);
            }
        };
        listUsuarioPanel.setOnClickHandler(onClickUsuario);
        
        LookupSelectListLink selectUsuario = new LookupSelectListLink("selectUsuario", confirmationModal, 500, 900, "Pesquisa de Usuario", listUsuarioPanel, false);
        form.add(selectUsuario);
        
        AjaxLink limparUsuario = new AjaxLink("limparUsuario") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity.setUsuario(null);
    			target.addComponent(txtUsuario);
    		}
    	};
    	form.add(limparUsuario);
        
        CheckBox chkExcluido = new CheckBox("excluido");
        
        
        txtId.setEnabled(false);
        
        form.add(txtId);
        form.add(txtDevice);
        
        form.add(chkExcluido);
    
    }

    @Override
    public Dispositivo deleteEntity(Dispositivo entity, AjaxRequestTarget target) throws SistemaException {
    	 dispositivoFacade.excluirDispositivo(entity);
        return entity;
    }

    @Override
    public Panel getEditEntityPanel(Dispositivo entity) {
        return new EditDispositivoPanel("panel", confirmationModal, entity);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "dispositivo.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "dispositivo.message.salvar.sucesso";
    }

    @Override
    public Dispositivo newEntity(Dispositivo entity, AjaxRequestTarget target) throws SistemaException {
        final Dispositivo pojo = new Dispositivo();
        return pojo;
    }

    @Override
    public Dispositivo saveEntity(Dispositivo entity, AjaxRequestTarget target) throws SistemaException {
    	 return dispositivoFacade.salvarDispositivo(entity);
    }
}
