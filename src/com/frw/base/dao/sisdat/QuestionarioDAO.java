package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.util.DateUtils;
import com.frw.manutencao.dominio.dto.FilterListQuestionarioDTO;

public class QuestionarioDAO extends BaseDAO<Questionario> {

	public List<String> buscarAutoComplete(String descricao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder queryBuilder = new StringBuilder("select q from Questionario q where q.excluido <> true ");
		
		if (descricao !=null && !descricao.isEmpty()) {
			queryBuilder.append(" and lower(q.descricao) like :descricao ");
			parameters.put("descricao", "%" + descricao.toLowerCase() + "%");
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

	public Questionario buscarQuestionarioPorCodigo(String codigoQuest) {
		Query query = em.createQuery("select q from Questionario q where q.excluido <> true and LOWER (q.codigo) like :codigo ");
		query.setParameter("codigo",codigoQuest.toLowerCase());
		
		try {
			return (Questionario) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param filterDTO
	 * @return
	 */
	public List<Questionario> buscarQuestionarioPorFiltro( FilterListQuestionarioDTO filterDTO) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT h FROM Questionario h ");
		queryBuilder.append(" JOIN h.empresas empresa");
		queryBuilder.append(" WHERE h.excluido <> true ");

		if (filterDTO.getDescricao() != null && !filterDTO.getDescricao().isEmpty()) {
			queryBuilder.append(" AND LOWER(h.descricao) like :descricao ");
			parameters.put("descricao", "%" + filterDTO.getDescricao().toLowerCase() + "%");
		}
		
		if(filterDTO.getDataInicio() != null){
			queryBuilder.append(" AND h.dataAlteracao >= :dataInicio ");
			parameters.put("dataInicio", DateUtils.startDay(filterDTO.getDataInicio()));
		}
		
		if(filterDTO.getDataFim() != null){
			queryBuilder.append(" AND h.dataAlteracao <= :dataFim ");
			parameters.put("dataFim", DateUtils.endDay(filterDTO.getDataFim()));
		}
		
		if(filterDTO.getUsuario() != null && filterDTO.getUsuario().getEmpresa() != null){
			queryBuilder.append(" AND h.empresas = :empresa ");
			parameters.put("empresa", filterDTO.getUsuario().getEmpresa());
		}
		
		queryBuilder.append(" order by h.descricao ");
		/** seta os parâmetros na query */
		Query query = em.createQuery(queryBuilder.toString());
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}

		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Questionario>();
		}
	}

	public List<Questionario> buscarTodosQuestionariosPublicados() {

		StringBuilder queryBuilder = new StringBuilder(
				"select h from Questionario h ");
		queryBuilder.append(" where h.excluido <> true and h.publicado = true");

		Query query = em.createQuery(queryBuilder.toString());

		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Questionario>();
		}
	}


	@Override
	public List<Questionario> findAll() {
		StringBuilder queryBuilder = new StringBuilder("select q from Questionario q where q.excluido <> true ");

		Query query = em.createQuery(queryBuilder.toString());
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Questionario>();
		}
	}
	
	public List<String> findAutocompleteByDescricao(String descricao) {
		StringBuilder queryBuilder = new StringBuilder("select q.descricao from Questionario q where q.excluido <> true ");
		
		if (descricao !=null && !descricao.isEmpty()) {
			queryBuilder.append(" and lower(q.descricao) like :descricao ");
		}
		
		queryBuilder.append(" order by q.descricao ");
		Query query = em.createQuery(queryBuilder.toString());
		
		if (queryBuilder.toString().contains(":descricao")) {
			query.setParameter("descricao", "%".concat(descricao.toLowerCase().concat("%")));
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public Questionario findByDescricaoCompleta(String descricao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder queryBuilder = new StringBuilder("select q from Questionario q where q.excluido <> true ");

		if (descricao !=null && !descricao.isEmpty()) {
			queryBuilder.append(" and lower(q.descricao) = :descricao ");
			parameters.put("descricao", descricao.toLowerCase());
		}
		
		Query query = em.createQuery(queryBuilder.toString());
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		try {
			return (Questionario) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Questionario> findOnlyQuestionarioWithPesquisa() {
		Query query = em.createQuery("select q from Questionario q where q.excluido <> true and q.id IN" +
				" (select p.questionario.id from Pesquisa p group by p.questionario.id) ");

		try {
			return (List<Questionario>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Questionario>();
		}
	}

	public List<Questionario> findOnlyQuestionarioWithPesquisaPorUsuario(Long idUsuario) {
		StringBuilder queryBuilder = new StringBuilder("select q from Questionario q where q.excluido <> true and q.id IN" +
				" (select p.questionario.id from Pesquisa p where p.usuario.id = ?1 group by p.questionario.id) ");

		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter(1, idUsuario);
		
		try {
			return (List<Questionario>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Questionario>();
		}
	}

}
