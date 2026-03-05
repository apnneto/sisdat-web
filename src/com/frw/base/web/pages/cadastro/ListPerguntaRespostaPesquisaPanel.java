package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 * @author Igor Pessanha
 *
 */
public class ListPerguntaRespostaPesquisaPanel extends AbstractEntityListPanelNew<ColetaPesquisa>{
	
	private static final long serialVersionUID = 1L;
		@EJB
		private ColetaPesquisaFacade coletaPesquisaFacade;
		private String id;
		private Pesquisa pesquisa;

	    public ListPerguntaRespostaPesquisaPanel(String id, final UpdatableModalWindow confirmationModal, Pesquisa pesquisa) {
	        super(id, confirmationModal);
	        this.id = id;
	        this.pesquisa = pesquisa;
	        setOutputMarkupId(true);
	        
	        newLink.setVisible(true);
	        enableEditLink = true;
	        enableDeleteLink = false;
	        enableColunaAcao = false;
	        exportarExcelLink.setVisible(false);
	    }


	    @Override
	    protected void addTableItems(ColetaPesquisa pojo, EntityLabelMap componentMap) {
	    	
	    	String resposta = new String();
	    	try {
				if(pojo.getResposta() != null ){
					resposta += pojo.getResposta().getDescricao();
				}
				
				if(pojo.getCampoLivre() != null && !pojo.getCampoLivre().isEmpty()){
					resposta += pojo.getCampoLivre(); 
				}
				
				if(resposta.isEmpty()){
					resposta = "";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	componentMap.add("id", new LabelFrw("label", String.valueOf(pojo.getId())));
	    	componentMap.add("pergunta", new LabelFrw("label", pojo.getPergunta() != null ? pojo.getPergunta().getDescricao(): ""));
	    	componentMap.add("resposta", new LabelFrw("label", resposta));
	    	
	    }

	    @Override
		protected Panel getEditPanel(ColetaPesquisa entity, AjaxRequestTarget target) {
			return new EditColetaPesquisaPanel("listPerguntaResposta", confirmationModal, entity);
		}

	    @Override
	    protected List<EntityColumnInfo> getEntityColumnInfo() {
	        List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

	        columns.add(new EntityColumnInfo("id", new FrwResourceModel("coleta.pesquisa.label.id")));
	        columns.add(new EntityColumnInfo("pergunta", new FrwResourceModel("coleta.pesquisa.label.pergunta")));
	        columns.add(new EntityColumnInfo("resposta", new FrwResourceModel("coleta.pesquisa.label.resposta")));
	        
	        return columns;
	    }

		@Override
	    protected List<ColetaPesquisa> loadList() {
	            return coletaPesquisaFacade.pesquisarColetasPesquisaPorPesquisa(pesquisa);
	     }
		
/*		@Override
		protected AbstractXLSExport getXLSExportUtil() {
			return new XLSExportPerguntaRespostaPesquisa();
		}

		@Override
		public boolean isContentExportableToExcel() {
			return true;
		}*/
		
		@Override
	    protected void removeEntity(ColetaPesquisa entity, AjaxRequestTarget target) {
	        SistemaSession.setUserAndCurrentDate(entity);
	        coletaPesquisaFacade.excluirColetaPesquisa(entity);
	    }

}
