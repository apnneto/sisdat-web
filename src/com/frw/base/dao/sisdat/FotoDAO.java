package com.frw.base.dao.sisdat;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Query;

import com.frw.base.dao.BaseDAO;
import com.frw.base.dominio.sisdat.Foto;

public class FotoDAO extends BaseDAO<Foto> {

	public List<Foto> buscarFotosPorPesquisa(Long idPesquisa) {
		Query query = em.createQuery("select f from Foto f where f.excluido <> true and f.pesquisa.id = ?1");
		query.setParameter(1, idPesquisa);
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insert(Foto foto) {
		try {
			if(foto != null && foto.getDataAlteracao() == null)
				foto.setDataAlteracao(new Date());
			em.persist(foto);
			em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
