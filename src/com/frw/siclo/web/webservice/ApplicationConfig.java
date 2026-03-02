/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.siclo.web.webservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Leonardo Barros
 */
@ApplicationPath("/ws")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        // No-arg constructor left intentionally empty. Resources will be discovered by the JAX-RS implementation
        // via classpath scanning or can be registered explicitly by overriding getClasses() or getSingletons().
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        // If you need to explicitly register resource or provider classes, add them here, e.g.:
        // resources.add(MyResource.class);
        return resources;
    }
}