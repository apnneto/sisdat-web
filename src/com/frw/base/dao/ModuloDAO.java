/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.dao;

import com.frw.base.dominio.base.Modulo;

/**
 *
 * @author juliano
 */
public class ModuloDAO extends BaseDAO<Modulo> {

    public Modulo getModuloDefault() {

        return (Modulo) em.createQuery("select m from Modulo m order by m.id ").getResultList().get(0);

    }

}
