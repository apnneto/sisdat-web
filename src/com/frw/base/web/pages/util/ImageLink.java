package com.frw.base.web.pages.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.BasePage;

/*
 * @author Carlos Santos
 */
public class ImageLink extends Panel {

    private boolean downloadLink;
    private Image image;
    private AbstractLink link;
    private Panel panel;

    public ImageLink(String id, final BasePage page, final String iconPath) {
        super(id);

        link = new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                setResponsePage(page);
            }
        };
        add(link);
        image = createImage(iconPath);
        link.add(image);

    }

    public ImageLink(String id, final String iconPath, final String titleKey) {
        super(id);
        setOutputMarkupId(true);

        link = new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                onClickAction(art);
            }
        };
        link.add( AttributeModifier.replace("title", titleKey));
        add(link);
        image = createImage(iconPath);
        link.add(image);


    }

    public ImageLink(String id, final UpdatableModalWindow confirmationModal, Panel panel, final int width, final int height, final String titleKey, String iconPath) {
        super(id);
        this.panel = panel;
        
        link = new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                openModalWindow(getString(titleKey), getPanel(), width, height, confirmationModal, target);
            }
        };

        link.add( AttributeModifier.replace("title", getString(titleKey)));

        add(link);
        image = createImage(iconPath);
        link.add(image);
    }


    public Image createImage(String iconPath) {
        return new Image("image", new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, iconPath)));
    }

    public AbstractLink getLink() {
        return link;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
    private void openModalWindow(String titleKey, Panel panel, int width, int height, UpdatableModalWindow confirmationModal, AjaxRequestTarget target) {
        onOpenEvent(panel, target);
        confirmationModal.setTitle(titleKey);
        confirmationModal.setContent(panel);
        confirmationModal.setInitialWidth(width);
        confirmationModal.setInitialHeight(height);
        confirmationModal.show(target);
        confirmationModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget art) {
                onCloseEvent(art);
            }
        });
    }

    protected void onClickAction(AjaxRequestTarget art) { }

    protected void onCloseEvent(AjaxRequestTarget art) { }

    protected void onOpenEvent(Panel panel, AjaxRequestTarget art) { }

}