package com.frw.base.web.pages.seguranca;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.DefaultFocusBehavior;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Carlos Santos
 * @author juliano
 */
@SuppressWarnings("serial")
public class EditUsuarioPanel extends AbstractEntityEditPanel<Usuario> {

    @EJB
    private CadastroFacade cadastroFacade;
    
    @EJB
    private EmpresaFacade empresaFacade;
    
    public EditUsuarioPanel(String id, UpdatableModalWindow confirmationModal, Usuario usuario) {
        super(id, confirmationModal, usuario);
        returnButton.setVisible(true);
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Usuario> form, final Usuario usuario) {

        TextFieldFrw<String> txtNome = new TextFieldFrw<String>("nome");
        txtNome.setRequired(true);
        TextFieldFrw<String> txtCPF = new TextFieldFrw<String>("cpf");
        TextFieldFrw<String> txtLogin = new TextFieldFrw<String>("login");
        txtLogin.setRequired(true);
        PasswordTextField txtSenha = new PasswordTextField("senha").setResetPassword(false);
        
        CheckBox chkAtivo = new CheckBox("ativo");
        
        IChoiceRenderer<Empresa> iEmpresaChoice = new IChoiceRenderer<Empresa>() {
			@Override
			public Object getDisplayValue(Empresa empresa) {
				return empresa.getRazaoSocial();
			}
			
			@Override
			public String getIdValue(Empresa empresa, int arg1) {
				return empresa.getId().toString();
			}
		};
        final DropDownChoice<Empresa> ddcEmpresa = new DropDownChoice<Empresa>("empresa", empresaFacade.buscarTodas(), iEmpresaChoice);
        ddcEmpresa.setRequired(true);
        form.add(ddcEmpresa);
        
        form.add(txtNome.add(new DefaultFocusBehavior()));
        form.add(txtCPF);
        form.add(txtLogin);
        form.add(txtSenha);
        
        form.add(chkAtivo);
    }
    		
    @Override
    public Usuario deleteEntity(Usuario usuario, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(usuario);
        cadastroFacade.excluirUsuario(usuario);
        return usuario;
    }

    @Override
    public Panel getEditEntityPanel(Usuario usuario) {
        return new EditUsuarioPanel("panel", confirmationModal, usuario);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "usuario.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "usuario.message.salvar.sucesso";
    }

    @Override
    public Usuario newEntity(Usuario entity, AjaxRequestTarget target) throws SistemaException {
        final Usuario u = new Usuario();
        return u;
    }
    
/*    private Usuario salvarUsuario(Usuario usuario) throws SistemaException {
    	SistemaSession.setUserAndCurrentDate(usuario);
        return cadastroFacade.salvarUsuario(usuario);
	}*/

	@Override
    public Usuario saveEntity(Usuario usuario, AjaxRequestTarget target) throws SistemaException {
    	SistemaSession.setUserAndCurrentDate(usuario);
        usuario = cadastroFacade.salvarUsuario(usuario);
        setResponsePage(ListUsuarioPage.class);
        return usuario;
    }

    @Override
    protected void onAfterSaveEntity(AjaxRequestTarget target) {
    	feedback.setVisible(false);
    }

    @Override
	protected boolean validateEntity(Usuario usuario, AjaxRequestTarget target)
			throws SistemaException {
		if (entity.getPerfis() == null || entity.getPerfis().isEmpty()) {
			Perfil perfil = null;
			if (entity.getEmpresa() != null && entity.getEmpresa().getId().equals(Empresa.PELLI_SISTEMAS_KEY)) {
				perfil = cadastroFacade.pesquisarPerfil(Perfil.ADM);
			}else {
				perfil = cadastroFacade.pesquisarPerfil(Perfil.PESQUISADOR);
			}
			usuario.getPerfis().add(perfil);
			cadastroFacade.salvarPerfil(perfil);
		}
		
		
		return true;
	}
	
    
}
