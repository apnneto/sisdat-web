package com.frw.base.dao.sisdat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Query;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.util.DateUtils;
import com.frw.manutencao.dominio.dto.FilterListRespostaPesquisaDTO;

public class PesquisaDAO extends BaseDAO<Pesquisa> {

	@SuppressWarnings("unchecked")
	public List<Pesquisa> findPesquisaByQuestionario(Questionario questionario) {

		try {
			return em
					.createQuery(
							"select p from Pesquisa p where p.questionario.id = :id and p.excluido <> true order by p.id")
					.setParameter("id", questionario.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pesquisa>();
		}

	}

	public List<Pesquisa> findPesquisaByQuestionarioUsuario(Questionario questionario, FilterListRespostaPesquisaDTO filterDTO) {

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			StringBuilder queryBuilder = new StringBuilder("");
			
			queryBuilder.append("SELECT p FROM Pesquisa p WHERE p.excluido <> true");
		
			if(questionario != null ){
				queryBuilder.append(" AND p.questionario.id = :questionario");
				parameters.put("questionario", questionario.getId());
			}
			
			if(filterDTO != null){
				if(filterDTO.getUsuario() != null){
					queryBuilder.append(" AND p.usuario.id = :usuario");
					parameters.put("usuario", filterDTO.getUsuario().getId());
				}
				if(filterDTO.getEmpresa() !=null){
					queryBuilder.append(" AND p.usuario.empresa.id = :empresa ");
					parameters.put("empresa", filterDTO.getEmpresa().getId());
				}
				if(filterDTO.getUsuario() != null && !filterDTO.getUsuario().getTipoUsuario().getId().equals(TipoUsuario.ADM)){
					queryBuilder.append(" AND p.visivel <> false ");
				}
				
				if(filterDTO.getDataInicio() != null && filterDTO.getDataFim() != null){
					queryBuilder.append(" AND p.dataSincronizacao >= :dataInicio ");
					queryBuilder.append(" AND p.dataSincronizacao <= :dataFim ");
					parameters.put("dataInicio", DateUtils.startDay(filterDTO.getDataInicio()));
					parameters.put("dataFim", DateUtils.endDay(filterDTO.getDataFim()));
				}
			}
			Query query =  em.createQuery(queryBuilder.toString());
			setQueryParameters(query, parameters);
			return query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pesquisa>();
		}

	}

}
