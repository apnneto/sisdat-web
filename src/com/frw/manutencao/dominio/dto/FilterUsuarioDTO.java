/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.manutencao.dominio.dto;

import java.io.Serializable;
import java.util.Date;

import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.sisdat.Empresa;

/**
 * 
 * @author Marcelo Alves
 */
public class FilterUsuarioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Date dataAtualizacao;
	private Empresa empresa;
	private String login;

	private String nome;

	private TipoUsuario tipoUsuarioLogado;

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public String getLogin() {
		return login;
	}

	public String getNome() {
		return nome;
	}

	public TipoUsuario getTipoUsuarioLogado() {
		return tipoUsuarioLogado;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTipoUsuarioLogado(TipoUsuario tipoUsuarioLogado) {
		this.tipoUsuarioLogado = tipoUsuarioLogado;
	}

}
