/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.util;

import com.frw.base.dominio.base.EntidadeBase;

/**
 *
 * @author Marcelo Alves
 */
public interface FilterCriteria<T extends EntidadeBase> {
    public boolean passes(T entidade);
}
