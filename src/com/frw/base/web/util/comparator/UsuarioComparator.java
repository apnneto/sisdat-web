package com.frw.base.web.util.comparator;

import java.util.Comparator;

import com.frw.base.dominio.base.Usuario;

/**
 * @author Carlos Santos
 */
public class UsuarioComparator implements Comparator<Usuario> {

    @Override
    public int compare(Usuario usuario1, Usuario usuario2) {

        int compare = usuario1.getTipoUsuario().getDescricao().compareToIgnoreCase(usuario2.getTipoUsuario().getDescricao());

        if (compare == 0) {
            compare = usuario1.getLogin().compareToIgnoreCase(usuario2.getLogin());
        }

        return compare;

    }
}
