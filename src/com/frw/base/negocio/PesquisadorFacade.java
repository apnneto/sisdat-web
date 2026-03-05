package com.frw.base.negocio;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.PesquisadorDAO;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Pesquisador;

@Stateless	
public class PesquisadorFacade {

	@Inject
	private PesquisadorDAO pesquisadorDAO;
	
	public List<Pesquisador> buscarPesquisadores() {
		return pesquisadorDAO.findAll();
	}
	
	public Pesquisador buscarPesquisadoresPorId(Long id) {
		return pesquisadorDAO.findbyId(id);
	}
	
	public List<Pesquisador> buscarPesquisadoresPorUsuario(Usuario usuario) {
		return pesquisadorDAO.findbyUsuarios(usuario);
	}
	
	public void excluir(Pesquisador entity){
		entity.setExcluido(true);
		pesquisadorDAO.saveOrUpdate(entity);
	}
	
	public void salvar(Pesquisador entity){
		pesquisadorDAO.saveOrUpdate(entity);
	}
}
