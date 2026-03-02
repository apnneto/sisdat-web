/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.time.Duration;

import com.frw.base.web.util.BasePageUtil;

/**
 *
 * @author User
 */
public class OnLoadUpdatableModalWindowBehavior extends AbstractAjaxTimerBehavior {

    private String message;
    private UpdatableModalWindow modal;

    public OnLoadUpdatableModalWindowBehavior(Duration updateInterval, String message, UpdatableModalWindow modal) {
        super(updateInterval);
        this.message = message;
        this.modal = modal;
    }

    @Override
    protected void onTimer(AjaxRequestTarget art) {
        stop();
        BasePageUtil.getInstance().ShowAlertMessage(message, art, modal);
        art.addComponent(modal);
    }
}
