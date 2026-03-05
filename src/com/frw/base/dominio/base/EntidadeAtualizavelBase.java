/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.dominio.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
