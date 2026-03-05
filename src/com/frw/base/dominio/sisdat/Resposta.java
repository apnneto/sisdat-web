package com.frw.base.dominio.sisdat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;

@Entity
@Table(name = "resposta")
public class Resposta extends EntidadeDominioBase<Resposta> implements Comparable<Resposta>{

	private static final long serialVersionUID = 1L;

	@Column(name = "correta")
	private Boolean correta = false;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "ordem")
	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "per_id")
	private Pergunta pergunta;
	
	@Column(name = "valor")
	private String valor;

	public Resposta() {
		correta = false;

	}

	public Resposta(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Resposta o) {
		return (this.descricao != null ? this.descricao : "").compareToIgnoreCase(o.getDescricao()) ;
	}

	public Boolean getCorreta() {
		return correta;
	}

	public String getDescricao() {
		if(descricao == null)
			descricao = "";
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public String getValor() {
		return valor;
	}

	public void setCorreta(Boolean correta) {
		this.correta = correta;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return getDescricao();
	}


}
