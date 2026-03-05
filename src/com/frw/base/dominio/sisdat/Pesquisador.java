package com.frw.base.dominio.sisdat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.dominio.base.Usuario;

/**
 * @author Marcos Lisbos
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pesquisador")
public class Pesquisador extends EntidadeDominioBase<Pesquisador> implements Comparable<Pesquisador> {

	@Column(name = "nome")
	private String nome;


	@ManyToOne
	@JoinColumn(name = "fk_usuario")
	private Usuario usuario;
	
	public Pesquisador() {
	}


	@Override
	public int compareTo(Pesquisador o) {
		return (this.nome != null? this.nome.toLowerCase(): "").compareTo(o.nome != null? o.nome.toLowerCase(): "");
	}


	public String getNome() {
		return nome;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

	
}
