/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.base;

import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author Marcelo Alves
 */
public class FrwResourceModel extends ResourceModel {

    private final String key;

    public FrwResourceModel(String resourceKey) {
        super(resourceKey);
        key = resourceKey;
    }

    public FrwResourceModel(String resourceKey, String defaultValue) {
        super(resourceKey, defaultValue);
        key = resourceKey;
    }

    public String getResouceKey() {
        return key;
    }

}
