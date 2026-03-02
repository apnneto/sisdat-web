/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.util;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author Framework
 */
public class UpdatableModalWindowPanel extends Panel {

    private Component content;
    public UpdatableModalWindowPanel(String id) {
        super(id);
        content=new WebMarkupContainer("content");
        add(content);

    }

    public void setContent(Component c) {
        content.replaceWith(c);
        content=c;
    }
}
