package com.frw.base.negocio;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.frw.base.dao.FuncionalidadeDAO;
import com.frw.base.dao.ModuloDAO;
import com.frw.base.dao.PerfilDAO;
import com.frw.base.dao.TipoUsuarioDAO;
import com.frw.base.dao.UsuarioDAO;
import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.web.SistemaSession;
import com.frw.manutencao.dominio.dto.FilterUsuarioDTO;

/**
 * @author Carlos Santos
 * @author juliano
 */
@Stateless
public class CadastroFacade {

    @EJB
    private EmpresaFacade empresaFacade;
    @Inject
    private FuncionalidadeDAO funcionalidadeDAO;
    @Inject
    private ModuloDAO moduloDAO;
    @Inject
    private PerfilDAO perfilDAO;
    @Inject
    private TipoUsuarioDAO tipoUsuarioDAO;
    
    @Inject
    private UsuarioDAO usuarioDAO;
 
    public List<Usuario> buscarTodosUsuariosComEmpresa() {
		List<Usuario> list = usuarioDAO.buscarTodosUsuariosComEmpresa();
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}
    
    public List<Usuario> buscarUsuariosPorEmpresa(Empresa empresa) {
		if (empresa == null) {
			return Collections.emptyList(); 
		}
		List<Usuario> list = usuarioDAO.buscarPorEmpresa(empresa.getId());
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
		
	}

    public void excluirPerfil(Perfil perfil) throws SistemaException {
        // se houver usuários usando este perfil, não permite a exclusão
        if (!perfil.getUsuarios().isEmpty()) {
            throw new SistemaException("exception.perfil.exclusao.naoPermitida");
        }

        perfil.setExcluido(true);
        perfilDAO.saveOrUpdate(perfil);
    }

    public void excluirUsuario(Usuario entity) {
		SistemaSession.setUserAndCurrentDate(entity);
        entity.setExcluido(true);
        usuarioDAO.saveOrUpdate(entity);
    }

    public List<String> pesquisaAutocompletePorLogin(String login) {
		return usuarioDAO.findAutocompleteByLogin(login);
	}

    public List<String> pesquisaAutocompletePorNome(String nome) {
		return usuarioDAO.findAutocompleteByNome(nome);
	}

    public List<Funcionalidade> pesquisarFuncionalidadesPorTipoUsuario(TipoUsuario tipo) {
        return funcionalidadeDAO.findFuncionalidadesByTipoUsuario(tipo);
    }
    
    public List<Funcionalidade> pesquisarFuncionalidadesPorTipoUsuarioEModulo(TipoUsuario tipo, Modulo modulo) {
        return funcionalidadeDAO.findFuncionalidadesByTipoUsuarioEModulo(tipo, modulo);
    }
	
	public Perfil pesquisarPerfil(Long idPerfil) {
		if (idPerfil == null) {
			return null;
		}
		return perfilDAO.findById(idPerfil);
	}

    public List<Perfil> pesquisarPerfis(Usuario usuarioLogado) {
        return perfilDAO.findAll();
    }    
    
    public List<Modulo> pesquisarTodosModulos() {
        return moduloDAO.findAll();
    }
	public List<Perfil> pesquisarTodosPerfis() {
        return perfilDAO.findAll();
    }

	public List<TipoUsuario> pesquisarTodosTiposUsuario() {
        return tipoUsuarioDAO.findAll();
    }

	public Usuario pesquisarUsuarioPorId(Long idUsuario) {
		if (idUsuario == null) {
			return null;
		}
		return usuarioDAO.findById(idUsuario);
	}

	public List<Usuario> pesquisaTodosUsuarios(FilterUsuarioDTO filter) {
        return usuarioDAO.pesquisarUsuarios(filter);
    }

	public List<Usuario> pesquisaUsuariosPorDataAtualizacao(Date dataAtualizacao) {
        FilterUsuarioDTO filter = new FilterUsuarioDTO();
        filter.setDataAtualizacao(dataAtualizacao);
        return usuarioDAO.pesquisarUsuarios(filter);
    }

	public void salvarPerfil(Perfil entity) {
        perfilDAO.saveOrUpdate(entity);
    }

	public Usuario salvarUsuario(Usuario usuario) throws SistemaException {
    	
        /** verifica se o nome do usuario foi informado */
        if (usuario.getNome() == null || usuario.getNome().equals("")) {
            throw new SistemaException("exception.user.nome.obrigatorio");
        }

        /** verifica se existe algum usuario com o cpf informado */
        if (usuario.getCpf() != null && !usuario.getCpf().trim().equals("")) {
            Usuario result = usuarioDAO.findByCpf(usuario.getCpf());

            if (result != null && (usuario.getId() == null || !usuario.getId().equals(result.getId()))) {
                throw new SistemaException("exception.user.cpf.exists");
            }
        }

        /** verifica se existe algum usuário com o mesmo login */
        Usuario result = usuarioDAO.findByLogin(usuario.getLogin());

        if (result != null && (usuario.getId() == null || !usuario.getId().equals(result.getId()))) {
            throw new SistemaException("exception.user.login.exists");
        }
        
        if (usuario.getEmpresa()!= null && usuario.getEmpresa().getId().equals(Empresa.PELLI_SISTEMAS_KEY)) {
			usuario.setTipoUsuario(pesquisarTipoUsuarioPorId(TipoUsuario.ADM));
			usuario.getPerfis().clear();
			usuario.getPerfis().add(pesquisarPerfil(Perfil.ADM));
		}else {
			definirUsuarioPesquisador(usuario);
		}

        return usuarioDAO.saveOrUpdate(usuario);
    }

	/**
	 * Sobrepoe os perfis e tipo de usuario por Pesquisador.
	 * @param usuario
	 */
	private void definirUsuarioPesquisador(Usuario usuario) {
		usuario.setTipoUsuario(pesquisarTipoUsuarioPorId(TipoUsuario.PESQUISADOR));
		usuario.getPerfis().clear();
		usuario.getPerfis().add(pesquisarPerfil(Perfil.PESQUISADOR));
	}

	private TipoUsuario pesquisarTipoUsuarioPorId(long idTipoUsuario) {
		return tipoUsuarioDAO.findById(idTipoUsuario);
	}
 
}
