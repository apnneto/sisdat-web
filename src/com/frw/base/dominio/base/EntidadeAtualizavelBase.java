/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.dominio.base;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author Marcelo Alves
 */
@MappedSuperclass
public class EntidadeAtualizavelBase extends EntidadeBase {
    
	private static final long serialVersionUID = 1L;

	@Column(name = "data_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
