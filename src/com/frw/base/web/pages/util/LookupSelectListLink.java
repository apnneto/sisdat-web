/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.BasePage;

/**
 *
 * @author Leoanrdo Barros
 *
 */
public class LookupSelectListLink extends IndicatingAjaxLink {

    private Component componenteList;

    private int height;
    private UpdatableModalWindow modal;
    private String title;
    private int width;

     public LookupSelectListLink(String id, UpdatableModalWindow modal, int height, int witdh , String title, Component component) {
        this(id, modal, height, witdh, title, component, true);
    }

     public LookupSelectListLink(String id, UpdatableModalWindow modal, int height, int witdh , String title, Component component, boolean showLookupImage) {
        super(id);
        this.modal = modal;
        this.height = height;
        this.width = witdh;
        this.componenteList = component;
        this.title = title;

        if(showLookupImage) {
            Image imgLookUp1 = new Image("imgLupa", new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "imagens/iconeLupa.png")));
            add(imgLookUp1);
         }else{
        	 add(new AttributeModifier("class", new Model<String>("icone ver")));
         }
     }

    @Override
    public void onClick(AjaxRequestTarget target) {

         modal.setContent(componenteList);
         modal.setInitialHeight(height);
         modal.setInitialWidth(width);
         modal.setTitle(title);
         modal.show(target);


    }
}
