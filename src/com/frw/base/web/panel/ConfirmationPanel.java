/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.panel;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Leo
 */


public abstract class ConfirmationPanel extends Panel {
     public ConfirmationPanel(String id, String message) {
         super(id);
	 add(new LabelFrw("message", message));
	 add(new Link("confirm") {
	      @Override
	      public void onClick() {
	        onConfirm();
	      }
	    });
	 add(new Link("cancel") {
	      @Override
	      public void onClick() {
	        onCancel();
	      }
	    });
     }
     protected abstract void onCancel();
     protected abstract void onConfirm();
}
