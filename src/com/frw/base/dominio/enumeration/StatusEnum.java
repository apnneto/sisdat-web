/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.dominio.enumeration;

/**
 *
 * @author Maximiliano
 */
public enum StatusEnum {
    
    ABERTA(1), BAIXADA(2), NAO_VISITADO(3);

    private Integer id;

    private StatusEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
