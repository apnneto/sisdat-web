package com.frw.base.web.pages.util;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.panel.TextAreaPanel;

/**
 *
 * @author Leo
 */
public class ObservacaoModalAjaxLink extends AjaxLink {

    protected UpdatableModalWindow confirmationModal;
    protected NonCachingImage image;
    protected MarkupContainer updateContainer;

    public ObservacaoModalAjaxLink(String id, PropertyModel<String> observacaoModel, UpdatableModalWindow modalWindow) {
        this(id,observacaoModel,modalWindow, null);
    }

    public ObservacaoModalAjaxLink(String id, PropertyModel<String> observacaoModel, UpdatableModalWindow modalWindow, WebMarkupContainer markupContainer) {
        super(id, observacaoModel);
        this.confirmationModal = modalWindow;
        this.updateContainer = markupContainer;

        image = new NonCachingImage("obsImage");
        image.setOutputMarkupId(true);
        add(image);

    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        confirmationModal.setContent(new TextAreaPanel(confirmationModal.getContentId(), getDefaultModel(),updateContainer));
        confirmationModal.setInitialHeight(220);
        confirmationModal.setInitialWidth(420);
        confirmationModal.show(target);

    }



    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (getModel().getObject() == null || getModel().getObject().toString().isEmpty()) {
            image.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/anatation_32.png")));
        } else {
            image.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/anotation_blue_32.png")));
        }

    }
}
