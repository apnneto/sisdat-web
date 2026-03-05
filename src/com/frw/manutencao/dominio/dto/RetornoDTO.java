package com.frw.manutencao.dominio.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RetornoDTO")
public class RetornoDTO {

	private List<RespostaDTO> listaRespostas = new ArrayList<RespostaDTO>();
	private List<String> perguntas = new ArrayList<String>();

	public void addPerguntas(String pergunta) {
		this.perguntas.add(pergunta);
	}

	public void addRespostas(RespostaDTO dto) {
		this.listaRespostas.add(dto);
	}

	public List<RespostaDTO> getListaRespostas() {
		return listaRespostas;
	}

	public List<String> getPerguntas() {
		return perguntas;
	}

	public List<RespostaDTO> getRespostas() {
		return listaRespostas;
	}

	public void setListaRespostas(List<RespostaDTO> listaRespostas) {
		this.listaRespostas = listaRespostas;
	}

	public void setPerguntas(List<String> perguntas) {
		this.perguntas = perguntas;
	}

	public void setRespostas(List<RespostaDTO> respostas) {
		this.listaRespostas = respostas;
	}
}
