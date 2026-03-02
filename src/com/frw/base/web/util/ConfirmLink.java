/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;

abstract public class ConfirmLink extends IndicatingAjaxLink {


    public ConfirmLink(String id, String msg) {
           super(id);
           add(new SimpleAttributeModifier("onclick",msg));
      }

      @Override
      abstract  public void onClick(AjaxRequestTarget target);
}
