/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.dominio.enumeration;

/**
 *
 * @author Marcelo Alves
 */
public enum TipoArquivoEnum {

    CSV("CSV"), DOC ("DOC"), JPEG("JPEG"), PDF("PDF"), XLS("XLS");

    private String value;

    private TipoArquivoEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
