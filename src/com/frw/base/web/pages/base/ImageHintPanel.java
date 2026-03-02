/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.frw.base.web.pages.util.ImageHint;

/**
 *
 * @author Marcelo Alves
 */
public class ImageHintPanel extends Panel {

    private ImageHint imageHint;

    public ImageHintPanel(String id, IModel<String> hintModel) {
        super(id);
        setOutputMarkupId(true);
        imageHint = new ImageHint("hint", hintModel);
        add(imageHint);
    }

    public ImageHint getField() {
        return imageHint;
    }

    public void setField(ImageHint field) {
        this.imageHint = field;
    }

}
