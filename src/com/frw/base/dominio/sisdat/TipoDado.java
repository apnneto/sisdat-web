package com.frw.base.dominio.sisdat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.frw.base.dominio.base.EntidadeBase;

@Entity
@Table(name="tipo_dado")
public class TipoDado extends EntidadeBase implements Comparable<TipoDado> {

	private static final long serialVersionUID = 2193056979719443278L;
	
	@Column(name = "descricao")
	private String descricao;

	@Override
	public int compareTo(TipoDado o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
