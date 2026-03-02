package com.frw.base.web;

import java.util.Iterator;

import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;

/**
 *
 * @author juliano
 */
public class SistemaAuthorizer implements IRoleCheckingStrategy{

    public boolean hasAnyRole(Roles roles) {

        Iterator<String> it=roles.iterator();
        SistemaSession session=(SistemaSession) Session.get();

        while(it.hasNext()) {
            String role=it.next();

            if(role.equals("valid_user")) {
                if(session.getUsuarioLogado()!=null)
                    return true;
            }

            // TODO: implementar outras roles de acordo com o modelo de permissões da aplicação

        }

        return false;
    }



}
