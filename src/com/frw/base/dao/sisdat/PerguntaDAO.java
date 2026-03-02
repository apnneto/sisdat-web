package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.manutencao.dominio.dto.FilterPerguntaDescricaoDTO;

public class PerguntaDAO extends BaseDAO<Pergunta>{
	
	public List<String> buscarAutoComplete(String descricao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder queryBuilder = new StringBuilder("select p from Pergunta p where p.excluido <> true ");
		
		if (descricao !=null && !descricao.isEmpty()) {
			queryBuilder.append(" and lower(p.descricao) like :descricao ");
			parameters.put("descricao", descricao.toLowerCase().concat("%"));
		}
		
		Query query = em.createQuery(queryBuilder.toString());
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}
	
	public List<Pergunta> buscarPerguntasDependentes(Questionario questionario, Pergunta pergunta) {
		try {
			return em.createQuery("select p from Pergunta p where p.questionario.id = :id and p.id <> :perId and p.excluido <> true ORDER BY p.quadrante").setParameter("id", questionario.getId()).setParameter("perId",pergunta.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}
	
	public List<Pergunta> buscarPerguntasPorFiltro(List<Long> idsPalavrasChave, List<Long> idsQuestionarios) {
		final String PARAMETER_PALAVRAS_CHAVES = "param_palavras";
		final String PARAMETER_QUESTIONARIOS = "param_questionarios";

		StringBuilder queryBuilder = new StringBuilder("select distinct p from Pergunta p join p.palavrasChave c join p.questionario q where p.excluido <> true ");

		if ((idsPalavrasChave !=null && !idsPalavrasChave.isEmpty()) || (idsQuestionarios !=null && !idsQuestionarios.isEmpty())){
			queryBuilder.append(" AND ( ");
			
			if (idsPalavrasChave !=null && !idsPalavrasChave.isEmpty()) {
				queryBuilder.append(" c.id IN :".concat(PARAMETER_PALAVRAS_CHAVES));
			}
			
			if ((idsPalavrasChave !=null && !idsPalavrasChave.isEmpty()) && (idsQuestionarios !=null && !idsQuestionarios.isEmpty())){
				queryBuilder.append(" OR ");
			}
			
			if (idsQuestionarios !=null && !idsQuestionarios.isEmpty()) {
				queryBuilder.append(" q.id IN :".concat(PARAMETER_QUESTIONARIOS));
			}
			
			queryBuilder.append(" ) ");
		}
		
		
		Query query = em.createQuery(queryBuilder.toString(), Pergunta.class);
		
		if (queryBuilder.toString().contains(PARAMETER_PALAVRAS_CHAVES)) {
			query.setParameter(PARAMETER_PALAVRAS_CHAVES, idsPalavrasChave);
		}
		
		if (queryBuilder.toString().contains(PARAMETER_QUESTIONARIOS)) {
			query.setParameter(PARAMETER_QUESTIONARIOS, idsQuestionarios);
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}

	
	public List<Pergunta> buscarPerguntasPorQuestionario(Questionario questionario) {
		
		if(questionario == null || questionario.getId() == null)
			return new ArrayList<Pergunta>();
		
		try {
			return em.createQuery(" select p from Pergunta p where p.questionario.id = :id and p.excluido <> true ORDER BY p.ordem, p.descricao").setParameter("id", questionario.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}
	
	public List<Pergunta> buscarPerguntasPorQuestionarioFilter(Long id, FilterPerguntaDescricaoDTO filterDTO) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder queryBuilder = new StringBuilder("select p from Pergunta p ");
		queryBuilder.append(" where p.excluido <> true ");
		queryBuilder.append(" AND p.questionario.id = :id ");
		parameters.put("id", id);

		if (filterDTO.getPerguntaDescricao() != null && !filterDTO.getPerguntaDescricao().isEmpty()) {
			queryBuilder.append(" AND lower(p.descricao) like :descricao ");
			parameters.put("descricao","%".concat(filterDTO.getPerguntaDescricao().toLowerCase().concat("%")));
		}
		if (filterDTO.getPerguntaSubDescricao() != null && !filterDTO.getPerguntaSubDescricao().isEmpty()) {
			queryBuilder.append(" AND lower(p.subdescricao) like :subdescricao ");
			parameters.put("subdescricao","%".concat(filterDTO.getPerguntaSubDescricao().toLowerCase().concat("%")));
		}
		
		queryBuilder.append(" order by p.descricao ");
		/** seta os parâmetros na query */
		Query query = em.createQuery(queryBuilder.toString());
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}

		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Pergunta>();
		}
	}
	
	public Pergunta buscarPorDescricaoCompleta(String descricao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder queryBuilder = new StringBuilder("select p from Pergunta p where p.excluido <> true ");

		if (descricao !=null && !descricao.isEmpty()) {
			queryBuilder.append(" and lower(p.descricao) = :descricao ");
			parameters.put("descricao", descricao.toLowerCase());
		}
		
		Query query = em.createQuery(queryBuilder.toString());
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		try {
			return (Pergunta) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Pergunta> findAll() {
		StringBuilder queryBuilder = new StringBuilder("select q from Pergunta q where q.excluido <> true ");

		Query query = em.createQuery(queryBuilder.toString());
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}

	public Integer ultimoIdentificadorOrdem(Pergunta pergunta) {
		try {
			Query query = em.createQuery(" select p.ordem from Pergunta p where p.questionario.id = :questId and p.excluido <> true ORDER BY p.ordem DESC ").setParameter("questId", pergunta.getQuestionario().getId());
			query.setMaxResults(1);
			
			return (Integer)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return new Integer(0);
		}
	}

}
