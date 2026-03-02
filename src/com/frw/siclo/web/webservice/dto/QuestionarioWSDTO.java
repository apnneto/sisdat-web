package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.frw.base.dominio.base.EntidadeDominioBase;

@XmlRootElement(name = "QuestionarioWSDTO")
public class QuestionarioWSDTO extends EntidadeDominioBase<QuestionarioWSDTO> {

	private String codigo;
	private String descricao;
	private Integer ordem;
	private String orientacao;
	private String resumo;
	private String versao;


	@Override
	public int compareTo(QuestionarioWSDTO o) {
		return (this.descricao != null ? this.descricao : "").compareToIgnoreCase(o.getDescricao());
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public String getOrientacao() {
		return orientacao;
	}

	public String getResumo() {
		return resumo;
	}

	public String getVersao() {
		return versao;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

}
