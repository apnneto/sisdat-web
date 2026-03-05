package com.frw.base.web.pages.seguranca;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.StringValidator;
import org.json.JSONException;
import org.json.JSONObject;

import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.negocio.siclo.BuscaCepWS;
import com.frw.base.validation.EmailValidator;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AjaxAbasPanel;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Miller
 */
@SuppressWarnings("serial")
public class EditEmpresaPanel extends AbstractEntityEditPanel<Empresa> {

	private AjaxAbasPanel abas;
	@EJB
	private EmpresaFacade empresaFacade;
	private TextFieldFrw<String> txtBairro;
	private TextFieldFrw<String> txtCep;
	private TextFieldFrw<String> txtCidade;
	private TextFieldFrw<String> txtComplemento;
	private TextFieldFrw<String> txtEmail;
	private TextFieldFrw<String> txtLogradouro;
	private TextFieldFrw<String> txtNumero;
	private TextFieldFrw<String> txtSiglaEstado;

	public EditEmpresaPanel(String id, UpdatableModalWindow confirmationModal, Empresa entity, AjaxAbasPanel abas) {
		super(id, confirmationModal, entity);
		this.abas = abas;
	}

	@Override
	public void addFormFields(BaseWebBeanForm<Empresa> form, final Empresa entity) {

		TextFieldFrw<String> txtTelefone = new TextFieldFrw<String>("telefone");
		txtTelefone.add(StringValidator.maximumLength(20));
		txtTelefone.setRequired(true);

		txtEmail = new TextFieldFrw<String>("email");
		txtEmail.add(StringValidator.maximumLength(50));
		txtEmail.setRequired(true);

		txtLogradouro = new TextFieldFrw<String>("logradouro");
		txtLogradouro.add(StringValidator.maximumLength(100));
		txtLogradouro.setRequired(true);

		txtNumero = new TextFieldFrw<String>("numero");
		txtNumero.add(StringValidator.maximumLength(100));
		txtNumero.setRequired(true);

		txtComplemento = new TextFieldFrw<String>("complemento");
		txtComplemento.add(StringValidator.maximumLength(50));

		txtCep = new TextFieldFrw<String>("cep");
		txtCep.setRequired(true);
		txtCep.add(new AjaxFormComponentUpdatingBehavior("onblur") {

	          @Override
	          protected void onError(AjaxRequestTarget target, RuntimeException e) {
	          	target.add(feedback);
	          }
	         
			@Override
	          protected void onUpdate(AjaxRequestTarget art) {
	          	
				String fieldValue = txtCep.getValue();
				if (fieldValue != null) {
					fieldValue = fieldValue.replace("-", "");
					fieldValue = fieldValue.replace("_", "");
				}
				if (fieldValue.length() >= 8) {
					
					JSONObject jsn = BuscaCepWS.getEnderecoCEP(txtCep.getModelObject());
					
					try {
						if(jsn.has("resultado") && jsn.getString("resultado").equals("1")){
							entity.setSiglaEstado(jsn.getString("uf"));
							entity.setCidade(jsn.getString("cidade"));
							entity.setBairro(jsn.getString("bairro"));
							entity.setLogradouro(jsn.getString("tipo_logradouro") + " " +jsn.getString("logradouro"));
							art.focusComponent(txtNumero);
							feedback.setVisible(false);
							art.add(feedback);
						
						}else{
							error(getString("cep.invalido"));
							feedback.setVisible(true);
							entity.setLogradouro(null);
							entity.setBairro(null);
							entity.setSiglaEstado(null);
							entity.setCidade(null);
							entity.setNumero(null);
							art.add(feedback);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					feedback.setVisible(false);
					entity.setLogradouro(null);
					entity.setBairro(null);
					entity.setSiglaEstado(null);
					entity.setCidade(null);
					entity.setNumero(null);
					art.add(feedback);
				}
				
				art.add(txtSiglaEstado);
				art.add(txtCidade);
				art.add(txtBairro);
				art.add(txtLogradouro);
				art.add(txtNumero);
	          }
	      });

		txtBairro = new TextFieldFrw<String>("bairro");
		txtBairro.add(StringValidator.maximumLength(50));
		txtBairro.setRequired(true);

		txtCidade = new TextFieldFrw<String>("cidade");
		txtCidade.add(StringValidator.maximumLength(50));
		txtCidade.setRequired(true);

		txtSiglaEstado = new TextFieldFrw<String>("siglaEstado");
		txtSiglaEstado.add(StringValidator.maximumLength(2));
		txtSiglaEstado.setRequired(true);

		form.add(new TextFieldFrw<String>("razaoSocial").add(StringValidator.maximumLength(100)));
		form.add(new TextFieldFrw<String>("nomeFantasia").add(StringValidator.maximumLength(100)));
		form.add(new TextFieldFrw<String>("cnpj").add(StringValidator.maximumLength(20)));
		form.add(new TextFieldFrw<String>("crea").add(StringValidator.maximumLength(20)));
		
		form.add(txtTelefone);
		form.add(txtEmail);
		form.add(txtNumero);
		form.add(txtComplemento);
		form.add(txtLogradouro);
		form.add(txtBairro);
		form.add(txtCep);
		form.add(txtCidade);
		form.add(txtSiglaEstado);

	}

	@Override
	public Empresa deleteEntity(Empresa entity, AjaxRequestTarget target) throws SistemaException {
		SistemaSession.setUserAndCurrentDate(entity);
		empresaFacade.deleteEmpresa(entity);
		return entity;
	}

	@Override
	public Panel getEditEntityPanel(Empresa entity) {
		return new EditEmpresaPanel("panel", confirmationModal, entity, abas);
	}

	@Override
	public String getEntityDeleteSuccessMessage() {
		return "empresa.message.excluir.sucesso";
	}

	@Override
	public String getEntitySaveSuccessMessage() {
		return "empresa.message.salvar.sucesso";
	}

	@Override
	public Empresa newEntity(Empresa entity, AjaxRequestTarget target) throws SistemaException {
		final Empresa c = new Empresa();
		return c;
	}
	
	@Override
	public Empresa saveEntity(Empresa entity, AjaxRequestTarget target) throws SistemaException {
		SistemaSession.setUserAndCurrentDate(entity);
		return empresaFacade.salvarEmpresa(entity);
	}

	private boolean validaEmail(String email) {
		EmailValidator validaEmail = new EmailValidator();
		return validaEmail.validaEmail(email);
		
	}
	@Override
	protected void onAfterSaveEntity(AjaxRequestTarget target) {
		target.add(abas);
	}
	@Override
	protected boolean validateEntity(Empresa entity, AjaxRequestTarget target)	throws SistemaException {
        if ((txtEmail.getModelObject()!= null)&&(!validaEmail(txtEmail.getModelObject()))){
        	error("Email inválido.");
        	feedback.setVisible(true);
        	target.add(feedback);
            return false;
        }
		return true;
	}
}
