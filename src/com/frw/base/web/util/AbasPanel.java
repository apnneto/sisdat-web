package com.frw.base.web.util;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;

/**
 *
 * @author geraldo
 */
public class AbasPanel extends TabbedPanel{

    public AbasPanel(String id, List<ITab> tabs) {
        super(id, tabs);
    }

}
