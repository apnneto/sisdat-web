package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="LogradouroWSDTO")
public class LogradouroWSDTO {

	private Long codigo;
	private String codigoBairro;
	private String descricao;
	private String tipo;
	
	public Long getCodigo() {
		return codigo;
	}
	public String getCodigoBairro() {
		return codigoBairro;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setCodigoBairro(String codigoBairro) {
		this.codigoBairro = codigoBairro;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
