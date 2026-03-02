/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.manutencao.dominio.dto;

import java.io.Serializable;
import java.util.Date;

import com.frw.base.dominio.base.Usuario;

/**
 *
 * @author Marcelo Alves
 */
public class FilterListQuestionarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private Date dataFim;
	private Date dataInicio;
	private String descricao;
	private Usuario usuario;

	public String getCodigo() {
		return codigo;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public String getDescricao() {
		return descricao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
