/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author Leo
 */
public class LabelFrw extends Label {

    public LabelFrw(String id) {
        super(id);
    }

    public LabelFrw(String id, IModel<?> model) {
        super(id, model);
    }
    
    public LabelFrw(String id, String label) {
        super(id, label);
    }

    public LabelFrw(String id, String label, String css) {
        super(id, label);
        this.add(new AttributeModifier("class", new Model<String>(css)));
    }
    
    
}
