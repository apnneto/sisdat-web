package com.frw.base.web.pages.base;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.BasePage;

/**
 * @author Carlos Santos
 */
public class ImagePanel extends Panel {

    public ImagePanel(String id, String iconPath) {
        super(id);
        
        NonCachingImage image = new NonCachingImage("image");
        image.setDefaultModel(new Model<ResourceReference>(new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, iconPath)));
        add(image);
    }

}
