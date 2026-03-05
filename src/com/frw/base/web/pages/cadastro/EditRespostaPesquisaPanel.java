package com.frw.base.web.pages.cadastro;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.negocio.quiz.PerguntaFacade;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.util.SistemaUtil;
import com.frw.base.util.enumeration.FormatoDataEnum;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityListRespostaPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.base.Titulo;
import com.frw.base.web.pages.map.MapSinglePointPanel;
import com.frw.base.web.pages.seguranca.ViewPicturePanel;
import com.frw.base.web.pages.seguranca.filter.FilterListRespostaPesquisaPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.MapMarkerPoint;
import com.frw.manutencao.dominio.dto.FilterListRespostaPesquisaDTO;
import com.frw.negocio.export.xls.XLSExportRespostaPesquisa;

public class EditRespostaPesquisaPanel extends AbstractEntityListRespostaPanelNew<Pesquisa> {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private CadastroFacade cadastroFacade;
	
	@EJB
	private ColetaPesquisaFacade coletaPesquisaFacade;
	
	private Integer cont;
	
	private FilterListRespostaPesquisaDTO filterDTO;
	
	private List<ColetaPesquisa> listColetasPesquisas;
	
	private List<Pergunta> listPerguntas;
	private List<Pesquisa> listPesquisas;
	private FilterListRespostaPesquisaPanel panelFilter;
	
	@EJB
	private PerguntaFacade perguntasFacede;
	@EJB
	private PesquisaFacade pesquisaFacade;
	@EJB
	private QuestionarioFacade questionarioFacade;

	private String strLabel;
	
    public EditRespostaPesquisaPanel(String id, final UpdatableModalWindow confirmationModal, Questionario questionario) {
        super(id, confirmationModal, questionario);
        
        newLink.setVisible(false);
        enableEditLink = false;
        enableDeleteLink = true;
        enableColunaAcao = true;
        exportarExcelLink.setVisible(true);
        
		addFilterPanel();
    }
    
    @Override
	public int getListRowsPerPage() {
		return 2000;
	}
    
    @Override
	public boolean isContentExportableToExcel() {
		return true;
	}

    
    private void addFilterPanel() {
    	panelFilter = new FilterListRespostaPesquisaPanel("searchFilters", confirmationModal, listContainer,  filterDTO, this.strLabel);
    	panelFilter.setOutputMarkupId(true);
        filtersContainer.replaceWith(panelFilter);
	}

