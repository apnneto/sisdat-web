/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.BasePage;

/**
 *
 * @author Marcelo Alves
 */
public class ImageHint extends Image {

    IModel<String> hintModel;

    public ImageHint(String id, IModel<String> hintModel) {
        super(id);
        this.hintModel = hintModel;
        configHint();
    }

    private void configHint() {
        if(hintModel != null && hintModel.getObject() != null) {
            this.add(new AttributeModifier("title", true, hintModel));
        }
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if(hintModel == null || hintModel.getObject() == null) {
            this.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/anatation_32.png")));
        } else {
            this.setDefaultModel(new Model<ResourceReference>(new ResourceReference(BasePage.class, "imagens/anotation_blue_32.png")));
        }

    }
}
