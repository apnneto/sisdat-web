/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;

abstract public class ConfirmLink extends IndicatingAjaxLink<Void> {

    public ConfirmLink(String id, String msg) {
        super(id);
        add(new Label("confirmScript").setVisible(false));
        add(org.apache.wicket.AttributeModifier.replace("onclick", msg));
    }

    @Override
    abstract public void onClick(AjaxRequestTarget target);
}