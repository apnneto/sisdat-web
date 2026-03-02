package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RotaWSDTO")
public class RotaWSDTO {

	private Long codigo;
	private String descricao;
	private String sigla;
	
	public Long getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getSigla() {
		return sigla;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
