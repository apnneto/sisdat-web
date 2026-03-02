/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.web.util.LabelFrw;

/**
 * 
 * @author Leo
 */
public class ModalAlertPanel extends Panel {

    /**
     *
     * @param modalWindowPage
     * @param window
     */

  
    public   ModalAlertPanel(String id,String message)
    {

        super(id);

        LabelFrw messageLabel = new LabelFrw("message", message);
        messageLabel.setEscapeModelStrings(false);

        add(messageLabel);

         add(new AjaxLink("confirm") {
	      @Override
	      public void onClick(AjaxRequestTarget target) {
	       
                ModalWindow.closeCurrent(target);
	      }
         });

      

    }
    
   
   
}