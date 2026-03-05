<%-- Redirect to Wicket application (triggers WicketFilter -> LoginPage) --%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<% response.sendRedirect(request.getContextPath() + "/wicket"); %>