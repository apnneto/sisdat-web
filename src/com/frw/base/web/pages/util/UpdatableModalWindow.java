/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;

/**
 * Wicket 10 bridge for the old ModalWindow-based UpdatableModalWindow.
 * Extends ModalDialog (the Wicket 10 replacement for ModalWindow).
 *
 * Bridge methods retained for source compatibility:
 *   show(target)            → open(target)
 *   setInitialHeight(int)   → no-op
 *   setInitialWidth(int)    → no-op
 *   setTitle(String)        → no-op
 *   setWindowClosedCallback → stored, called on close()
 *   update(target)          → target.add(contentPanel)
 *   closeCurrent(target)    → static helper via findParent
 */
public class UpdatableModalWindow extends ModalDialog {

    private static final long serialVersionUID = 1L;

    /** Drop-in replacement for ModalWindow.WindowClosedCallback */
    public interface WindowClosedCallback {
        void onClose(AjaxRequestTarget target);
    }

    private class DisableDefaultConfirmBehavior extends Behavior {
        private static final long serialVersionUID = 1L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            response.render(OnDomReadyHeaderItem.forScript(
                    "Wicket.Window.unloadConfirmation = false;"));
        }
    }

    private UpdatableModalWindowPanel contentPanel;
    private WindowClosedCallback windowClosedCallback;

    public UpdatableModalWindow(String id) {
        this(id, null);
    }

    public UpdatableModalWindow(String id, IModel<?> model) {
        super(id);
        add(new DisableDefaultConfirmBehavior());
    }

    // ── Bridge: show(target) → open(target) ──────────────────────────────────
    public void show(AjaxRequestTarget target) {
        open(target);
    }

    /** Override close() to fire the WindowClosedCallback */
    @Override
    public ModalDialog close(AjaxRequestTarget target) {
        ModalDialog result = super.close(target);
        if (windowClosedCallback != null) {
            windowClosedCallback.onClose(target);
        }
        return result;
    }

    // ── Static helper: replacement for ModalWindow.closeCurrent(target) ──────
    /**
     * Closes the nearest UpdatableModalWindow ancestor of the component
     * that triggered the current request.  Call from inside a panel that
     * lives inside an UpdatableModalWindow, just like ModalWindow.closeCurrent().
     */
    public static void closeCurrent(AjaxRequestTarget target) {
        target.getPage().visitChildren(UpdatableModalWindow.class,
            (UpdatableModalWindow modal, org.apache.wicket.util.visit.IVisit<Void> visit) -> {
                if (modal.isOpen()) {
                    modal.close(target);
                    visit.stop();
                }
            });
    }

    // ── Bridge: size / title setters – no-op in Wicket 10 ────────────────────
    public UpdatableModalWindow setInitialHeight(int height) { return this; }
    public UpdatableModalWindow setInitialWidth(int width)   { return this; }
    public UpdatableModalWindow setTitle(String title)       { return this; }

    /** Bridge for callers that still do: new SomePanel(modal.getContentId()) */
    public String getContentId() { return CONTENT_ID; }

    // ── Content management ────────────────────────────────────────────────────
    @Override
    public void setContent(Component component) {
        if (contentPanel == null) {
            contentPanel = new UpdatableModalWindowPanel(CONTENT_ID);
            super.setContent(contentPanel);
        }
        contentPanel.setContent(component);
    }

    /** Replaces ModalWindow.setWindowClosedCallback */
    public UpdatableModalWindow setWindowClosedCallback(WindowClosedCallback callback) {
        this.windowClosedCallback = callback;
        return this;
    }

    public void update(AjaxRequestTarget target) {
        if (contentPanel != null) {
            target.add(contentPanel);
        }
    }
}