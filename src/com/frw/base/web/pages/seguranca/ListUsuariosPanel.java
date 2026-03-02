package com.frw.base.web.pages.seguranca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.util.SistemaUtil;
import com.frw.base.web.SistemaSession;
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
public class ListUsuariosPanel extends AbstractEntityListPanelNew<Usuario>{

    @EJB
    private CadastroFacade cadastroFacade;

    private FilterUsuarioDTO filterDTO;

    public ListUsuariosPanel(String id, final UpdatableModalWindow confirmationModal) {
        super(id, confirmationModal);
        setOutputMarkupId(true);

        /* cria filtro de pesquisa */
        FilterListUsuarioPanel filterPanel = new FilterListUsuarioPanel("searchFilters", listContainer, filterDTO, cadastroFacade);
        filtersContainer.replaceWith(filterPanel);
    }
    
    public ListUsuariosPanel(String id, final UpdatableModalWindow confirmationModal, boolean selectedPanel) {
        super(id, confirmationModal);
        setOutputMarkupId(true);

        if(selectedPanel == true)
        {
        	enableDeleteLink = false;
        	enableEditLink = false;
        	enableSelectLink = true;
        	newLink.setVisible(false);
        }
        
    }

    @Override
	public boolean isContentExportableToExcel() {
		return true;
	}

    private String getPerfisString(List<Perfil> perfis) {
		String perfisString = "";
		for (int i = 0; i < perfis.size(); i++) {
			perfisString += perfis.get(i).getNome();
			if (i < perfis.size()-1) {
				perfisString += ", ";
			}
		}
		return perfisString;
	}

    @Override
	protected void addTableItems(Usuario entity, EntityLabelMap componentMap) {
		componentMap.add("nome", new LabelFrw("label", entity.getNome()));
		componentMap.add("login", new LabelFrw("label", entity.getLogin()));
		componentMap.add("tipoUsuario", new LabelFrw("label", entity.getTipoUsuario() !=  null ? entity.getTipoUsuario().getDescricao(): ""));
		componentMap.add("perfis", new LabelFrw("label", getPerfisString(entity.getPerfis())));
		componentMap.add("empresa", new LabelFrw("label", entity.getEmpresa() != null ? entity.getEmpresa().getRazaoSocial() : "-"));
		componentMap.add("excluido", new LabelFrw("label", getString(SistemaUtil.getBooleanKey(entity.isExcluido()))));
	}


    @Override
    protected void beforeLoadPage() {
        filterDTO = new FilterUsuarioDTO();
    }

	@Override
    protected Panel getEditPanel(Usuario entity, AjaxRequestTarget target) {
        return new EditUsuarioPanel("content", confirmationModal, entity);
    }

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();
		
		columns.add(new EntityColumnInfo("nome", new FrwResourceModel("usuario.label.nome")));
		columns.add(new EntityColumnInfo("login", new FrwResourceModel("usuario.label.login")));
		columns.add(new EntityColumnInfo("tipoUsuario", new FrwResourceModel("usuario.label.tipo.usuario")));
		columns.add(new EntityColumnInfo("perfis", new FrwResourceModel("usuario.label.perfil"),false));
		columns.add(new EntityColumnInfo("empresa", new FrwResourceModel("label.empresa")));
		columns.add(new EntityColumnInfo("excluido", new FrwResourceModel("usuario.label.excluido"),false));
		
		return columns;
	}
	
	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportUsuario();
	}

	@Override
    protected List<Usuario> loadList() {
        Usuario usuarioLogado = ((SistemaSession) Session.get()).getUsuarioLogado();
        filterDTO.setTipoUsuarioLogado(usuarioLogado.getTipoUsuario());
        List<Usuario> usuarios = cadastroFacade.pesquisaTodosUsuarios(filterDTO);
        Collections.sort(usuarios, new UsuarioComparator());
        return usuarios;
    }

	@Override
    protected void removeEntity(Usuario usuario, AjaxRequestTarget target) {
    	cadastroFacade.excluirUsuario(usuario);
    }

}
