/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.web.util.LabelFrw;

/**
 * 
 * @author Leo
 */
public abstract class ModalConfirmationPanel extends Panel {

    private LabelFrw messageLabel;
    /**
     *
     * @param modalWindowPage
     * @param window
     */
    protected AjaxLink cancelLink;
    protected AjaxLink confirmLink;

    public ModalConfirmationPanel(String id, String message) {

        super(id);

        add(messageLabel = new LabelFrw("message", new ResourceModel(message)));
        messageLabel.setOutputMarkupId(true);

        add(confirmLink = new AjaxLink("confirm") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (onConfirm(target)) {
                    UpdatableModalWindow.closeCurrent(target);
                }
            }
        });

        cancelLink = (new AjaxLink("cancel") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancel(target);
                UpdatableModalWindow.closeCurrent(target);
            }
        });

        add(cancelLink);


    }

    public AjaxLink getCancelLink() {
        return cancelLink;
    }

    public AjaxLink getConfirmLink() {
        return confirmLink;
    }

    public LabelFrw getMessageLabel() {
        return messageLabel;
    }

    public void setCancelLink(AjaxLink cancelLink) {
        this.cancelLink = cancelLink;
    }

    public void setConfirmLink(AjaxLink confirmLink) {
        this.confirmLink = confirmLink;
    }

    public void setMessage(String message) {
        messageLabel.setDefaultModel(new ResourceModel(message));
    }

    public void setMessageLabel(LabelFrw messageLabel) {
        this.messageLabel = messageLabel;
    }

    protected void onCancel(AjaxRequestTarget target) {
    }

    protected boolean onConfirm(AjaxRequestTarget target) {
        return true;
    }
}
