package com.frw.siclo.web.webservice.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PortaWSDTO")
public class PortaWSDTO {
	
	private Long codigo;
	private String descricao;
	
	public Long getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
