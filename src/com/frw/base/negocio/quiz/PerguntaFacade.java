/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.negocio.quiz;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.sisdat.PerguntaDAO;
import com.frw.base.dao.sisdat.TipoLovDAO;
import com.frw.base.dao.sisdat.TipoPerguntaDAO;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.TipoLov;
import com.frw.base.dominio.sisdat.TipoPergunta;
import com.frw.base.web.SistemaSession;
import com.frw.manutencao.dominio.dto.FilterPerguntaDescricaoDTO;

@Stateless
public class PerguntaFacade {
	
	@Inject
	private PerguntaDAO perguntaDAO;
	
	@Inject
	private TipoLovDAO tipoLovDAO;
	
	@Inject
	private TipoPerguntaDAO tipoPerguntaDAO;
	
	public List<Pergunta> buscarPerguntasDependentes(Questionario questionario, Pergunta pergunta) {
		return perguntaDAO.buscarPerguntasDependentes(questionario, pergunta);
	}
	
	public List<Pergunta> buscarPerguntasPorQuestionario(Questionario questionario) {
		return perguntaDAO.buscarPerguntasPorQuestionario(questionario);
	}

	public List<Pergunta> buscarPerguntasPorQuestionarioFilter(Questionario questionario, FilterPerguntaDescricaoDTO filterDTO) {
		return perguntaDAO.buscarPerguntasPorQuestionarioFilter(questionario.getId(), filterDTO);
	}

    public Pergunta buscarPorDescricaoCompleta(String descricao) {
		return perguntaDAO.buscarPorDescricaoCompleta(descricao);
	}

	public Pergunta buscarPorId(Long id) {
		return perguntaDAO.findById(id);
	}
	
	public List<Pergunta> buscarTodasPerguntas() {
		return perguntaDAO.findAll();
	}

	public List<TipoLov> buscarTodosTipoLov() {
		return tipoLovDAO.findAll();
	}
	
	public List<TipoPergunta> buscarTodosTipoPerguntas() {
		return tipoPerguntaDAO.findAll();
	}
	
	public void excluirPergunta(Pergunta pergunta) {
        pergunta.setExcluido(true);
        perguntaDAO.saveOrUpdate(pergunta);
    }
	
	public List<String> pesquisarAutoComplete(String input) {
		return perguntaDAO.buscarAutoComplete(input);
	}

	public Pergunta salvarPergunta(Pergunta pergunta) {
        return perguntaDAO.saveOrUpdate(pergunta);
    }

	public Pergunta salvarPerguntaComPalavrasChaves(Pergunta entity) {
		SistemaSession.setUserAndCurrentDate(entity);
		return perguntaDAO.saveOrUpdate(entity);
	}

	public Integer ultimoIdentificadorOrdem(Pergunta pergunta) {
		return perguntaDAO.ultimoIdentificadorOrdem(pergunta);
	}
}
