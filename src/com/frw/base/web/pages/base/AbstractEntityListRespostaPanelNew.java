package com.frw.base.web.pages.base;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.frw.base.dao.sisdat.FotoDAO;
import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Foto;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.ShowAnexoPage;
import com.frw.base.web.pages.util.EntityComparator;
import com.frw.base.web.pages.util.ModalAlertPanel;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.frw.base.web.pages.util.MyPagingNavigator;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.BasePageUtil;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.SistemaConstants;
import com.frw.negocio.export.xls.XLSExportRespostaPesquisa;

/**
 *
 * @author juliano
 */
@SuppressWarnings("serial")
public abstract class AbstractEntityListRespostaPanelNew<T extends EntidadeBase> extends Panel implements SortableListPanel {

	public class EntityLabelMap extends HashMap<String, LabelMapEntry> {

        public void add(String property, Component c) {
            add(property, c, null);
        }

        public void add(String property, Component c, String cssClass) {
            LabelMapEntry entry = new LabelMapEntry();
            entry.component = c;
            entry.cssClass = cssClass;
            super.put(property, entry);
        }
    }
	public interface OnClickHandler {   public void onClick(AjaxRequestTarget target); }
	
	protected class EntityListView extends PageableListView<T> {

		public EntityListView(String id, IModel<? extends List<? extends T>> model, int rowsPerPage) {
            super(id, (IModel)model, (long)rowsPerPage);
        }

        @Override
        protected void populateItem(final ListItem<T> item) {

            beforePopulateItem(item);

            final T object = item.getModelObject();
            //final Page editPage = getEditPage(object);
            
            
            WebMarkupContainer webBodyAction = new WebMarkupContainer("bodyAction");
            webBodyAction.setOutputMarkupId(true);
            webBodyAction.setVisible(isBodyActionVisible());
            item.add(webBodyAction);

            AjaxLink editLink = new AjaxLink("editLink") {

                @Override
                public void onClick(AjaxRequestTarget target) {
                    Page p = getEditPage(object);
                    if (p != null) {
                        setResponsePage(p);
                    } else {
                        Panel panel = getEditPanel(object, target);
                        if (panel != null) {
                            panel.setOutputMarkupId(true);
                            editEntity(panel);
                            target.add(panel);
                        }
                    }
                }
            };


            editLink.setEnabled(enableEditLink);
            editLink.setVisible(enableEditLink);

            
            //item.add(editLink);
            webBodyAction.add(editLink);

           /* item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

                @Override
                public String getObject() {
                    return (item.getIndex() % 2 == 1) ? "linha2" : "linha1";
                }
            }));*/


            final EntityLabelMap bodyColumnMap = new EntityLabelMap();
            addTableItems(item.getModelObject(), bodyColumnMap);

            ListView<EntityColumnInfo> bodyColumns = new ListView<EntityColumnInfo>("bodyColumns", visibleColumnsModel) {

                @Override
                protected void populateItem(ListItem<EntityColumnInfo> li) {

                    LabelMapEntry labelComponent = bodyColumnMap.get(li.getModelObject().property);
                    if (labelComponent == null) {
                        throw new RuntimeException("Nao foi possivel encontrar componente de label para propriedade " + li.getModelObject().property);
                    }
                    li.add(labelComponent.component);

                    if (labelComponent.cssClass != null) {
                        li.add(new AttributeModifier("class", new Model<String>(labelComponent.cssClass)));
                    }



                }
            };
            
            item.add(bodyColumns);

            colunaAcao = new WebMarkupContainer("colunaAcao");
           
            
            item.add(colunaAcao);
            

            AjaxLink removeLink = new AjaxLink("removeLink") {

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    target.appendJavaScript("Wicket.Window.unloadConfirmation=false");
                    confirmationModal.setContent(new ModalConfirmationPanel(confirmationModal.getContentId(), "message.confirmation.delete") {

                        @Override
                        protected boolean onConfirm(AjaxRequestTarget tg) {
                            try {
                                removeEntity(item.getModelObject(), tg);
                            } catch (Exception e) {
                                Throwable root = ExceptionUtils.getRootCause(e);
                                if (root == null) {
                                    root = e;
                                }

                                ModalAlertPanel alert = new ModalAlertPanel(confirmationModal.getContentId(), getString(root.getMessage(), item.getModel(), "Não foi possivel excluir este ítem, ele já deve estar sendo utilizado no sistema."));
                                confirmationModal.setContent(alert);
                                confirmationModal.update(tg);
                                return false;
                            }
                            EntityListView.this.getModel().detach();
                            tg.add(listContainer);
                            return true;
                        }
                    });

                    confirmationModal.show(target);
                }
            };
            
