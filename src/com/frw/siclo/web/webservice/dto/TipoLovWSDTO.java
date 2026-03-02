package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TipoLovWSDTO")
public class TipoLovWSDTO {
	
	private Long codigo;
	private String descricao;
	
	/* -------------------- */
	
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
