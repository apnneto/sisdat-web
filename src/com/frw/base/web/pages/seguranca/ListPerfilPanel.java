package com.frw.base.web.pages.seguranca;


import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.base.Perfil;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportPerfil;



/**
 *
 * @author Leonardo Barros
 */
public class ListPerfilPanel extends AbstractEntityListPanelNew<Perfil> {

    @EJB
    private CadastroFacade cadastroFacade;

    public ListPerfilPanel(String id, final UpdatableModalWindow confirmationModal) {
        super(id, confirmationModal);
        setOutputMarkupId(true);
    }


    @Override
	public boolean isContentExportableToExcel() {
		return true;
	}

    @Override
    protected void addTableItems(Perfil pojo, EntityLabelMap componentMap) {
        
    	componentMap.add("id", new LabelFrw("label", pojo.getId().toString())); 
    	componentMap.add("nome", new LabelFrw("label", pojo.getNome())); 
    	componentMap.add("tipoUsuario", new LabelFrw("label", pojo.getTipoUsuario() != null ? pojo.getTipoUsuario().getDescricao() : ""));  

    }

    @Override
    protected Panel getEditPanel(Perfil entity, AjaxRequestTarget target) {
        return new EditPerfilPanel("content", confirmationModal, entity);
    }

    @Override
    protected List<EntityColumnInfo> getEntityColumnInfo() {
        List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

        columns.add(new EntityColumnInfo("id", new FrwResourceModel("label.id"),false,true));
        columns.add(new EntityColumnInfo("nome", new FrwResourceModel("nome"),true,true));
        columns.add(new EntityColumnInfo("tipoUsuario", new FrwResourceModel("tipoUsuario"),true,true));
        
        return columns;
    }

    @Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportPerfil();
	}
    
    @Override
    protected List<Perfil> loadList() {
            return cadastroFacade.pesquisarPerfis(getUsuarioLogado());
     }

	@Override
    protected void removeEntity(Perfil entity, AjaxRequestTarget target) throws SistemaException {
        super.removeEntity(entity, target);
        cadastroFacade.excluirPerfil(entity);
    }

}
