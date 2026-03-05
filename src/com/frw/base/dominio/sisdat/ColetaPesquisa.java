package com.frw.base.dominio.sisdat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "coleta_pesquisa")
public class ColetaPesquisa extends AbstractColeta<ColetaPesquisa> implements  Comparable<ColetaPesquisa> {

	@Column(name = "campo_livre")
	private String campoLivre;

	@ManyToOne
	@JoinColumn(name = "per_id")
	private Pergunta pergunta;

	@ManyToOne
	@JoinColumn(name = "pes_id")
	private Pesquisa pesquisa;


	@ManyToOne
	@JoinColumn(name = "res_id")
	private Resposta resposta;

	@Override
	public int compareTo(ColetaPesquisa o) {
		return (this.campoLivre != null ? campoLivre : "").compareToIgnoreCase(o.getCampoLivre());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColetaPesquisa other = (ColetaPesquisa) obj;
		if (pergunta == null) {
			if (other.pergunta != null)
				return false;
		} else if (!pergunta.equals(other.pergunta))
			return false;
		if (pesquisa == null) {
			if (other.pesquisa != null)
				return false;
		} else if (!pesquisa.equals(other.pesquisa))
			return false;
		return true;
	}

	public String getCampoLivre() {
		return campoLivre;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public Pesquisa getPesquisa() {
		return pesquisa;
	}

	public Resposta getResposta() {
		if(resposta == null)
			resposta = new Resposta();
		return resposta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pergunta == null) ? 0 : pergunta.hashCode());
		result = prime * result + ((pesquisa == null) ? 0 : pesquisa.hashCode());
		return result;
	}

	public void setCampoLivre(String campoLivre) {
		this.campoLivre = campoLivre;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public void setPesquisa(Pesquisa pesquisa) {
		this.pesquisa = pesquisa;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

	@Override
	public String toString() {
		return id != null ?id.toString():"" + " - " + campoLivre != null ? campoLivre : "" +" - "+ resposta!= null ? resposta.getDescricao(): "";
	}
	
	
}
