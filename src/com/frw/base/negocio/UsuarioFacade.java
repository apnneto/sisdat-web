package com.frw.base.negocio;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.UsuarioDAO;
import com.frw.base.dominio.base.Usuario;

@Stateless	
public class UsuarioFacade {

	@Inject
	private UsuarioDAO dao;
	
	public Usuario buscarPorId(Long id) {
		return dao.findById(id);
	}
	
	public Usuario buscarUsuarioPorLogin(String login) {
		return dao.findByLogin(login);
	}

	public List<Usuario> buscarUsuarios() {
		return dao.findAll();
	}
}
