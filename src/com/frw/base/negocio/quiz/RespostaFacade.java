package com.frw.base.negocio.quiz;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.frw.base.dao.sisdat.RespostaDAO;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.Resposta;

@Stateless
public class RespostaFacade {

	@Inject
	private RespostaDAO respostaDAO;

	public Resposta buscarPorId(Long key) {
		return respostaDAO.findById(key);
	}

	public List<Resposta> buscarRespostasPorPergunta(Pergunta pergunta) {
		if (pergunta.getId() == null) {
			return new ArrayList<Resposta>();
		}
		return respostaDAO.buscarRespostasPorPergunta(pergunta.getId());
	}

	public List<Resposta> buscarRespostasPorQuestionario(
			Questionario questionario) {
		return respostaDAO.buscarRespostasPorQuestionario(questionario.getId());
	}

	public List<Resposta> buscarTodasRespostas() {
		return respostaDAO.findAll();
	}

	public void excluirResposta(Resposta resposta) {
		resposta.setExcluido(true);
		respostaDAO.saveOrUpdate(resposta);
	}
	
	public Integer pesquisarMaxOrdemPorPergunta(Pergunta pergunta){
		return respostaDAO.pesquisarMaxOrdemPorPergunta(pergunta.getId());
	}
	
	public Resposta salvarResposta(Resposta resposta) {
		Resposta respostaSalva = respostaDAO.saveOrUpdate(resposta);
    	if (respostaSalva.getCorreta() == true){
    		
	        List<Resposta> list = buscarRespostasPorPergunta(respostaSalva.getPergunta());
	        
	        for (Resposta resp : list){
	        	if (resp.getId() != respostaSalva.getId()) {
	        		resp.setCorreta(false);
	        		respostaDAO.saveOrUpdate(resp);
				}
	        }
        
    	}

        return respostaSalva;
    }

}
