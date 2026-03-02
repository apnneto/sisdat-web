package com.frw.base.dao;

import java.util.List;

import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.TipoUsuario;

/**
 * @author juliano
 */
public class PerfilDAO extends BaseDAO<Perfil> {

    public List<Perfil> findByTipoUsuario(TipoUsuario tipo) {
        return em.createNamedQuery("findPerfilByTipoUsuario")
                 .setParameter("tipo", tipo)
                 .getResultList();
    }

}
