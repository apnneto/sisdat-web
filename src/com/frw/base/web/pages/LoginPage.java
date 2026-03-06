package com.frw.base.web.pages;

import java.text.DateFormat;

import jakarta.ejb.EJB;
import jakarta.validation.constraints.NotNull;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssUrlReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.negocio.SystemFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.DefaultFocusBehavior;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.util.LabelFrw;
import com.frw.base.web.util.TextFieldFrw;

/**
 *
 * @author juliano
 */
public class LoginPage extends WebPage {

    class LoginForm extends BaseWebBeanForm<LoginForm> {

        @NotNull
        private String login;
        @NotNull
        private String password;

        public LoginForm() {
            super("loginForm");
            setEntity(this);
            TextFieldFrw<String> txtLogin = new TextFieldFrw<String>("login");
            txtLogin.add(new DefaultFocusBehavior());
            txtLogin.setRequired(true);
            add(txtLogin);
            PasswordTextField txtSenha = new PasswordTextField("password");
            txtSenha.setRequired(true);
            add(txtSenha);
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        protected void onSubmit() {
            Usuario usuario = systemFacade.login(login, password);
                    
            if (usuario == null) {
                error(getString("login.incorreto"));
                markup.setVisibilityAllowed(true);

            } else {
            	if (usuario.getTipoUsuario() == null || usuario.getPerfis() == null || usuario.getPerfis().isEmpty()) {
					setResponsePage(LoginPage.class);
				}else {
	                LoginPage.this.usuario = usuario;
	
	                SistemaSession session = (SistemaSession) Session.get();
	                session.setUsuarioLogado(usuario);
	                setResponsePage(HomePage.class);
	                return;
				}
            }
        }
    }
    
    private MarkupContainer markup;
    @EJB
    private SystemFacade systemFacade;

    private Usuario usuario;

    public LoginPage() {

        add(new LoginForm());
        
        markup = new MarkupContainer("msg.erro"){};
        markup.add(new FeedbackPanel("feedback"));
        markup.setOutputMarkupPlaceholderTag(true);
      
        add(markup);
        markup.setVisibilityAllowed(false);
        

        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, getLocale());

        LabelFrw lblDataAtual = new LabelFrw("dataAtual", "V1.2");
        add(lblDataAtual);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        String ctx = getRequest().getContextPath();
        response.render(new CssUrlReferenceHeaderItem(ctx + "/css/estilo.css", null, null));
    }
}
