package com.frw.base.dominio.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Carlos Santos
 * @author Miller
 */
@MappedSuperclass
public abstract class EntidadeDominioBase<T> extends EntidadeBase implements Comparable<T>{

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Data_Alteracao")
    private Date dataAlteracao;

    @Column(name = "Excluido")
    private Boolean excluido = false;

    @Column(name = "Usuario")
    private String usuarioAlteracao;

    public Date getDataAlteracao() {
    	if(dataAlteracao == null)
    		dataAlteracao = new Date();
        return dataAlteracao;
    }

    public String getUsuarioAlteracao() {
    	if(usuarioAlteracao == null)
    		usuarioAlteracao = "system";
        return usuarioAlteracao;
    }

    public Boolean isExcluido() {
    	
    	if(excluido == null)
    		excluido = false;
    	
        return excluido;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public void setUsuarioAlteracao(String usuario) {
        this.usuarioAlteracao = usuario;
    }

}
