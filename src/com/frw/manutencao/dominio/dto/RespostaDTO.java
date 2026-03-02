package com.frw.manutencao.dominio.dto;

import java.util.ArrayList;
import java.util.List;

public class RespostaDTO {

	private List<FotoDTO> fotos = new ArrayList<FotoDTO>();
	private List<String> respostas = new ArrayList<String>();
	
	public void addFotos(FotoDTO foto){
		this.fotos.add(foto);
	}

	public void addResposta(String resposta){
		this.respostas.add(resposta);
	}
	public List<FotoDTO> getFotos() {
		return fotos;
	}
	public List<String> getRespostas() {
		return respostas;
	}
	public void setFotos(List<FotoDTO> fotos) {
		this.fotos = fotos;
	}
	public void setRespostas(List<String> respostas) {
		this.respostas = respostas;
	}

}
