package com.frw.base.web.pages.base;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.Entidade;
import com.frw.base.util.XLSExportUtil;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.ShowAnexoPage;
import com.frw.base.web.pages.util.EntityComparator;
import com.frw.base.web.pages.util.MyPagingNavigator;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.BasePageUtil;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.SistemaConstants;
import com.frw.negocio.export.xls.AbstractXLSExport;

/**
 * @author Carlos Santos
 */
public abstract class AbstractListPanel<T extends Entidade> extends Panel implements SortableListPanel {

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
    protected class EntityListView extends PageableListView<T> {

        public EntityListView(String id, IModel<? extends List<? extends T>> model, int rowsPerPage) {
            super(id, model, rowsPerPage);
        }

        @Override
        protected void populateItem(final ListItem<T> item) {

            beforePopulateItem(item);
            populateTableRow(item);
            afterPopulateItem(item);

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
    protected List<EntityColumnInfo> columns;
    protected UpdatableModalWindow confirmationModal;
    protected NonCachingImage editImg;

    protected EntityComparator entityComparator;
    protected Link exportarExcelLink;
    protected Link exportarPDFLink;

    protected FeedbackPanel feedback;
    protected WebMarkupContainer filtersContainer;
    protected WebMarkupContainer footerContainer;

    protected LabelFrw lblMsgEmpty;
    protected EntityListView listaView;

    protected WebMarkupContainer listContainer;

	protected IModel model;

    protected AjaxLink newLink;

    protected VisibleEntityColumnsModel visibleColumnsModel = new VisibleEntityColumnsModel();

    public AbstractListPanel(String id, UpdatableModalWindow confirmationModal) {
        super(id);
        this.confirmationModal = confirmationModal;

        beforeLoadPage();

        columns = getEntityColumnInfo();
        
        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        entityComparator = new EntityComparator(getEntityClass());

        exportarExcelLink = new Link("btnExportarExcel") {

            @Override
            public void onClick() {

                LinkedHashMap<String, Titulo> titulos = new LinkedHashMap<String, Titulo>();
                for (EntityColumnInfo i : columns) {
                    if (i.canBeExported) {
                    	Titulo titulo = new Titulo();
                    	titulo.setTitulo(BasePageUtil.getInstance().getString(((FrwResourceModel) i.getName()).getResouceKey()));
                    	titulo.setEntityColumnInfo(i);
                        titulos.put(i.getProperty(), titulo);
                    }
                }

                ByteArrayOutputStream out =  new ByteArrayOutputStream();
                AbstractXLSExport ixlsExport = getXLSExportUtil();
                ixlsExport.generateXLSTable(out, titulos, listaView.getList());
                byte[] bytes  = out.toByteArray();
                RequestCycle.get().setRequestTarget( new ShowAnexoPage(bytes, "parcerias_grid_export.xls"));
            }
        };
        exportarExcelLink.setVisible(Boolean.FALSE);
        exportarExcelLink.setOutputMarkupId(true);

        model = getListModel();

        exportarPDFLink = new Link("btnExportarPDF") {

            @Override
            public void onClick() {
                byte[] bytes  = generatePDFReport(listaView.getList());
                RequestCycle.get().setRequestTarget( new ShowAnexoPage(bytes, "parcerias_grid_export.pdf"));
            }
        };
        exportarPDFLink.setVisible(Boolean.FALSE);
        exportarPDFLink.setOutputMarkupId(true);

        Image imgExcel = new Image("downloadImage", new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/excel.png")));
        exportarExcelLink.add(imgExcel);

        Image imgPDF = new Image("downloadImagePDF", new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/pdf.png")));
        exportarPDFLink.add(imgPDF);

        /** add search filter fields */
        filtersContainer = new WebMarkupContainer("searchFilters");
        add(filtersContainer);

        lblMsgEmpty = new LabelFrw("msgEmpty", BasePageUtil.getInstance().getString("label.pesquisa.list.empty"));
        lblMsgEmpty.setVisible(false);

        listaView = new EntityListView("list", model, getListRowsPerPage());

        /* add empty footer container */
        footerContainer = getFooterListPanel();
        footerContainer.setOutputMarkupId(true);

        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(listaView);
        listContainer.add(exportarExcelLink);
        listContainer.add(exportarPDFLink);
        listContainer.add(new MyPagingNavigator("navigator", listaView));
        listContainer.add(getTableHeader());
        listContainer.add(lblMsgEmpty);
        listContainer.add(footerContainer);
        add(listContainer);

        afterLoadPage();

    }

    public void afterPopulateItem(ListItem<T> item) { }

    public void beforePopulateItem(ListItem<T> item) { };
    public DefaultListColumnHeader getColumnHeader(EntityColumnInfo i) {
        return new DefaultListColumnHeader("headerColumn", i, AbstractListPanel.this);
    }

    public Class getEntityClass() {
        ParameterizedType t = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) t.getActualTypeArguments()[0];
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

    public WebMarkupContainer getTableHeader() {

        final ListView<EntityColumnInfo> headerColumns = new ListView<EntityColumnInfo>("headerColumns", visibleColumnsModel) {
            @Override
            protected void populateItem(final ListItem<EntityColumnInfo> li) {
                li.add(getColumnHeader(li.getModelObject()));
            }
        };

        return headerColumns;

    }

    public void hideColumn(EntityColumnInfo columnInfo, AjaxRequestTarget art) {
        columnInfo.visible = false;
        visibleColumnsModel.detach();
        art.addComponent(listContainer);
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

    @Override
    public void setSortingField(String field, AjaxRequestTarget target) {
        setSortingField(field);
        Collections.sort((List)model.getObject(),entityComparator);
        target.addComponent(listContainer);
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

    protected abstract void addTableItems(T entity, EntityLabelMap componentMap);

    protected void afterLoadPage() { }

    protected void beforeLoadPage() { }

    protected void editEntity(Panel editPanel) {
        this.replaceWith(editPanel);
    }

    protected byte[] generatePDFReport(List<? extends T> list) {
        return null;
    }

    protected EntityColumnInfo getEntityColumInfo(String name) {
        for (EntityColumnInfo entityColumnInfo : columns) {
            if (entityColumnInfo.getProperty().equals(name)) {
                return entityColumnInfo;
            }
        }
        throw new RuntimeException("Nâo foi possível encontrar a coluna " + name);
    }

    protected abstract List<EntityColumnInfo> getEntityColumnInfo();

    protected IModel getListModel() {
        return new LoadableDetachableModel<List<T>>() {
            @Override
            protected List<T> load() {
                 List<T> list = loadList();

                if (entityComparator.getSortingField() != null) {
                    Collections.sort(list, entityComparator);
                }

                if(list == null || list.isEmpty() || !isContentExportableToExcel()) {
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

    protected AbstractXLSExport getXLSExportUtil() {
    	return new XLSExportUtil();
    }

    protected abstract List<T> loadList();

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (listaView.getList() == null || listaView.getList().isEmpty()) {
            lblMsgEmpty = new LabelFrw("msgEmpty", BasePageUtil.getInstance().getString("label.pesquisa.list.empty"));
            lblMsgEmpty.setVisible(true);
        }
    }

    protected void populateTableRow(final ListItem<T> item) {

        final T object = item.getModelObject();

        item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return (item.getIndex() % 2 == 1) ? "linha2" : "linha1";
            }
        }));


        final EntityLabelMap bodyColumnMap = new EntityLabelMap();
        addTableItems(item.getModelObject(), bodyColumnMap);

        ListView<EntityColumnInfo> bodyColumns = new ListView<EntityColumnInfo>("bodyColumns", visibleColumnsModel) {

            @Override
            protected void populateItem(ListItem<EntityColumnInfo> li) {

                LabelMapEntry labelComponent = bodyColumnMap.get(li.getModelObject().property);
                if (labelComponent == null) {
                    throw new RuntimeException("Nâo foi possível encontrar componente de label para propriedade " + li.getModelObject().property);
                }
                li.add(labelComponent.component);

                if (labelComponent.cssClass != null) {
                    li.add(new AttributeModifier("class", true, new Model<String>(labelComponent.cssClass)));
                }

            }
        };
        item.add(bodyColumns);

    }

}
