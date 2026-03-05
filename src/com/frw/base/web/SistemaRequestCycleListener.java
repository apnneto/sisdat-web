package com.frw.base.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import com.frw.base.exception.ObjetoObsoletoException;
import com.frw.base.web.pages.MyErrorPage;
import com.frw.base.web.pages.ObjetoObsoletoErrorPage;
import com.frw.base.web.pages.SessaoExpiradaErrorPage;

/**
 * Replaces Wicket 1.4 WebRequestCycle.onRuntimeException().
 * Wicket 9 uses IRequestCycleListener for global exception handling.
 */
public class SistemaRequestCycleListener implements IRequestCycleListener {

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception e) {
        Logger.getLogger(SistemaRequestCycleListener.class.getName())
              .log(Level.SEVERE, "Ocorreu um erro na aplicação", e);

        Throwable root = ExceptionUtils.getRootCause(e);

        if (root instanceof ObjetoObsoletoException) {
            return new RenderPageRequestHandler(new PageProvider(new ObjetoObsoletoErrorPage(null, (RuntimeException) e)));
        }
        if (e instanceof org.apache.wicket.protocol.http.PageExpiredException) {
            return new RenderPageRequestHandler(new PageProvider(new SessaoExpiradaErrorPage()));
        }
        return new RenderPageRequestHandler(new PageProvider(new MyErrorPage(e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e))));
    }
}
