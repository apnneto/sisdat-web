/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.siclo.web.webservice;

import net.sf.json.JSONObject;

/**
 *
 * @author Maximiliano
 */
public abstract class JSONBase implements JSONSerializable {
    private Long id;
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", getId());
        return obj;
    }

    protected abstract void addJSONObjectProperties(JSONObject json);
}

    