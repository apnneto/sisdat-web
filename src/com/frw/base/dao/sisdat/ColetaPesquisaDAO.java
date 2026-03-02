package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.List;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;

public class ColetaPesquisaDAO extends BaseDAO<ColetaPesquisa> {

	public List<ColetaPesquisa> buscarColetaPesquisaPorPesquisa(Long id) {
		try {
			return em.createQuery("select p from ColetaPesquisa p where p.pesquisa.id = :id and p.excluido <> true")
					.setParameter("id", id).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ColetaPesquisa>();
		}
	}

	public List<ColetaPesquisa> buscarColetaPesquisaPorQuestionario(Questionario questionario) {
		try {
			return em
					.createQuery(
							"select p from ColetaPesquisa p where p.pesquisa.questionario.id = :id and p.excluido <> true")
					.setParameter("id", questionario.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ColetaPesquisa>();
		}
	}

	public List<ColetaPesquisa> buscarMultiplasRespostas(ColetaPesquisa coletaPesquisa) {
		try {
			return em
					.createQuery(
							"select distinct c from ColetaPesquisa c where c.excluido <> true and c.pesquisa.id = :idPesquisa and c.pergunta.id = :idPergunta ")
					.setParameter("idPergunta", coletaPesquisa.getPergunta().getId())
					.setParameter("idPesquisa", coletaPesquisa.getPesquisa().getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ColetaPesquisa>();
		}
	}

	public ColetaPesquisa buscarResposta(Pesquisa pesquisa, Pergunta pergunta) {
		try {
			return em.createQuery("select DISTINCT c from ColetaPesquisa c where c.excluido <> true and c.pesquisa = :pesquisa and c.pergunta = :pergunta ", ColetaPesquisa.class)
							.setParameter("pesquisa", pesquisa)
							.setParameter("pergunta", pergunta)
							.setMaxResults(1)
							.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return new ColetaPesquisa();
		}
	}

}
