/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Framework
 */
public class UpdatableModalWindow extends ModalWindow implements ModalWindow.WindowClosedCallback {

    private class DisableDefaultConfirmBehavior extends AbstractBehavior implements IHeaderContributor {

	        private static final long serialVersionUID = 1L;

	        @Override
	        public void renderHead(IHeaderResponse response)
	        {
	                response.renderOnDomReadyJavascript("Wicket.Window.unloadConfirmation = false");
	        }

	}

    private UpdatableModalWindowPanel contentPanel;

    private WindowClosedCallback windowClosedCallback;

    public UpdatableModalWindow(String id) {
        this(id,null);
    }

    public UpdatableModalWindow(String id, IModel<?> model) {
        super(id,model);
        super.setWindowClosedCallback(this);
        add(new DisableDefaultConfirmBehavior());

    }

    @Override
    public String getContentId() {
        return "content";
    }

    @Override
    public void onClose(AjaxRequestTarget art) {

        if(windowClosedCallback!=null)
            windowClosedCallback.onClose(art);

        setInitialHeight(170);
        setInitialWidth(400);
        setTitle("");
        
    }

    @Override
    public ModalWindow setContent(Component component) {

        if(contentPanel==null || getContent()==null || !getContent().equals(contentPanel)) {
            contentPanel=new UpdatableModalWindowPanel(super.getContentId());
            super.setContent(contentPanel);
        }
       contentPanel.setContent(component);
       return this;
    }

    @Override
    public ModalWindow setWindowClosedCallback(WindowClosedCallback callback) {
        windowClosedCallback=callback;
        return this;
    }

        public void update(AjaxRequestTarget target) {
		    target.addComponent(contentPanel);
		}




}
