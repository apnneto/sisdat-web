/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.cadastro;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.dominio.sisdat.TipoPergunta;
import com.frw.base.negocio.quiz.TipoPerguntaFacade;
import com.frw.base.web.util.AjaxButtonFrw;

/**
 *
 * @author Malves
 */
@SuppressWarnings("serial")
public class EditEscolherTipoPerguntaPanel extends Panel {

    private boolean avancar = false;
	private FeedbackPanel feedback;
    private Form form;
    
    @EJB
    private TipoPerguntaFacade tipoPerguntaFacade;
    
    private IModel<TipoPergunta> tipoPerguntaSelecionada;

    public EditEscolherTipoPerguntaPanel(String id) {
        super(id);

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);


        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        form = new Form("form");
        
        tipoPerguntaSelecionada = new Model<TipoPergunta>();
        DropDownChoice<TipoPergunta> cmbTipoPergunta = new DropDownChoice<TipoPergunta>("cmbTipoPergunta", tipoPerguntaSelecionada, tipoPerguntaFacade.buscarTodosTipoPerguntas());
        cmbTipoPergunta.setLabel(new ResourceModel("titulo.pergunta.tipo"));
        cmbTipoPergunta.setRequired(true);
        form.add(cmbTipoPergunta);

        AjaxButtonFrw btnSalvar = new AjaxButtonFrw("btnAvancar") {
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				avancar = Boolean.FALSE;
				super.onError(target, form);
				target.addComponent(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget art, Form<?> form) {
				
				avancar = Boolean.TRUE;
				
				ModalWindow.closeCurrent(art);
			}
		};
		btnSalvar.setModel(new ResourceModel("botao.avancar"));
		form.add(btnSalvar);

        AjaxButtonFrw btnCancelar = new AjaxButtonFrw("btnCancelar") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget art, Form<?> form) {
				avancar = Boolean.FALSE;
				ModalWindow.closeCurrent(art);
			}
		};
		btnCancelar.setModel(new ResourceModel("botao.cancelar"));
		btnCancelar.setDefaultFormProcessing(false);
		form.add(btnCancelar);		
        
        add(form);

    }
    
	public TipoPergunta getTipoPerguntaSelecionada() {
		return tipoPerguntaSelecionada.getObject();
	}
    
	public void setAvancarSelected(boolean avancar){
		this.avancar = avancar;
	}
	
	public boolean wasAvancarSelected() {
		return avancar;
	}
    
}
