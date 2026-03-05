package com.frw.base.web.pages.panel;

import jakarta.ejb.EJB;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.SystemFacade;
import com.frw.base.web.SistemaSession;

/**
 *
 * @author Leonardo Barros
 */
public class ChangePasswordPanel extends Panel {

    class ChangePasswordForm extends Form {

        private AjaxButton buttonAlterar;
        private PasswordTextField txtConfirmacaoSenhaNova;
        private PasswordTextField txtSenhaAtual;
        private PasswordTextField txtSenhaNova;

        public ChangePasswordForm(String id) {
            super(id);

            txtSenhaAtual = new PasswordTextField("txtSenhaAtual", new Model<String>());
            txtSenhaNova = new PasswordTextField("txtSenhaNova", new Model<String>());
            txtConfirmacaoSenhaNova = new PasswordTextField("txtConfirmacaoSenhaNova", new Model<String>());

            add(txtSenhaAtual);
            add(txtSenhaNova);
            add(txtConfirmacaoSenhaNova);

            buttonAlterar = new IndicatingAjaxButton("btnAlterarSenha") {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    try {

                        SistemaSession session = (SistemaSession) Session.get();
                        cadastroFacade.changeUserPassword(session.getUsuarioLogado(), txtSenhaAtual.getInput(), txtSenhaNova.getInput(), txtConfirmacaoSenhaNova.getInput());

                        info(getString("user.change.password.sucessful"));
                        target.add(feedbackPanel);

                    } catch (SistemaException pe) {
                        info(getString(pe.getMessage()));
                        pe.printStackTrace();
                        target.add(feedbackPanel);
                        return;
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, @SuppressWarnings("unused") Form<?> form) {
                    target.add(feedbackPanel);
                }
            };

            buttonAlterar.setModel(new ResourceModel("user.change.password.btn.alterar"));
            buttonAlterar.setDefaultFormProcessing(false);
            add(buttonAlterar);

        }
    }
    @EJB
    private SystemFacade cadastroFacade;

    private ChangePasswordForm changePasswordForm;

    private FeedbackPanel feedbackPanel;

    public ChangePasswordPanel(String id) {
        super(id);

        feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        feedbackPanel.setOutputMarkupId(true);
        
        changePasswordForm = new ChangePasswordForm("changePasswordForm");
        add(changePasswordForm);
    }
}
