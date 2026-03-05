package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.RequestCycleSettings.RenderStrategy;

import com.frw.base.web.pages.HomePage;
import com.frw.base.web.pages.LoginPage;

/**
 * Classe de inicialização da aplicação Wicket
 * @author juliano
 */
public class SistemaApplication extends WebApplication {

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        return RuntimeConfigurationType.DEPLOYMENT;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new SistemaSession(request);
    }

    @Override
    protected void init() {

        /* desabilita ajax debug */
        getDebugSettings().setAjaxDebugModeEnabled(Boolean.FALSE);

        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setRenderStrategy(RenderStrategy.ONE_PASS_RENDER);

        // EJB injection via JNDI
        getComponentInstantiationListeners().add(new JavaEEComponentInjectorWicket9(this));

        mountPackage("pages", HomePage.class);
        // panels are not mountable pages — only Page subclasses are mounted

        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new SistemaAuthorizer()));

        getSecuritySettings().setUnauthorizedComponentInstantiationListener(
            new IUnauthorizedComponentInstantiationListener() {
                public void onUnauthorizedInstantiation(Component cmpnt) {
                    throw new RestartResponseAtInterceptPageException(LoginPage.class);
                }
            });

        // Global error/exception handling via IRequestCycleListener
        getRequestCycleListeners().add(new SistemaRequestCycleListener());
    }
}