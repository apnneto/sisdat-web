package com.frw.base.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.util.lang.PackageName;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;
import org.wicketstuff.javaee.naming.JavaEE6NamingStrategy;

import com.frw.base.web.pages.HomePage;
import com.frw.base.web.pages.LoginPage;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.logicstyle.logicwicket.WebAppResourceStreamLocator;

/**
 * Classe de inicialização da aplicação Wicket
 * @author juliano
 */
public class SistemaApplication extends WebApplication {

    @Override
	public String getConfigurationType() {
 		return Application.DEPLOYMENT;
 	}

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final RequestCycle newRequestCycle(final Request request, final Response response) {
        return new MyRequestCycle(this, (WebRequest) request, (WebResponse) response);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new SistemaSession(request);
    }

    @Override
    protected void init() {

    	  /* desabilita ajax debug */
        getDebugSettings().setAjaxDebugModeEnabled(Boolean.FALSE);
        getResourceSettings().setResourcePollFrequency(null);

        getResourceSettings().setResourceStreamLocator(new WebAppResourceStreamLocator(getServletContext(), "com/frw/base/web/pages"));
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.ONE_PASS_RENDER);

        addComponentInstantiationListener(new JavaEEComponentInjector(this, new JavaEE6NamingStrategy("sisdat-web")));

        mount("/pages", PackageName.forClass(HomePage.class));
        mount("/pages/util", PackageName.forClass(ModalConfirmationPanel.class));

        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new SistemaAuthorizer()));

        getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener() {

            public void onUnauthorizedInstantiation(Component cmpnt) {

                throw new RestartResponseAtInterceptPageException(LoginPage.class);
            }
        });
    }

    @Override
    protected ISessionStore newSessionStore() {
        return new HttpSessionStore(this);
    }
    
    @Override
    protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
        return new UploadWebRequest(servletRequest);
    }
}
