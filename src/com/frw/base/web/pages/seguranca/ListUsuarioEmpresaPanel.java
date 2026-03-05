package com.frw.base.web.pages.seguranca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.seguranca.filter.FilterListUsuarioPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.comparator.UsuarioComparator;
import com.frw.manutencao.dominio.dto.FilterUsuarioDTO;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportUsuario;

/**
 *
 * @author juliano
 */
public class ListUsuarioEmpresaPanel extends AbstractEntityListPanelNew<Usuario>{

	private static final long serialVersionUID = 1L;

	@EJB
    private CadastroFacade cadastroFacade;

    private Empresa empresa;

	private FilterUsuarioDTO filterDTO;

	private Usuario usuario;

    public ListUsuarioEmpresaPanel(String id, final UpdatableModalWindow confirmationModal, Empresa empresa) {
        super(id, confirmationModal);
        setOutputMarkupId(true);
        this.empresa = empresa;

        init();
    }

    public ListUsuarioEmpresaPanel(String id,
			UpdatableModalWindow confirmationModal, Usuario usuario) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		
		this.usuario = usuario;
		init();
	}

	@Override
	public boolean isContentExportableToExcel() {
		return true;
	}

	private void init() {
        FilterListUsuarioPanel filterPanel = new FilterListUsuarioPanel("searchFilters", listContainer, filterDTO, cadastroFacade);
        filtersContainer.replaceWith(filterPanel);
	}

    @Override
	protected void addTableItems(Usuario entity, EntityLabelMap componentMap) {
		componentMap.add("nome", new LabelFrw("label", entity.getNome()));
		componentMap.add("login", new LabelFrw("label", entity.getLogin()));
		componentMap.add("tipoUsuario", new LabelFrw("label", entity.getTipoUsuario() != null ? entity.getTipoUsuario().getDescricao() : ""));
	}

    @Override
    protected void beforeLoadPage() {
        filterDTO = new FilterUsuarioDTO();
    }


    @Override
    protected Panel getEditPanel(Usuario entity, AjaxRequestTarget target) {
        return new EditUsuarioEmpresaPanel("panel", confirmationModal, entity, empresa);
    }

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();
		
		columns.add(new EntityColumnInfo("nome", new FrwResourceModel("usuario.label.nome")));
		columns.add(new EntityColumnInfo("login", new FrwResourceModel("usuario.label.login")));
		columns.add(new EntityColumnInfo("tipoUsuario", new FrwResourceModel("usuario.label.tipo.usuario")));
		
		return columns;
	}

	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportUsuario();
	}
	
	@Override
    protected List<Usuario> loadList() {
        /*Usuario usuarioLogado = ((SistemaSession) Session.get()).getUsuarioLogado();
        filterDTO.setTipoUsuarioLogado(usuarioLogado.getTipoUsuario());*/
        
        filterDTO.setEmpresa(empresa);
        
        if (usuario != null) {
        	filterDTO.setNome(usuario.getNome());
        	filterDTO.setLogin(usuario.getLogin());
		}
        
        List<Usuario> usuarios = cadastroFacade.pesquisaTodosUsuarios(filterDTO);
        Collections.sort(usuarios, new UsuarioComparator());
        return usuarios;
    }

	@Override
    protected void removeEntity(Usuario usuario, AjaxRequestTarget target) {
    	cadastroFacade.excluirUsuario(usuario);
    }

}
