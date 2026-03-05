package com.frw.base.dominio.sisdat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.frw.base.dominio.base.EntidadeBase;

@Entity
@Table(name = "tipo_pergunta")
public class TipoPergunta extends EntidadeBase implements Comparable<TipoPergunta> {

	private static final long serialVersionUID = 1L;

	@Column(name = "ativo")
	private Boolean ativo;

	@Column(name = "descricao")
	private String descricao;

	@Override
	public int compareTo(TipoPergunta o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return getDescricao();
	}

}
