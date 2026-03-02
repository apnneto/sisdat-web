package com.frw.base.web.util.comparator;

import java.util.Comparator;

import com.frw.base.dominio.base.Perfil;

/**
 * @author Carlos Santos
 */
public class PerfilComparator implements Comparator<Perfil> {

    @Override
    public int compare(Perfil perfil1, Perfil perfil2) {

        int compare = perfil1.getTipoUsuario().getDescricao().compareToIgnoreCase(perfil2.getTipoUsuario().getDescricao());

        /** se o tipo de usuário é igual, compara o nome da funcionalidade */
        if (compare == 0) {
            compare = perfil1.getNome().compareToIgnoreCase(perfil2.getNome());
        }

        return compare;

    }

}
