package com.frw.base.web.pages.cadastro;

import java.util.Date;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.exception.SistemaException;
import com.frw.base.util.enumeration.FormatoDataEnum;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.DateTextFieldCustom;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Igor Pessanha
 *
 */
public class EditPerguntaRespostaPesquisaPanel extends AbstractEntityEditPanel<Pesquisa> {

	private static final long serialVersionUID = 1L;
	@EJB
    private ColetaPesquisa coletaPesquisa;
        

    public EditPerguntaRespostaPesquisaPanel(String id, UpdatableModalWindow confirmationModal, Pesquisa entity) {
        super(id, confirmationModal, entity);
        container.setVisible(false);
    }

	@Override
    public void addFormFields(BaseWebBeanForm<Pesquisa> form, final Pesquisa entity) {

    	TextFieldFrw txtId = new TextFieldFrw("id");
    	TextFieldFrw txtQuestionario = new TextFieldFrw("questionario");
    	TextFieldFrw txtUsuario = new TextFieldFrw("usuario");
    	DateTextFieldCustom txtDataAbertura = new DateTextFieldCustom("dataAbertura", new PropertyModel<Date>(entity, "dataAbertura"),FormatoDataEnum.DATA_HORA_MINUTO.toString());
    	DateTextFieldCustom txtDataFechamento = new DateTextFieldCustom("dataFechamento", new PropertyModel<Date>(entity, "dataFechamento"), FormatoDataEnum.DATA_HORA_MINUTO.toString());
    	DateTextFieldCustom txtDataSincronizacao = new DateTextFieldCustom("dataSincronizacao", new PropertyModel<Date>(entity, "dataSincronizacao"), FormatoDataEnum.DATA_HORA_MINUTO.toString());
    	
        txtId.setEnabled(false);
        txtQuestionario.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtDataAbertura.setEnabled(false);
        txtDataFechamento.setEnabled(false);
        txtDataSincronizacao.setEnabled(false);
        
        form.add(txtId);
        form.add(txtQuestionario);
        form.add(txtUsuario);
        form.add(txtDataAbertura);
        form.add(txtDataFechamento);
        form.add(txtDataSincronizacao);       
        
        ListPerguntaRespostaPesquisaPanel listPerguntaResposta = new ListPerguntaRespostaPesquisaPanel("listPerguntaResposta", confirmationModal, entity);
        listPerguntaResposta.setOutputMarkupId(true);
        form.add(listPerguntaResposta);
    
    }

	@Override
	protected Pesquisa deleteEntity(Pesquisa entity, AjaxRequestTarget target)
			throws SistemaException {
		return null;
	}

	@Override
	protected Panel getEditEntityPanel(Pesquisa entity) throws Exception {
		return null;
	}

	@Override
	protected String getEntityDeleteSuccessMessage() {
		return null;
	}

	@Override
	protected String getEntitySaveSuccessMessage() {
		return null;
	}

	@Override
	protected Pesquisa newEntity(Pesquisa entity, AjaxRequestTarget target)
			throws SistemaException {
		return null;
	}

	@Override
	protected Pesquisa saveEntity(Pesquisa entity, AjaxRequestTarget target)
			throws SistemaException {
		return null;
	}

}
