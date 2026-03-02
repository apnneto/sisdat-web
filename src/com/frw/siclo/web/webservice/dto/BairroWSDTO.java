package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BairroWSDTO")
public class BairroWSDTO {
	
	private String codigo;
	private String codigoMunicipio;
	private String descricao;
	
	public String getCodigo() {
		return codigo;
	}
	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
