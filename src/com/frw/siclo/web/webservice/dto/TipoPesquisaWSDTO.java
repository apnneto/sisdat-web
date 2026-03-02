package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TipoPesquisaWSDTO")
public class TipoPesquisaWSDTO {
	
	private boolean ativo;
	private Long codigo;
	private String descricao;
	
	public Long getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
