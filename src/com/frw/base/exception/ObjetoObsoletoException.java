/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.exception;

import com.frw.base.dominio.base.Entidade;

/**
 *
 * @author Marcelo Alves
 */
public class ObjetoObsoletoException extends RuntimeException {

    private Entidade objetoObsoleto;

    public ObjetoObsoletoException(String message, Entidade objeto) {
        super(message);
        this.objetoObsoleto = objeto;
    }

    public Entidade getObjetoObsoleto() {
        return objetoObsoleto;
    }

}
