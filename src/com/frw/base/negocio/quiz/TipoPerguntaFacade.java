/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.negocio.quiz;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.sisdat.TipoPerguntaDAO;
import com.frw.base.dominio.sisdat.TipoPergunta;

@Stateless
public class TipoPerguntaFacade {
	
	@Inject
	private TipoPerguntaDAO tipoPerguntaDAO;
	

	public List<TipoPergunta> buscarTodosTipoPerguntas() {
		return tipoPerguntaDAO.findAll();
	}
	

}
