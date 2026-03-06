package com.frw.base.web.pages.base;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.exception.SistemaException;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.ShowAnexoPage;
import com.frw.base.web.pages.util.ModalAlertPanel;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.frw.base.web.pages.util.MyPagingNavigator;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.SistemaConstants;

/**
 *
 * @author juliano
 */
public abstract class AbstractEntityListPage<T extends EntidadeBase> extends BasePage {

    protected class EntityListView extends PageableListView<T> {

        public EntityListView(String id, IModel<? extends List<? extends T>> model, int rowsPerPage) {
            super(id, (IModel)model, (long)rowsPerPage);




        }

        @Override
        protected void populateItem(final ListItem<T> item) {

            beforePopulateItem(item);

            final T object = item.getModelObject();
            //final Page editPage = getEditPage(object);

            lblMsgEmpty.setVisible(false);

            Link editLink = new Link("editLink") {

                @Override
                public void onClick() {
                    setResponsePage(getEditPage(object));
                }
            };
            item.add(editLink);
            editImg = new NonCachingImage("editImg");
            editImg.add(new AttributeModifier("title", new Model(object.getId() == null ? " - " : object.getId().toString())));
            editLink.add(editImg);

            //editLink.add(new LabelFrw("codigo", object.getId().toString()));

            editLink.setEnabled(enableEditLink && getEditPageClass() != null);
            editImg.setVisible(object.getId() != null);

            item.add(new AttributeModifier("class",
                org.apache.wicket.model.LambdaModel.of(() -> (item.getIndex() % 2 == 1) ? "linha2" : "linha1")));

            addTableItems(item);


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
                                if(root == null)
                                    root = e;

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

                    //confirmationModal.update(target);
                    //confirmationModal.setCookieName(confirmationModal.getContentId());
                    confirmationModal.show(target);
                }
            };
            // image loaded as resource ref via model.
            Image image = new Image("removeImage", new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/iconeFecharMenu.png")));
            removeLink.add(image);

            removeLink.setVisible(enableDeleteLink && getEditPageClass() != null);

            colunaAcao.add(removeLink);

            colunaAcao.setVisible(enableColunaAcao);

            afterPopulateItem(item, removeLink, editLink);

            if (editLink.isEnabled()) {
                editImg.setDefaultModel(new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/page_white_edit.png")));
            } else {
                editImg.setDefaultModel(new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/page_white_edit_disabled.png")));
            }



        }
    }
    protected WebMarkupContainer colunaAcao;
    protected boolean enableColunaAcao = true;
    protected boolean enableDeleteLink = true;
    /** flag para controlar  a exibição dos links de edição e exclusão da listagem */
    protected boolean enableEditLink = true;
    protected Link exportarPDFLink;
    protected FeedbackPanel feedback;
    protected WebMarkupContainer filtersContainer;
    protected WebMarkupContainer footerContainer;
    final protected LabelFrw lblMsgEmpty;
    protected EntityListView listaView;
    protected WebMarkupContainer listContainer;
    //protected LoadableDetachableModel<List<T>> model;
    protected IModel model;

    protected Link newLink;

    NonCachingImage editImg;

    public AbstractEntityListPage() {
        beforeLoadPage();

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        newLink = new Link("editPageLink") {

            @Override
            public void onClick() {
                setResponsePage(getEditPageClass());
            }
        };
        newLink.add(new LabelFrw("caption", new Model(getNewEntiyLinkLabel())));
        add(newLink);

        exportarPDFLink = new Link("btnExportarPDF") {

            @Override
            public void onClick() {
                byte[] bytes  = generatePDFReport(listaView.getList());
                { jakarta.servlet.http.HttpServletResponse r = (jakarta.servlet.http.HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse(); try { r.setHeader("Content-Disposition","attachment;filename="+ "parcerias_grid_export.pdf"); r.setHeader("Content-Type","application/octet-stream"); r.getOutputStream().write(bytes); r.flushBuffer(); } catch(Exception _ex){} }
            }
        };
        exportarPDFLink.setVisible(Boolean.FALSE);
        exportarPDFLink.setOutputMarkupId(true);

        Image imgPDF = new Image("downloadImagePDF", new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/pdf.png")));
        exportarPDFLink.add(imgPDF);

        Fragment fragment = new Fragment("botoesContainer", "botoesContainerAdded", this);
        add(fragment);

        addFragmentAditionalButtons(fragment);

        model = new LoadableDetachableModel<List<T>>() {
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


        lblMsgEmpty = new LabelFrw("msgEmpty", getString("label.pesquisa.list.empty"));
        lblMsgEmpty.setVisible(true);

        listaView = new EntityListView("list", model, getListRowsPerPage());
       

        /* add empty footer container */
        footerContainer = getFooterListPanel();
        footerContainer.setOutputMarkupId(true);

        /* create list container */
        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(listaView);
        listContainer.add(exportarPDFLink);
        MyPagingNavigator navigator = new MyPagingNavigator("navigator", listaView);
        navigator.setOutputMarkupId(true);
        navigator.setVisible(showPagingNavigator());
        listContainer.add(navigator);
        listContainer.add(getTableHeader());
        listContainer.add(lblMsgEmpty);
        listContainer.add(footerContainer);

         add(listContainer);

        afterLoadPage();
    }

    public void afterPopulateItem(ListItem<T> item, AbstractLink removeLink, AbstractLink editLink) {
    }

    public void beforePopulateItem(ListItem<T> item) {
    }

    public WebMarkupContainer getFooterListPanel() {
        return new WebMarkupContainer("footerPanel");
    }

    public int getListRowsPerPage() {
        return SistemaConstants.ROWS_PER_PAGE;
    }


    public WebMarkupContainer getTableHeader() {
        return new Fragment("header", "headerRow", this);
    }


    public boolean isContentExportableToPDF() {
        return false;
    }

    protected void addFragmentAditionalButtons(Fragment fragment) {
    }

    protected abstract void addTableItems(ListItem<T> item);

    protected void afterLoadPage() {
    }

    protected void beforeLoadPage() {
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

    protected String getNewEntiyLinkLabel() {
        return getString("novo.generico");
    }

    protected abstract List<T> loadList();

    protected void removeEntity(T entity, AjaxRequestTarget target) throws SistemaException {
    }

    protected boolean showPagingNavigator() {
        return Boolean.TRUE;
    }

    protected boolean showScrollBar(){
        return false;
    }

}
