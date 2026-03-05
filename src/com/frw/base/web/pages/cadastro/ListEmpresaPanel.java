package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Miller
 */
@SuppressWarnings("serial")
public class ListEmpresaPanel extends AbstractEntityListPanelNew<Empresa>{

	@EJB
	private EmpresaFacade empresaFacade;
	
	public ListEmpresaPanel(String id, final UpdatableModalWindow confirmationModal) {
	    super(id, confirmationModal);
	    setOutputMarkupId(true);
	}
	
	@Override
	protected void addTableItems(Empresa entity, EntityLabelMap componentMap) {
		componentMap.add("razaoSocial", new LabelFrw("label", entity.getRazaoSocial()));
		componentMap.add("nomeFantasia", new LabelFrw("label", entity.getNomeFantasia()));
		componentMap.add("crea", new LabelFrw("label", entity.getCrea()));
	}
	
	@Override
	protected Panel getEditPanel(Empresa entity, AjaxRequestTarget target) {
	    return new AbasEmpresaPanel("content", confirmationModal, entity);
	}
	
	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
	    List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();
	
	    columns.add(new EntityColumnInfo("razaoSocial", new FrwResourceModel("label.razao.social")));
	    columns.add(new EntityColumnInfo("nomeFantasia", new FrwResourceModel("label.nome.fantasia")));
	    columns.add(new EntityColumnInfo("crea", new FrwResourceModel("label.crea")));
	    
	    return columns;
	}
	
	@Override
	protected List<Empresa> loadList() {
		return empresaFacade.buscarTodas();
	}
	
	@Override
	protected void removeEntity(Empresa entity, AjaxRequestTarget target) throws SistemaException {
	    SistemaSession.setUserAndCurrentDate(entity);
	    empresaFacade.deleteEmpresa(entity);
	}

}
