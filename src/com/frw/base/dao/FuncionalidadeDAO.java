package com.frw.base.dao;

import java.util.List;

import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.TipoUsuario;


/**
 * @author juliano
 */
public class FuncionalidadeDAO extends BaseDAO<Funcionalidade> {

    public List<Funcionalidade> findAllFuncionalidades() {
        return em.createNamedQuery("findAllFuncionalidades")
                 .getResultList();
    }

    public List<Funcionalidade> findFuncionalidadesByTipoUsuario(TipoUsuario tipo) {
        return em.createNamedQuery("findFuncionalidadesByTipoUsuario")
                 .setParameter("tipo", tipo.getId())
                 .getResultList();
    }

    public List<Funcionalidade> findFuncionalidadesByTipoUsuarioEModulo(TipoUsuario tipo, Modulo modulo) {
        return em.createNamedQuery("findFuncionalidadesByTipoUsuarioEModulo")
                 .setParameter("tipo", tipo.getId())
                 .setParameter("modulo", modulo.getId())
                 .getResultList();
    }
    
}
