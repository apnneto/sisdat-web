package com.frw.base.web;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet required on Payara 6 / Servlet 6.0 so that the WicketFilter
 * (mapped to /*) is included in the filter chain for all requests.
 *
 * On Payara 6, a Filter with url-pattern="/*" is only invoked when the request
 * also matches a Servlet. This servlet satisfies that requirement.
 *
 * The WicketFilter intercepts all Wicket page/resource requests first.
 * For anything Wicket does NOT handle (static files: CSS, JS, images, etc.),
 * the request falls through to this servlet, which forwards it to the
 * container's built-in "default" servlet so the file is served from the WAR.
 */
public class WicketEntryPointServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // WicketFilter did not handle this request — forward to default servlet
        // so that static resources (CSS, JS, images) are served from the WAR.
        RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
        if (rd != null) {
            rd.forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
