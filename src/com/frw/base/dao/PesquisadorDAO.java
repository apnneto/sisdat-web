package com.frw.base.dao;

import java.util.ArrayList;
import java.util.List;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Pesquisador;

/**
 * @author Marcos Lisboa
 */
public class PesquisadorDAO extends BaseDAO<Pesquisador> {
	
	public Pesquisador findbyId(Long id){
		
		try {
			return (Pesquisador) em.createQuery("select p from Pesquisador p where p.id = :id").setParameter("id", id).getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Pesquisador();
		}
		
		
	}
	
	public List<Pesquisador> findbyUsuarios(Usuario usuario){
	
		try {
			return em.createQuery("select p from Pesquisador p where p.excluido <> true AND p.usuario.id = :usuario order by p.nome").setParameter("usuario", usuario.getId()).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Pesquisador>();
		}
		
		
	}
	
}
