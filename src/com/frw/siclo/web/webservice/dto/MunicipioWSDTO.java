package com.frw.siclo.web.webservice.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MunicipioWSDTO")
public class MunicipioWSDTO {

	private String codigo;
	private String descricao;
	private String estado;
	
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getEstado() {
		return estado;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
