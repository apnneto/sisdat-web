package com.frw.base.web;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.javaee.injection.AnnotJavaEEInjector;
import org.wicketstuff.javaee.naming.JavaEE6NamingStrategy;

/**
 * Wicket 9 compatible EJB injector.
 * Replaces Wicket 1.4 JavaEEComponentInjector (which used InjectorHolder).
 * Registered via WebApplication.getComponentInstantiationListeners().add(...)
 */
public class JavaEEComponentInjectorWicket9 implements IComponentInstantiationListener {

    private final AnnotJavaEEInjector injector;

    public JavaEEComponentInjectorWicket9(WebApplication app) {
        this.injector = new AnnotJavaEEInjector(new JavaEE6NamingStrategy("sisdat-web"));
    }

    @Override
    public void onInstantiation(Component component) {
        injector.inject(component);
    }
}
