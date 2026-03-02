package com.frw.base.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.negocio.SystemFacade;

/**
 * @author juliano
 */
public class SistemaSession extends WebSession {

    public static void setUserAndCurrentDate(EntidadeDominioBase entidade) {

        entidade.setDataAlteracao(Calendar.getInstance().getTime());
        if (((SistemaSession) Session.get()).getUsuarioLogado() != null) {
            entidade.setUsuarioAlteracao(((SistemaSession) Session.get()).getUsuarioLogado().getLogin());
        }
    }
    private Funcionalidade funcionalidadeSelecionada;
    private Map<Modulo, List<Funcionalidade>> funcionalidadesPorModulo = new HashMap<Modulo, List<Funcionalidade>>();
    private Modulo moduloSelecionado;
    private List<Modulo> modulosUsuarioLogado;

    private Usuario usuarioLogado;

    public SistemaSession(Application application, Request request) {
        super(application, request);
    }

    public SistemaSession(Request request) {
        super(request);
    }

    public SistemaSession(WebApplication application, Request request) {
        super(application, request);
    }

    public void clearFuncionalidadesUsuarioLogado() {
        funcionalidadesPorModulo.clear();
    }

    public Funcionalidade getFuncionalidadeSelecionada() {
        return funcionalidadeSelecionada;
    }

    public List<Funcionalidade> getFuncionalidadesUsuarioLogado(Modulo modulo) {
        return funcionalidadesPorModulo.get(modulo);
    }

    public synchronized Modulo getModuloSelecionado() {

        if (moduloSelecionado != null) {
            return moduloSelecionado;
        }

        return null;
    }

    public synchronized List<Modulo> getModulosUsuarioLogado() {
        if (modulosUsuarioLogado == null) {
            if (usuarioLogado != null) {
                //         Infelizmente nao da pra usar dependency injection neste ponto...
                try {

                    InitialContext ctx = new InitialContext();
                    SystemFacade facade = (SystemFacade) ctx.lookup("java:global/sisdat-web/SystemFacade");
                    List<Modulo> modulos = facade.getModulos(getUsuarioLogado());
                    modulosUsuarioLogado = modulos;
                    return modulos;


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return modulosUsuarioLogado;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setFuncionalidadeSelecionada(Funcionalidade funcionalidadeSelecionada) {
        this.funcionalidadeSelecionada = funcionalidadeSelecionada;
    }

    public void setFuncionalidadesUsuarioLogado(Modulo modulo, List<Funcionalidade> funcionalidades) {
        funcionalidadesPorModulo.put(modulo, funcionalidades);
    }

    public void setModuloSelecionado(Modulo moduloSelecionado) {
        this.moduloSelecionado = moduloSelecionado;
    }

    public void setModulosUsuarioLogado(List<Modulo> modulosUsuarioLogado) {
        this.modulosUsuarioLogado = modulosUsuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

}
