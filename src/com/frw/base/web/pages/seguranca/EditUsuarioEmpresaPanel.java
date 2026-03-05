package com.frw.base.web.pages.seguranca;

import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Pesquisador;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.negocio.PesquisadorFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew.OnClickHandler;
import com.frw.base.web.pages.util.LookupSelectListLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Miller
 */
@SuppressWarnings("serial")
public class EditUsuarioEmpresaPanel extends AbstractEntityEditPanel<Usuario> {

    @EJB
    private CadastroFacade cadastroFacade;
    
    private CheckBox chkAtivo;
    
    private Empresa empresa;
    private LoadableDetachableModel<List<Pesquisador>> pesquisadores;
    private WebMarkupContainer pesquisadoresListContainer;

	@EJB
    private PesquisadorFacade pesquisadorFacade;
	
	private ListView<Pesquisador> pesquisadorView;
	private TextFieldFrw<String> txtCpf;
	private TextFieldFrw<String> txtLogin;
	private TextFieldFrw<String> txtNome;
	private TextFieldFrw<String> txtTipoUsuario;
	
    public EditUsuarioEmpresaPanel(String id, UpdatableModalWindow confirmationModal, Usuario usuario, Empresa empresa) {
        super(id, confirmationModal, usuario);
        returnButton.setVisible(true);
        this.empresa = empresa;
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Usuario> form, final Usuario usuario) {

    	// lookup usuario
    	final TextFieldFrw txtUsuario = new TextFieldFrw("usuario", new PropertyModel<String>(entity, "nome"));
		txtUsuario.setOutputMarkupId(true);
		txtUsuario.setEnabled(false);
		form.add(txtUsuario);
		
        final ListUsuariosPanel listUsuarioPanel = new ListUsuariosPanel(confirmationModal.getContentId(), confirmationModal,true);
        OnClickHandler onClickUsuario = new OnClickHandler() {
            @Override
            public void onClick(AjaxRequestTarget target) {
            	entity = listUsuarioPanel.getSelectedEntity();
            	
            	txtCpf.setModelObject(entity.getCpf());
            	txtLogin.setModelObject(entity.getLogin());
            	txtNome.setModelObject(entity.getNome());
            	
                target.add(txtUsuario);
                target.add(txtCpf);
                target.add(txtLogin);
                target.add(txtNome);
                target.add(txtTipoUsuario);
                ModalWindow.closeCurrent(target);
            }
        };
        listUsuarioPanel.setOnClickHandler(onClickUsuario);
        
        LookupSelectListLink selectUsuario = new LookupSelectListLink("selectUsuario", confirmationModal, 500, 900, "Pesquisa de Usuario", listUsuarioPanel, false);
        form.add(selectUsuario);
        
        AjaxLink limparUsuario = new AjaxLink("limparUsuario") {
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			entity = null;
    			
            	txtCpf.setModelObject(null);
            	txtLogin.setModelObject(null);
            	txtNome.setModelObject(null);
            	txtTipoUsuario.setModelObject(null);
            	
                target.add(txtUsuario);
                target.add(txtCpf);
                target.add(txtLogin);
                target.add(txtNome);
                target.add(txtTipoUsuario);
    		}
    	};
    	form.add(limparUsuario);
    	
    	txtNome = new TextFieldFrw<String>("nome");
        txtNome.setEnabled(false);
        
        txtCpf = new TextFieldFrw<String>("cpf");
        txtCpf.setEnabled(false);
        
        txtLogin = new TextFieldFrw<String>("login");
        txtLogin.setEnabled(false);
        
        chkAtivo = new CheckBox("ativo");
        
        txtTipoUsuario = new TextFieldFrw<String>("tipoUsuario.descricao");
        txtTipoUsuario.setEnabled(false);

        form.add(txtNome);
        form.add(txtCpf);
        form.add(txtLogin);
        form.add(txtTipoUsuario);
        
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
        return new EditUsuarioEmpresaPanel("panel", confirmationModal, usuario, empresa);
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
    
    @Override
    public Usuario saveEntity(Usuario usuario, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        entity.setEmpresa(empresa);
        return cadastroFacade.salvarUsuario(entity);
    }

    @Override
    protected void onAfterSaveEntity(AjaxRequestTarget target) {
    	feedback.setVisible(false);
    }

	@Override
	protected boolean validateEntity(Usuario usuario, AjaxRequestTarget target)
			throws SistemaException {
		if (entity.getPerfis() == null || entity.getPerfis().isEmpty()) {
			Perfil perfil = cadastroFacade.pesquisarPerfil(Perfil.ADM);
			usuario.getPerfis().add(perfil);
			cadastroFacade.salvarPerfil(perfil);
		}
		return true;
	}
	
    
}
