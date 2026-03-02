/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;

import com.frw.base.exception.ObjetoObsoletoException;
import com.frw.base.web.pages.MyErrorPage;
import com.frw.base.web.pages.ObjetoObsoletoErrorPage;
import com.frw.base.web.pages.SessaoExpiradaErrorPage;
import com.frw.base.web.pages.util.UpdatableModalWindow;

public class MyRequestCycle extends WebRequestCycle {

    UpdatableModalWindow modal;

    /**
     * MyRequestCycle constructor
     *
     * @param application the web application
     * @param request the web request
     * @param response the web response
     */
    public MyRequestCycle(final WebApplication application, final WebRequest request, final Response response) {
        super(application, request, response);
    }

    @Override
    public void onBeginRequest() {
        //...
    }

    @Override
    public void onEndRequest() {
        //...
    }

    @Override
    public Page onRuntimeException(Page page, RuntimeException e) {

        Logger.getLogger(MyRequestCycle.class.getName()).log(Level.SEVERE, "Ocorreu um erro na aplicação", e);

        Throwable t = ExceptionUtils.getRootCause(e);

        if (t != null) {
            if (t instanceof ObjetoObsoletoException) {
                return new ObjetoObsoletoErrorPage(page, e);
            } else {
                return new MyErrorPage(e);
            }
        } else {
            if(e instanceof PageExpiredException) {
                return new SessaoExpiradaErrorPage();
            } else {
                return new MyErrorPage(e);
            }
        }

    }
}
