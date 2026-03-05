package com.frw.base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.Usuario;
import com.frw.manutencao.dominio.dto.FilterUsuarioDTO;

/**
 * @author juliano
 */
public class UsuarioDAO extends BaseDAO<Usuario> {

    public List<Usuario> buscarPorEmpresa(Long idEmpresa) {
		StringBuilder queryBuilder = new StringBuilder("select u from Usuario u where u.excluido <> true ");
		if(idEmpresa != null){
			queryBuilder.append("and u.empresa.id = ?1 ");
		}
		queryBuilder.append("order by u.nome");
		
		Query query = em.createQuery(queryBuilder.toString());
		if(idEmpresa != null){
			query.setParameter(1, idEmpresa);
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    public List<Usuario> buscarTodosUsuariosComEmpresa() {
		Query query = em.createQuery("select u from Usuario u where u.excluido <> true and u.empresa is not null order by u.nome");
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    public List<Usuario> findAllUsuarios() {
        return em.createNamedQuery("findAllUsuarios")
                .getResultList();
    }

    public List<String> findAutocompleteByLogin(String login) {
		StringBuilder queryBuilder = new StringBuilder("select u.login from Usuario u where u.excluido <> true ");
		
		if (login !=null && !login.isEmpty()) {
			queryBuilder.append(" and lower(u.login) like :login ");
		}
		
		queryBuilder.append(" order by u.login ");
		Query query = em.createQuery(queryBuilder.toString());
		
		if (queryBuilder.toString().contains(":login")) {
			query.setParameter("login", login.toLowerCase().concat("%"));
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

    public List<String> findAutocompleteByNome(String nome) {
		StringBuilder queryBuilder = new StringBuilder("select u.nome from Usuario u where u.excluido <> true ");
		
		if (nome !=null && !nome.isEmpty()) {
			queryBuilder.append(" and lower(u.nome) like :nome ");
		}
		
		queryBuilder.append(" order by u.nome ");
		Query query = em.createQuery(queryBuilder.toString());
		
		if (queryBuilder.toString().contains(":nome")) {
			query.setParameter("nome", nome.toLowerCase().concat("%"));
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

    public Usuario findByCpf(String cpf) {
        try {
            return (Usuario) em.createNamedQuery("findUsuarioByCpf")
                               .setParameter("cpf", cpf)
                               .getSingleResult();
        } catch (NoResultException noex) {
            return null;
        }
    }
    
    public Usuario findByLogin(String login) {
        try {
            return (Usuario) em.createNamedQuery("findUsuarioByLogin")
                               .setParameter("login", login)
                               .getSingleResult();
        } catch (NoResultException noex) {
            return null;
        }
    }

	public Usuario findByLoginESenha(String login, String password) {
        try {
            return (Usuario) em.createNamedQuery("findUsuarioByLoginESenha")
                               .setParameter("login", login)
                               .setParameter("password", password)
                               .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
	
	public List<Funcionalidade> getFuncionalidades(Usuario usuarioLogado, Modulo moduloSelecionado) {
         return em.createNamedQuery("findFuncionalidadesByUsuarioAndModulo")
                .setParameter("u", usuarioLogado)
                .setParameter("m", moduloSelecionado)
                .getResultList();

    }

	public List<Modulo> getModulos(Usuario usuarioLogado) {
         return em.createNamedQuery("findModulosByUsuario")
                .setParameter("u", usuarioLogado)
                .getResultList();
    }

	public List<Usuario> pesquisarUsuarios(FilterUsuarioDTO filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        if (filter == null) {
            filter = new FilterUsuarioDTO();
        }

        StringBuilder queryBuilder = new StringBuilder("select distinct u from Usuario u ");
        queryBuilder.append(" where u.excluido <> true ");

        if (filter.getNome() != null) {
            queryBuilder.append(" and lower(u.nome) like :nome ");
            parameters.put("nome", filter.getNome().toLowerCase().concat("%"));
        }

        if (filter.getLogin() != null) {
            queryBuilder.append(" and lower(u.login) like :login ");
            parameters.put("login", filter.getLogin().toLowerCase().concat("%"));
        }
        
        if(filter.getDataAtualizacao() != null) {
            queryBuilder.append(" and u.dataAlteracao > :data ");
            parameters.put("data", filter.getDataAtualizacao());
        }

        if(filter.getEmpresa() != null) {
            queryBuilder.append(" and u.empresa.id = :idEmpresa ");
            parameters.put("idEmpresa", filter.getEmpresa().getId());
        }

        /** seta os parâmetros na query */
        Query query = em.createQuery(queryBuilder.toString());
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        return query.getResultList();

    }

}
