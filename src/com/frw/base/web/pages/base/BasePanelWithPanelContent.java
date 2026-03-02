package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Carlos Santos
 */
public class BasePanelWithPanelContent extends Panel {

    protected WebMarkupContainer footerContainer;
    protected WebMarkupContainer headerContainer;

    public BasePanelWithPanelContent(String id) {
        super(id);
        
        /* add header */
        this.headerContainer = new WebMarkupContainer("header");
        add(this.headerContainer);

        /* add footer */
        this.footerContainer = new WebMarkupContainer("footer");
        add(this.footerContainer);
    }

}
