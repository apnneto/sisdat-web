/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.manutencao.dominio.dto;

import com.frw.base.web.pages.seguranca.filter.dto.AbstractFilterDTO;

/**
 * @author Marcos Lisboa
 */
public class FilterPerguntaDescricaoDTO extends AbstractFilterDTO {
	
	private static final long serialVersionUID = 1L;

	private String perguntaDescricao;
	private String perguntaSubDescricao;

	public String getPerguntaDescricao() {
		return perguntaDescricao;
	}

	/**
	 * @return the perguntaSubDescricao
	 */
	public String getPerguntaSubDescricao() {
		return perguntaSubDescricao;
	}

	public void setPerguntaDescricao(String perguntaDescricao) {
		this.perguntaDescricao = perguntaDescricao;
	}

	/**
	 * @param perguntaSubDescricao the perguntaSubDescricao to set
	 */
	public void setPerguntaSubDescricao(String perguntaSubDescricao) {
		this.perguntaSubDescricao = perguntaSubDescricao;
	}
	
}
