package com.frw.siclo.web.webservice.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StatusPesquisaWSDTO")
public class StatusPesquisaWSDTO {

	private String dataSincronizacao;
	private String status;

	public String getDataSincronizacao() {
		return dataSincronizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setDataSincronizacao(String dataSincronizacao) {
		this.dataSincronizacao = dataSincronizacao;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
