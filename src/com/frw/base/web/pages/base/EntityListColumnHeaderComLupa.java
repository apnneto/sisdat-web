/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.base;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;

/**
 *
 * @author Framework
 */
public class EntityListColumnHeaderComLupa  extends DefaultListColumnHeader {

    public EntityListColumnHeaderComLupa(String id, EntityColumnInfo info, AbstractEntityListPanelNew parent) {

        super(id,info,parent);

        IndicatingAjaxLink lookupLink=new IndicatingAjaxLink("lookupLink") {

            @Override
            public void onClick(AjaxRequestTarget art) {
               art.appendJavaScript("alert('Lookup clicado')");
            }
        };

        add(lookupLink);
    }


}
