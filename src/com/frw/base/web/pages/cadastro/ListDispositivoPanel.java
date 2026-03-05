package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Dispositivo;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.DispositivoFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Leonardo Barros
 */
public class ListDispositivoPanel extends AbstractEntityListPanelNew<Dispositivo>{

		@EJB
		private DispositivoFacade dispositivoFacade;

	    public ListDispositivoPanel(String id, final UpdatableModalWindow confirmationModal) {
	        super(id, confirmationModal);
	        setOutputMarkupId(true);
	    }

	    @Override
	    protected void addTableItems(Dispositivo pojo, EntityLabelMap componentMap) {
	        
	    	componentMap.add("id", new LabelFrw("label", String.valueOf(pojo.getId())));
	    	componentMap.add("device", new LabelFrw("label", pojo.getDevice()));
	    	componentMap.add("usuario", new LabelFrw("label", pojo.getUsuario() != null ? pojo.getUsuario().getNome() : ""));

	    }

	    @Override
	    protected Panel getEditPanel(Dispositivo entity, AjaxRequestTarget target) {
	        return new EditDispositivoPanel("content", confirmationModal, entity);
	    }

	    @Override
	    protected List<EntityColumnInfo> getEntityColumnInfo() {
	        List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

	        columns.add(new EntityColumnInfo("id", new FrwResourceModel("dispositivo.label.id")));
	        columns.add(new EntityColumnInfo("device", new FrwResourceModel("dispositivo.label.device")));
	        columns.add(new EntityColumnInfo("usuario", new FrwResourceModel("dispositivo.label.usuario")));
	        
	        return columns;
	    }

	    @Override
	    protected List<Dispositivo> loadList() {
	            return dispositivoFacade.buscarTodosDispositivo();
	     }

	    @Override
	    protected void removeEntity(Dispositivo entity, AjaxRequestTarget target) throws SistemaException {
	        super.removeEntity(entity, target);
	        SistemaSession.setUserAndCurrentDate(entity);
	        dispositivoFacade.excluirDispositivo(entity);
	    }

}
