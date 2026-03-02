package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.List;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.TipoPergunta;

public class TipoPerguntaDAO extends BaseDAO<TipoPergunta>{
	
	public List<Pergunta> buscarPerguntasPorQuestionario(Long id) {
		try {
			return em.createQuery("select p from Pergunta p where p.questionario.id = :id and p.excluido <> true").setParameter("id", id).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}
	
}
