package com.frw.base.web.util.comparator;

import java.util.Comparator;

import com.frw.base.dominio.base.TipoUsuario;

/**
 * @author Carlos Santos
 */
public class TipoUsuarioComparator implements Comparator<TipoUsuario> {

    @Override
    public int compare(TipoUsuario tipo1, TipoUsuario tipo2) {
       return tipo1.getDescricao().compareToIgnoreCase(tipo2.getDescricao());
    }

}