    private Component getImagem(final Pesquisa entity){ 
		
		try {
			if (entity != null) {
				if (entity.getFotos() != null && !entity.getFotos().isEmpty()) {
					
					AjaxLink<String> showLocalLink = new AjaxLink<String>("label") {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							target.appendJavaScript("Wicket.Window.unloadConfirmation=false");
							ViewPicturePanel picturePanel = new ViewPicturePanel(confirmationModal.getContentId(), entity.getFotos());
							confirmationModal.setContent(picturePanel);
							confirmationModal.setInitialHeight(410);
							confirmationModal.setInitialWidth(680);
							confirmationModal.setTitle("Foto");
							confirmationModal.show(target);
						}
					};
					showLocalLink.add(new AttributeModifier("class", new Model<String>("icone picture cursor")));
					return showLocalLink;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		return new LabelFrw("label", "");
	}

	private Component getLocal(final Pesquisa entity) {

		if (entity != null 
				&& entity.getLatitudeFinal() != null && entity.getLongitudeFinal() != null
				&& entity.getLatitudeInicial() != 0.0 && entity.getLongitudeInicial() != 0.0
				&& entity.getLatitudeFinal() != 0.0 && entity.getLongitudeFinal() != 0.0) {

			AjaxLink<String> showLocalLink = new AjaxLink<String>("label") {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("Wicket.Window.unloadConfirmation=false");
					
					MapSinglePointPanel mapPanel = new MapSinglePointPanel(confirmationModal.getContentId(), new MapMarkerPoint(entity));

					confirmationModal.setContent(mapPanel);
					confirmationModal.setInitialHeight(410);
					confirmationModal.setInitialWidth(680);
					confirmationModal.setTitle("Local da Pesquisa");

					confirmationModal.show(target);
				}
			};

			showLocalLink.add(new AttributeModifier("class", new Model<String>("icone ver cursor")));

			return showLocalLink;

		} else {
			return new LabelFrw("label", " ");
		}

	}

	@Override
	protected void addTableItems(Pesquisa pojo, EntityLabelMap componentMap) {
		
		try {
			
			listColetasPesquisas = coletaPesquisaFacade.pesquisarColetasPesquisaPorPesquisa(listPesquisas.get(cont));
			ColetaPesquisa coletaColuna ;
			
			componentMap.add("mapa", getLocal(pojo));
			componentMap.add("imagem", getImagem(pojo));
			Pesquisa pesquisa = listPesquisas.get(cont);
			componentMap.add("usuario", new Label("label", pesquisa != null && pesquisa.getUsuario() != null ? pesquisa.getUsuario().getNome().toUpperCase() : ""));
			
			for(Pergunta pergunta : listPerguntas){
				coletaColuna = null;
				
				for(ColetaPesquisa coleta : listColetasPesquisas){
					if(coleta.getPergunta().getId().equals(pergunta.getId()))
						coletaColuna = coleta;
				}
				
				if(coletaColuna != null)
					componentMap.add(pergunta.getId().toString(), new Label("label", pesquisaFacade.getReposta(coletaColuna)));
				else{
					componentMap.add(pergunta.getId().toString(), new Label("label", ""));
				}
				componentMap.add("latitudeFinal", new Label("label", pesquisa.getLatitudeFinal() != null ? SistemaUtil.formatNumeric(pesquisa.getLatitudeFinal(), 6) : ""));
				componentMap.add("longitudeFinal", new Label("label", pesquisa.getLongitudeFinal() != null ? SistemaUtil.formatNumeric(pesquisa.getLongitudeFinal(), 6) : ""));
				componentMap.add("fotos", new LabelFrw("label", coletaPesquisaFacade.getNomeFotos(pojo)));
				componentMap.add("dataSincronizacao", new LabelFrw("label",pojo.getDataSincronizacao() != null ? SistemaUtil.formatDate(pojo.getDataSincronizacao(), FormatoDataEnum.DATA_HORA_MINUTO) :""));
			}
			
			listColetasPesquisas.clear();
			cont++;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void ajustTitulo(EntityColumnInfo i, Titulo titulo) {
		Pergunta pergunta = perguntasFacede.buscarPorId(Long.parseLong(i.getProperty()));
		titulo.setTitulo(pergunta.getDescricao());
	}

	@Override
    protected void beforeLoadPage(Questionario questionario) {
    	this.setOutputMarkupId(true);

    	if(filterDTO == null)
    		filterDTO = new FilterListRespostaPesquisaDTO();
    	
    	listPerguntas = perguntasFacede.buscarPerguntasPorQuestionario(questionario);
    	this.strLabel = questionario != null ? "Questionário:   " + questionario.getDescricao() +  "       (" + questionario.getCodigo()+")" : "";
        filterDTO.setUsuario(getUsuarioLogado());
        filterDTO.setEmpresa(getUsuarioLogado().getEmpresa());
        
    }
	
	
	@Override
    protected List<EntityColumnInfo> getEntityColumnInfo() {
        List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();
        
        columns.add(new EntityColumnInfo("mapa", new FrwResourceModel("label.empty"), false, false, false));
        columns.add(new EntityColumnInfo("imagem", new FrwResourceModel("label.fotos"), false, false, false));
        columns.add(new EntityColumnInfo("usuario", new FrwResourceModel("label.usuarios"), false, false, false));
        columns.add(new EntityColumnInfo("latitudeFinal", new FrwResourceModel("label.latitude"), false, false, true));
        columns.add(new EntityColumnInfo("longitudeFinal", new FrwResourceModel("label.longitude"), false, false, true));
        columns.add(new EntityColumnInfo("dataSincronizacao", new FrwResourceModel("pesquisa.label.data.sincronizacao"), false, false, false));
        
        
        for(Pergunta pergunta : listPerguntas){
			columns.add(new EntityColumnInfo(pergunta.getId().toString(), new Model<String>(pergunta != null ? resumeString(pergunta.getDescricao()): ""),false));
		}
        columns.add(new EntityColumnInfo("fotos", new FrwResourceModel("label.fotos"), false, false, true));
        
        return columns;
    }
	
	@Override
	protected String getPalavraResultadosEncontradosLabel() {
		return getString("label.questionarios.respondidos");
	}
	
	@Override
	protected String getStringResultadosNaoEncontrados() {
		String mensagem = super.getStringResultadosNaoEncontrados();
		if (getUsuarioLogado().getEmpresa() == null) {
			mensagem += ". <br/>Problema encontrado: " + getString("mensagem.usuario.sem.empresa");
		}
		return mensagem;
	}

	@Override
	protected XLSExportRespostaPesquisa getXLSExportUtil() {
		Empresa empresa = getUsuarioLogado() != null ? getUsuarioLogado().getEmpresa() : null;
		return new XLSExportRespostaPesquisa(empresa);
	}

	@Override
    protected List<Pesquisa> loadList(Questionario questionario) {
    	cont = 0;
		listPesquisas = pesquisaFacade.buscarPesquisaPorQuestionarioUsuario(questionario, filterDTO);
		return listPesquisas;
    }

	@Override
    protected void removeEntity(Pesquisa pojo, AjaxRequestTarget target) {
        SistemaSession.setUserAndCurrentDate(pojo);
        if(getUsuarioLogado().getTipoUsuario().getId().equals(TipoUsuario.ADM)){
        	pojo.setExcluido(true);
        }
        pojo.setVisivel(false);
        pesquisaFacade.saveOrUpdate(pojo);
    }
}
