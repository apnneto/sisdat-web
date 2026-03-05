package com.frw.siclo.web.webservice.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RespostaWSDTO")
public class RespostaWSDTO {

	private Long codigo;
	private Long codigoPergunta;
	private Boolean correta;
	private String descricao;
	private Integer ordem;
	private String valor;

	public Long getCodigo() {
		return codigo;
	}

	public Long getCodigoPergunta() {
		return codigoPergunta;
	}

	public Boolean getCorreta() {
		return correta;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public String getValor() {
		return valor;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setCodigoPergunta(Long codigoPergunta) {
		this.codigoPergunta = codigoPergunta;
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

	public void setValor(String valor) {
		this.valor = valor;
	}

}
