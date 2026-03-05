package com.frw.base.negocio;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.frw.base.dao.ModuloDAO;
import com.frw.base.dao.UsuarioDAO;
import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.exception.SistemaException;

/**
 * @author juliano
 */
@Stateless
public class SystemFacade {
    
    @Inject
    private ModuloDAO moduloDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @PersistenceContext(unitName = "xq.pu")
    protected EntityManager em;


    public void changeUserPassword(Usuario userLogado, String currentPassword, String newPassword, String newPasswordConfirmation) throws SistemaException {

        if(!newPassword.equals(newPasswordConfirmation)) {
            throw  new SistemaException("exception.user.changepassword.confirmacao.invalida");
        }

        if(!userLogado.getSenha().equals(currentPassword)){
            throw  new SistemaException("exception.user.changepassword.senhaatual.invalida");
        }

        userLogado.setSenha(newPassword);
        usuarioDAO.saveOrUpdate(userLogado);

    }

    public void criaUsuario(Usuario usuario) {
       usuarioDAO.saveOrUpdate(usuario);

    }

    public void forceFlush() {

        em.flush();
    }

    // Método de conveniencia para testes unitarios

    public List<Funcionalidade> getFuncionalidades(Usuario usuarioLogado, Modulo moduloSelecionado) {
        return usuarioDAO.getFuncionalidades(usuarioLogado, moduloSelecionado);
    }

    public Modulo getModuloDefault() {

        return moduloDAO.getModuloDefault();
    }

    public List<Modulo> getModulos(Usuario usuarioLogado) {
        return usuarioDAO.getModulos(usuarioLogado);
    }

    public Usuario login(String login, String password) {
        return usuarioDAO.findByLoginESenha(login,password);
    }

}