            removeLink.setVisible(enableDeleteLink);
            
            if (!removeLink.isEnabled()) {
            	removeLink.add(new AttributeModifier("class", new Model<String>("sub-excluir")));
            }
            

            colunaAcao.add(removeLink);

            colunaAcao.setVisible(enableColunaAcao);
            lastActionColumn.setVisible(enableColunaAcao);


             selectLink = new AjaxLink("selectLink") {
               @Override
                public void onClick(final AjaxRequestTarget target) {
                    onSelectClick(item.getModelObject(),target);
                }
            };
            
            selectLink.setVisible(enableSelectLink);
            colunaAcao.add(selectLink);

            afterPopulateItem(item, removeLink, editLink);

            if (!editLink.isEnabled()) {
            	editLink.add(new AttributeModifier("class", new Model<String>("icone editar disabled")));
            }

            editLink.add(new AttributeModifier("title", new Model<String>(""+item.getModelObject().getId())));
        }

    }
    protected class LabelMapEntry {

        Component component;
        String cssClass;
    }
    protected class VisibleEntityColumnsModel extends LoadableDetachableModel<List<EntityColumnInfo>> {

        @Override
        protected List<EntityColumnInfo> load() {
            return getVisibleColumns();
        }
    }
    private Label lblRegistrosEncontrados;
    private Logger logger = Logger.getLogger(getClass().getName());
    private OnClickHandler onClickHandler;
    @EJB
    private PesquisaFacade pesquisaFacade;
    private List<Pesquisa> pesquisas;
    private T selectedEntity;
    
    @EJB
	protected ColetaPesquisaFacade coletaPesquisaFacade;
    protected List<EntityColumnInfo> columns;
    protected WebMarkupContainer colunaAcao;
    protected UpdatableModalWindow confirmationModal;
    protected boolean enableColunaAcao = true;
    protected boolean enableColunaCodigo = true;
    protected boolean enableDeleteLink = true;
    /** flag para controlar  a exibição dos links de edição e exclusão da listagem */
    protected boolean enableEditLink = true;
    protected boolean enableSelectLink = false;
    
    
    protected EntityComparator entityComparator;
    protected Link exportarExcelLink;
   

    protected Link exportarPDFLink;
    protected FeedbackPanel feedback;
    protected WebMarkupContainer filtersContainer;

    protected WebMarkupContainer footerContainer;
    @Inject
	protected FotoDAO fotoDAO;
    
    protected WebMarkupContainer lastActionColumn;
    
    protected LabelFrw lblMsgEmpty;
    
    protected EntityListView listaView;
    
    protected WebMarkupContainer listContainer;

    //protected LoadableDetachableModel<List<T>> model;
    protected IModel model;

    protected AjaxLink newLink;

    protected AjaxLink selectLink;

    protected VisibleEntityColumnsModel visibleColumnsModel = new VisibleEntityColumnsModel();

    public AbstractEntityListRespostaPanelNew(String id, UpdatableModalWindow confirmationModal, Questionario questionario) {
        super(id);
        this.confirmationModal = confirmationModal;

        beforeLoadPage(questionario);

        columns = getEntityColumnInfo();

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        newLink = new AjaxLink("editPageLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    onBeforeNewLinkClick();
                } catch(SistemaException e) {
                    error(getString(e.getMessage()));
                    target.add(feedback);
                    return;
                }

                Class page = getEditPageClass();
                if (page != null) {
                    setResponsePage(page);
                } else {
                    Panel panel = getEditPanel(getNewEntityInstance(), target);
                    if (panel != null) {
                        panel.setOutputMarkupId(true);
                        editEntity(panel);
                        target.add(panel);
                    }
                }
            }

        };
        newLink.add(new LabelFrw("caption", new Model(getNewEntiyLinkLabel())));
        add(newLink);
        
        if (!newLink.isEnabled()) {
        	newLink.add(new AttributeModifier("class", new Model<String>("sub-incluir")));
        }
        
        exportarExcelLink = new Link("btnExportarExcel") {

			@Override
            public void onClick() {
				
				// carrega as informações para o relatorio xls
				LinkedHashMap<String, Titulo> titulos = new LinkedHashMap<String, Titulo>();
				for (EntityColumnInfo i : columns) {
					if (i.canBeExported) {
						Titulo titulo = new Titulo();
						
						if (!i.getProperty().equals(XLSExportRespostaPesquisa.PROPERTY_COL_FOTOS) 
								&& !i.getProperty().equals(XLSExportRespostaPesquisa.PROPERTY_COL_LATITUDE)
								&& !i.getProperty().equals(XLSExportRespostaPesquisa.PROPERTY_COL_LONGITUDE)) {
							ajustTitulo(i, titulo);
						}
						titulo.setEntityColumnInfo(i);

						titulos.put(i.getProperty(), titulo);
					}
				}
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				XLSExportRespostaPesquisa ixlsExport = getXLSExportUtil();
				ixlsExport.generateXLSTable(out, titulos, listaView.getList(),pesquisaFacade);
				byte[] planilhaXls = out.toByteArray();

				try {

					List<Foto> fotos = new ArrayList<Foto>();

					for (Pesquisa p : pesquisas) {
						List<Foto> fotosPesquisa = p.getFotos();
						if (fotosPesquisa != null && !fotosPesquisa.isEmpty()) {
							fotos.addAll(pesquisaFacade.buscarFotosPesquisa(p));
						}
					}

					// caso nao tenha fotos, nao sera gerado um arquivo zip
					if (fotos == null || fotos.isEmpty()) {
						{ jakarta.servlet.http.HttpServletResponse r = (jakarta.servlet.http.HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse(); try { r.setHeader("Content-Disposition","attachment;filename="+ "RespostasPesquisa.xls"); r.setHeader("Content-Type","application/octet-stream"); r.getOutputStream().write(planilhaXls); r.flushBuffer(); } catch(Exception _ex){} }
					} else {

						byte[] zipFile = pesquisaFacade.generateZipFile(fotos, planilhaXls);
						ShowAnexoPage zipAnexo = null;
						String zipName = "SisDAT-" + 
								pesquisas.get(0).getQuestionario().getCodigo().replace(".", "").replace("-", "").replace(" ", "") + new Date().getTime()
								+ ".zip";
						zipAnexo = new ShowAnexoPage(zipFile, zipName);

						logger.severe("Download de Zip -> " + zipAnexo.getFileName());

						zipAnexo.respond();
						
					}// END if fotos
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

        };
        exportarExcelLink.setVisible(Boolean.FALSE);
        exportarExcelLink.setOutputMarkupId(true);
        exportarExcelLink.add(new LabelFrw("captionExcel", new Model("Exportar Excel")));
        add(exportarExcelLink);
        

        Fragment fragment = new Fragment("botoesContainer", "botoesContainerAdded", this);
        add(fragment);

        addFragmentAditionalButtons(fragment);

        entityComparator = new EntityComparator(getEntityClass());

        

        model = getListModel(questionario);

        exportarPDFLink = new Link("btnExportarPDF") {

            @Override
            public void onClick() {
                byte[] bytes  = generatePDFReport(listaView.getList());
                { jakarta.servlet.http.HttpServletResponse r = (jakarta.servlet.http.HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse(); try { r.setHeader("Content-Disposition","attachment;filename="+ "grid_export.pdf"); r.setHeader("Content-Type","application/octet-stream"); r.getOutputStream().write(bytes); r.flushBuffer(); } catch(Exception _ex){} }
            }
        };
        exportarPDFLink.setVisible(Boolean.FALSE);
        exportarPDFLink.setOutputMarkupId(true);

        /** add search filter fields */
        filtersContainer = new WebMarkupContainer("searchFilters");
        add(filtersContainer);


        lblMsgEmpty = new LabelFrw("msgEmpty", BasePageUtil.getInstance().getString("label.pesquisa.list.empty"));
        lblMsgEmpty.setVisible(false);

        listaView = new EntityListView("list", model, getListRowsPerPage());

        // carrega as fotos
     	pesquisas = (List<Pesquisa>) listaView.getList();
     	pesquisaFacade.gerarNomeFotos(pesquisas);

        Image imgPDF = new Image("downloadImagePDF", new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/pdf.png")));
        exportarPDFLink.add(imgPDF);

        /* add empty footer container */
        footerContainer = getFooterListPanel();
        footerContainer.setOutputMarkupId(true);

        
        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(listaView);
        
        lblRegistrosEncontrados = new Label("registrosEncontrados",
            org.apache.wicket.model.LambdaModel.of(() -> getRegistrosEncontrados()));
        lblRegistrosEncontrados.setEscapeModelStrings(false);
		listContainer.add(lblRegistrosEncontrados);
		
        MyPagingNavigator navigator = new MyPagingNavigator("navigator", listaView);
        navigator.setOutputMarkupId(true);
        navigator.setVisible(showPagingNavigator());
        listContainer.add(navigator);
        
        WebMarkupContainer headerAction = new WebMarkupContainer("headerAction");
        headerAction.setOutputMarkupId(true);
        headerAction.setVisible(isHeaderActionVisible());
        listContainer.add(headerAction);
        
        listContainer.add(getTableHeader());
        
        lastActionColumn = new WebMarkupContainer("lastActionColumn");
        
        
        listContainer.add(lastActionColumn);
        listContainer.add(lblMsgEmpty);

        listContainer.add(exportarPDFLink);

        listContainer.add(footerContainer);

        add(listContainer);


        afterLoadPage();

    }

    public void afterPopulateItem(ListItem<T> item, AbstractLink removeLink, AbstractLink editLink) {
    }
    
    public void beforePopulateItem(ListItem<T> item) {
    }

    public void disablePanelActions(){
    	this.enableEditLink = false;
    	this.newLink.setVisible(false);
    	this.enableDeleteLink = false;
    	this.enableSelectLink = false;
    	
    }

    public Class getEntityClass() {

        ParameterizedType t = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) t.getActualTypeArguments()[0];
    };
    public DefaultListColumnHeader getEntityColumnHeader(EntityColumnInfo i) {
        return new DefaultListColumnHeader("headerColumn", i, AbstractEntityListRespostaPanelNew.this);
    }

    public EntityComparator getEntityComparator() {
        return entityComparator;
    }

    public WebMarkupContainer getFooterListPanel() {
        return new WebMarkupContainer("footerPanel");
    }

	public int getListRowsPerPage() {
        return SistemaConstants.ROWS_PER_PAGE;
    }

	public OnClickHandler getOnClickHandler() {
        return onClickHandler;
    }

	public T getSelectedEntity() {
        return selectedEntity;
    }

	public WebMarkupContainer getTableHeader() {


        final ListView<EntityColumnInfo> headerColumns = new ListView<EntityColumnInfo>("headerColumns", visibleColumnsModel) {

            @Override
            protected void populateItem(final ListItem<EntityColumnInfo> li) {

                li.add(getEntityColumnHeader(li.getModelObject()));
            }
        };

        return headerColumns;


    }
	public Usuario getUsuarioLogado() {
        SistemaSession session = (SistemaSession) Session.get();
        return session.getUsuarioLogado();
    }
	
	
    @Override
    public void hideColumn(EntityColumnInfo columnInfo, AjaxRequestTarget art) {
        columnInfo.visible = false;
        visibleColumnsModel.detach();
        art.add(listContainer);
    }


    public boolean isContentExportableToExcel() {
        return false;
    }

    public boolean isContentExportableToPDF() {
        return false;
    }

    public void setEntityComparator(EntityComparator entityComparator) {
        this.entityComparator = entityComparator;
    }

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public void setSelectedEntity(T selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    @Override
    public void setSortingField(String field, AjaxRequestTarget target) {
        setSortingField(field);

        Collections.sort((List)model.getObject(),entityComparator);
        //model.detach();
        
        target.add(listContainer);

    }

    private String getRegistrosEncontrados() {
		if (listaView.getList() != null && !listaView.getList().isEmpty()) {
			return new StringResourceModel("label.resultados.encontrados", this).setParameters(new Object[]{getPalavraResultadosEncontradosLabel(), listaView.getList().size()}).getObject();
		}else {
			return getStringResultadosNaoEncontrados();
		}
	}

    private List<EntityColumnInfo> getVisibleColumns() {
        List<EntityColumnInfo> visible = new ArrayList<EntityColumnInfo>();
        for (EntityColumnInfo i : columns) {
            if (i.visible) {
                visible.add(i);
            }
        }

        return visible;
    }

    private void setSortingField(String field) {
        if (entityComparator.getSortingField() == null || !entityComparator.getSortingField().equals(field)) {
            entityComparator.setSortingField(field);
            entityComparator.setSortDirection(EntityComparator.SortDirection.ASCENDING);
        } else if (entityComparator.getSortingField().equals(field)) {
            entityComparator.reverseSortDirection();
        }
    }

    protected void addFragmentAditionalButtons(Fragment fragment) {
    }

    protected abstract void addTableItems(T entity, EntityLabelMap componentMap);

    protected void afterLoadPage() {
    }

    protected void ajustTitulo(EntityColumnInfo i, Titulo titulo) {
    	titulo.setTitulo(BasePageUtil.getInstance().getString(((FrwResourceModel) i.getName()).getResouceKey()));
	}

    protected void beforeLoadPage(Questionario questionario) {
    }

    protected void editEntity(Panel editPanel) {
        this.replaceWith(editPanel);
    }

    protected byte[] generatePDFReport(List<? extends T> list) {
        return null;
    }

    protected Page getEditPage(T entity) {
        return null;
    }

    protected Class getEditPageClass() {
        return null;
    }

    protected Panel getEditPanel(T entity, AjaxRequestTarget target) {
        return null;
    }

    protected EntityColumnInfo getEntityColumInfo(String name) {
        for (EntityColumnInfo entityColumnInfo : columns) {
            if (entityColumnInfo.getProperty().equals(name)) {
                return entityColumnInfo;
            }
        }
        throw new RuntimeException("Nao foi possivel encontrar a coluna " + name);
    }

    protected abstract List<EntityColumnInfo> getEntityColumnInfo();

    protected IModel getListModel(final Questionario questionario) {
        return new LoadableDetachableModel<List<T>>() {
            @Override
            protected List<T> load() {
                 List<T> list = loadList(questionario);
            if (entityComparator.getSortingField() != null) {
                Collections.sort(list, entityComparator);
            }

            if(!isContentExportableToExcel()) {
                exportarExcelLink.setVisible(false);
            } else {
                exportarExcelLink.setVisible(true);
            }

            if(list == null || list.isEmpty() || !isContentExportableToPDF()) {
                exportarPDFLink.setVisible(false);
            } else {
                exportarPDFLink.setVisible(true);
            }

            return list;
            }
        };
    }

    protected T getNewEntityInstance() {
        try {
            ParameterizedType t = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) t.getActualTypeArguments()[0];
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("erro: " + e);
        }
    }

    protected String getNewEntiyLinkLabel() {
        return BasePageUtil.getInstance().getString("novo.generico");
    }

    /**
	 * Define a palavra utilizada para a descricao da mensagem de resultados encontrados.
	 * Ex.: Total de [PALAVRA] encontrados(as): 7
	 * @return [PALAVRA]
	 */
	protected abstract String getPalavraResultadosEncontradosLabel();

    protected String getStringResultadosNaoEncontrados() {
		return new StringResourceModel("label.resultados.nao.encontrados", this, null).getObject();
	}

    protected abstract XLSExportRespostaPesquisa getXLSExportUtil();

    protected Boolean isBodyActionVisible(){
    	return Boolean.TRUE;
    }

    protected Boolean isHeaderActionVisible(){
    	return Boolean.TRUE;
    }

    protected abstract List<T> loadList(Questionario questionario);

    protected void onBeforeNewLinkClick() throws SistemaException {
        return;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (listaView.getList() == null || listaView.getList().isEmpty()) {
            lblMsgEmpty = new LabelFrw("msgEmpty", BasePageUtil.getInstance().getString("label.pesquisa.list.empty"));
            lblMsgEmpty.setVisible(true);
        }
    }

    protected void onSelectClick(T entity, AjaxRequestTarget target){
        selectedEntity = entity;
        if(onClickHandler != null)
            onClickHandler.onClick(target);
    }
    
    protected void removeEntity(T entity, AjaxRequestTarget target) throws SistemaException {
    }
    
    protected String resumeString(String valor) {
    	
    	final int MAX = 50;
    	
    	if ((valor != null) && (!valor.trim().equals(""))) {
    		
    		if (valor.length() > MAX)
    			return valor.substring(0, MAX) + "...";
    		else
    			return valor;
    		
    	} else {
    		return "-";
    	}

    }
    
    protected boolean showPagingNavigator() {
        return Boolean.TRUE;
    }
}
