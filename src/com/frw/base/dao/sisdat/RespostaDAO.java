package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.Resposta;

public class RespostaDAO extends BaseDAO<Resposta> {

	public List<Resposta> buscarRespostasPorPergunta(Long id) {
		try {
			return em.createQuery("select r from Resposta r where r.pergunta.id = :id and r.excluido <> true").setParameter("id", id).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Resposta>();
		}
	}

	public List<Resposta> buscarRespostasPorQuestionario(Long id) {
		try {
			return em.createQuery("select r from Resposta r where r.pergunta.questionario.id = :id and r.excluido <> true").setParameter("id", id).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Resposta>();
		}
	}


	public Integer pesquisarMaxOrdemPorPergunta(Long id) {

		try {
			Query query = em.createQuery("select MAX(r.ordem) from Resposta r where r.pergunta.id = :id and r.excluido <> true");
			query.setParameter("id", id);

			return (Integer) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
