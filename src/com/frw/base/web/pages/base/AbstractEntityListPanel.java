package com.frw.base.web.pages.base;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

import com.frw.base.dominio.base.AnexoWeb;
import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.exception.ForeignKeyViolationException;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.ShowAnexoPage;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.frw.base.web.pages.util.MyPagingNavigator;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.pages.util.UploadFilePanel;
import com.frw.base.web.util.BasePageUtil;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.SistemaConstants;

/**
 * @author Carlos Henrique
 */
public abstract class AbstractEntityListPanel<T extends EntidadeBase> extends Panel {

    protected class EntityListView extends PageableListView<T> {

        public EntityListView(String id, IModel<? extends List<? extends T>> model, int rowsPerPage) {
            super(id, model, rowsPerPage);
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();
            if (listaView.getModelObject() == null || listaView.getModelObject().isEmpty()) {
                lblMsgEmpty.setVisible(true);
            } else {
                lblMsgEmpty.setVisible(false);
            }
        }

        @Override
        protected void populateItem(final ListItem<T> item) {

            beforePopulateItem(item);

            final T object = item.getModelObject();

            /** edit link */
            AjaxLink editLink = new AjaxLink("editLink") {

                @Override
                public void onClick(AjaxRequestTarget target) {
                    Panel panel = getEditPanel(object, target);
                    if (panel != null) {
                        panel.setOutputMarkupId(true);
                        editEntity(panel);
                        target.addComponent(panel);
                    }
                }
            };
            item.add(editLink);
            
            editLink.add(new AttributeModifier("title", true, new Model(object.getId() == null ? " - " : object.getId().toString())));
            
            colunaAcao = new WebMarkupContainer("colunaAcao");
            item.add(colunaAcao);

            /** add other table columns */
            addTableItems(item);

            /** create link for deleting table entry */
            AjaxLink removeLink = new AjaxLink("removeLink") {

                @Override
                public void onClick(final AjaxRequestTarget target) {

                    target.appendJavascript("Wicket.Window.unloadConfirmation=false");
                    confirmationModal.setContent(new ModalConfirmationPanel(confirmationModal.getContentId(), "message.confirmation.delete") {

                        @Override
                        protected boolean onConfirm(AjaxRequestTarget target) {
                            try {
                                removeEntity(item.getModelObject(), target);
                            } catch (RuntimeException e) {
                                Throwable root = ExceptionUtils.getRootCause(e);
                                if(root == null)
                                    root = e;
                                
                                if(root instanceof ForeignKeyViolationException) {
                                    error(getString(root.getMessage()));
                                    target.addComponent(feedback);
                                    ModalWindow.closeCurrent(target);
                                    return false;
                                }
                                throw e;
                            }
                            EntityListView.this.getModel().detach();
                            target.addComponent(listContainer);
                            return true;
                        }
                    });
                    confirmationModal.show(target);

                }
            };
            

            colunaAcao.add(removeLink);

           afterPopulateItem(item, removeLink, editLink);

            if(editLink.isEnabled()){
               // editImg.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/page_white_edit.png")));
            }else{
             //   editImg.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/page_white_edit_disabled.png")));
            }

            colunaAcao.setVisible(removeLink.isVisible());
        }
    }
    protected WebMarkupContainer colunaAcao;
    protected UpdatableModalWindow confirmationModal;
    protected Link exportarPDFLink;
    protected FeedbackPanel feedback;
    protected WebMarkupContainer filtersContainer;
    protected WebMarkupContainer footerContainer; 
    final protected LabelFrw lblMsgEmpty;
    protected EntityListView listaView;
    protected WebMarkupContainer listContainer;
    

    protected AjaxLink newLink;

    protected UploadFilePanel uploadFilePanel;

    public AbstractEntityListPanel(String id, UpdatableModalWindow confirmationModal, final T entity) {

        super(id);
        this.confirmationModal = confirmationModal;
        setOutputMarkupId(true);
        beforeLoadPage();


        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        newLink = new AjaxLink("editPanelLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                Panel panel = getEditPanel(entity, target);
                if (panel != null) {
                    panel.setOutputMarkupId(true);
                    editEntity(panel);
                    target.addComponent(panel);
                }
            }
        };

