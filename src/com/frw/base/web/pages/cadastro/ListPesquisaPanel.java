package com.frw.base.web.pages.cadastro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Leonardo Barros
 */
public class ListPesquisaPanel extends AbstractEntityListPanelNew<Pesquisa>{

		private SimpleDateFormat dateHourFormat;

		@EJB
		private PesquisaFacade pesquisaFacade;

	    public ListPesquisaPanel(String id, final UpdatableModalWindow confirmationModal) {
	        super(id, confirmationModal);
	        setOutputMarkupId(true);
	        newLink.setVisible(false);
	    }
	    
	    public ListPesquisaPanel(String id, final UpdatableModalWindow confirmationModal, boolean selectedPanel) {
	        super(id, confirmationModal);
	        setOutputMarkupId(true);

	        if(selectedPanel == true)
	        {
	        	enableDeleteLink = false;
	        	enableEditLink = false;
	        	enableSelectLink = false;
	        	newLink.setVisible(false);
	        	exportarExcelLink.setVisible(false);
	        }
	        
	    }
	    @Override
	    protected void addTableItems(Pesquisa pojo, EntityLabelMap componentMap) {
	    	componentMap.add("questionario", new LabelFrw("label", pojo.getQuestionario() != null ? pojo.getQuestionario().getDescricao() : ""));
	    	componentMap.add("dataAbertura", new LabelFrw("label", pojo.getDataAbertura() != null ? dateHourFormat.format(pojo.getDataAbertura()): ""));
	    	componentMap.add("dataFechamento", new LabelFrw("label", pojo.getDataFechamento() != null ? dateHourFormat.format(pojo.getDataFechamento()) : ""));
	    	componentMap.add("dataSincronizacao", new LabelFrw("label", pojo.getDataSincronizacao() != null ?dateHourFormat.format(pojo.getDataSincronizacao()) : ""));
	    	componentMap.add("usuario", new LabelFrw("label", pojo.getUsuario() != null ? pojo.getUsuario().getNome() : ""));
	    }
	    @Override
	    protected void beforeLoadPage() {
	    	dateHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    }

	    @Override
		protected Panel getEditPanel(Pesquisa entity, AjaxRequestTarget target) {
			return new EditPerguntaRespostaPesquisaPanel("content", confirmationModal, entity);
		}

	    @Override
	    protected List<EntityColumnInfo> getEntityColumnInfo() {
	        List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

	        columns.add(new EntityColumnInfo("questionario", new FrwResourceModel("pesquisa.label.questionario")));
	        columns.add(new EntityColumnInfo("dataAbertura", new FrwResourceModel("pesquisa.label.data.abertura")));
	        columns.add(new EntityColumnInfo("dataFechamento", new FrwResourceModel("pesquisa.label.data.fechamento")));
	        columns.add(new EntityColumnInfo("dataSincronizacao", new FrwResourceModel("pesquisa.label.data.sincronizacao")));
	        columns.add(new EntityColumnInfo("usuario", new FrwResourceModel("pesquisa.label.usuario")));
	        
	        return columns;
	    }
	    
	    @Override
	    protected List<Pesquisa> loadList() {
	            return pesquisaFacade.buscarPesquisas();
	     }
	    @Override
	    protected void removeEntity(Pesquisa entity, AjaxRequestTarget target) 	throws SistemaException {
	    	entity.setVisivel(false);
	    	entity.setExcluido(true);
	    	pesquisaFacade.saveOrUpdate(entity);
	    }
}