        newLink.add(new LabelFrw("caption", new Model(BasePageUtil.getInstance().getString(getNewEntiyLinkLabel()))));
        add(newLink);

        exportarPDFLink = new Link("btnExportarPDF") {

            @Override
            public void onClick() {
                byte[] bytes  = generatePDFReport(listaView.getList());
                RequestCycle.get().setRequestTarget( new ShowAnexoPage(bytes, "parcerias_grid_export.pdf"));
            }
        };
        exportarPDFLink.setVisible(Boolean.FALSE);
        exportarPDFLink.setOutputMarkupId(true);

        Image imgPDF = new Image("downloadImagePDF", new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/pdf.png")));
        exportarPDFLink.add(imgPDF);

        /** create detachable model for table */
        LoadableDetachableModel<List<T>> model = new LoadableDetachableModel<List<T>>() {

            @Override
            protected List<T> load() {
                List<T> list = loadList();
                if(list == null || list.isEmpty() || !isContentExportableToPDF()) {
                    exportarPDFLink.setVisible(false);
                } else {
                    exportarPDFLink.setVisible(true);
                }
                return list;
            }
        };

        /** add search filter fields */
        filtersContainer = new WebMarkupContainer("searchFilters");
        add(filtersContainer);

        lblMsgEmpty = new LabelFrw("msgEmpty", BasePageUtil.getInstance().getString("label.pesquisa.list.empty"));
        lblMsgEmpty.setVisible(true);
        lblMsgEmpty.setOutputMarkupId(true);



        /** create panel's entity list */
        listaView = new EntityListView("list", model, getListRowsPerPage());
        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(exportarPDFLink);
        listContainer.add(listaView);
        MyPagingNavigator navigator = new MyPagingNavigator("navigator", listaView);
        navigator.setOutputMarkupId(true);
        navigator.setVisible(showPagingNavigator());
        listContainer.add(navigator);
        listContainer.add(getTableHeader());
        listContainer.add(lblMsgEmpty);
        add(listContainer);

        /* add empty footer container */
        footerContainer = getTableFooter();
        listContainer.add(footerContainer);

        uploadFilePanel = createUploadFile(listContainer);
        add(uploadFilePanel);

        afterLoadPage();

    }

    public void afterPopulateItem(ListItem<T> item, AbstractLink removeLink, AbstractLink editLink) {
    }

    public void beforePopulateItem(ListItem<T> item) {
    }

    public int getListRowsPerPage() {
        return SistemaConstants.ROWS_PER_PAGE;
    }

    public WebMarkupContainer getTableFooter() {
        return new WebMarkupContainer("footerPanel");
    }

    public WebMarkupContainer getTableHeader() {
        return new Fragment("header", "headerRow", this);
    }

    public Usuario getUsuarioLogado() {
        SistemaSession session = (SistemaSession) Session.get();
        return session.getUsuarioLogado();
    }

    public boolean isContentExportableToPDF() {
        return false;
    }

    protected abstract void addTableItems(ListItem<T> item);

    protected void afterLoadPage() {
    }

    protected void beforeLoadPage() {
    }

    protected UploadFilePanel createUploadFile(WebMarkupContainer container) {
        UploadFilePanel uploadPanel = new UploadFilePanel("uploadFile", container, getMaxUploadSize(), new Model("Upload")) {

            @Override
            public void afterUpload(AjaxRequestTarget target) {
                if (getAnexoFile() != null) {
                    uploadFileEvent(getAnexoFile(), target);
                }
            }
        };
        uploadPanel.setVisible(false);

        return uploadPanel;
    }

    protected void editEntity(Panel editPanel) {
        this.replaceWith(editPanel);
    }

    protected byte[] generatePDFReport(List<? extends T> list) {
        return null;
    }

    protected Panel getEditPanel(T entity, AjaxRequestTarget target) {
        return null;
    }

    protected Bytes getMaxUploadSize() {
        return Bytes.kilobytes(1024);
    }

    protected String getNewEntiyLinkLabel() {
        return "novo.generico";
    }

    protected abstract List<T> loadList();

    protected void removeEntity(T entity, AjaxRequestTarget target) {
    }

    protected boolean showPagingNavigator() {
        return Boolean.TRUE;
    }

    protected void uploadFileEvent(AnexoWeb anexoWeb, AjaxRequestTarget target) {
    }
}
