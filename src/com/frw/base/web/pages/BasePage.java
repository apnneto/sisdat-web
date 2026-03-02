package com.frw.base.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.menu.SideMenu;
import com.frw.base.web.pages.menu.TopMenu;
import com.frw.base.web.pages.panel.ChangePasswordPanel;
import com.frw.base.web.pages.util.ModalAlertPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;

/**
 * Página base da aplicação Wicket
 * Provê o look & feel básico e funcionalidades básicas
 * @author juliano
 */
public class BasePage extends WebPage {

    public static final String PARAMETER = "param";
    public UpdatableModalWindow confirmationModal;
    private WebMarkupContainer containerMenuVertical, containerMenuConteudo;
    private String result;
    
    private String tituloKey;

    protected SideMenu sideMenu;

    protected LabelFrw tituloPageLabel;

    public BasePage() {

        SistemaSession session = (SistemaSession) Session.get();

        Link logoutLink = new Link("logout") {

            @Override
            public void onClick() {
                Session.get().invalidate();
                getRequestCycle().setRequestTarget(new RedirectRequestTarget("/sisdat-web/") );
            }
        };

        add(logoutLink);

        AjaxLink changePasswordLink = new AjaxLink("changePasswordLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavascript("Wicket.Window.unloadConfirmation=false");
                confirmationModal.setContent(new ChangePasswordPanel(confirmationModal.getContentId()));
                confirmationModal.setInitialHeight(300);
                confirmationModal.setInitialWidth(450);
                confirmationModal.setTitle("Alterar Senha");
                confirmationModal.show(target);
            }
        };

        
        add(changePasswordLink);


        LabelFrw usuario = new LabelFrw("usuario", session.getUsuarioLogado() != null ? session.getUsuarioLogado().getNome() : "");
        add(usuario);

        List<Locale> languages = new ArrayList<Locale>();
        languages.add(Locale.US);
        languages.add(new Locale("pt", "BR"));

        DropDownChoice<Locale> language = new DropDownChoice<Locale>("language", languages) {

            @Override
            protected void onSelectionChanged(Locale newSelection) {

                Session session = Session.get();
                session.setLocale(getModelObject());
                setResponsePage(HomePage.class, new PageParameters("nome=teste"));
            }

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };

        language.setModel(new Model<Locale>());

        add(language);
        language.setVisible(false);

        tituloPageLabel = new LabelFrw("tituloPage", new ResourceModel(getTituloKey()));

        add(tituloPageLabel);
        confirmationModal = new UpdatableModalWindow("modalConfirmation");

        add(confirmationModal);

        confirmationModal.setInitialHeight(170);

        confirmationModal.setInitialWidth(400);
        containerMenuConteudo = new WebMarkupContainer("containerMenuConteudo");

        add(containerMenuConteudo);

        containerMenuConteudo.setOutputMarkupId(true);

        containerMenuVertical = new WebMarkupContainer("containerMenuVertical");

        containerMenuConteudo.add(containerMenuVertical);

        containerMenuVertical.setOutputMarkupId(true);

        containerMenuVertical.setMarkupId("containerMenuVertical");
        
        TopMenu topMenu = new TopMenu("topMenu");

        add(topMenu);
        sideMenu = new SideMenu("sideMenu", getUsuarioLogado());

        containerMenuVertical.add(sideMenu);

    }

    public AttributeModifier getAttributeModifierLinhaPautada(final ListItem item) {

        AttributeModifier attr = new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return (item.getIndex() % 2 == 1) ? "linha1" : "linha2";


            }
        });



        return attr;


    }
    public SistemaSession getParceriasSession() {
        return (SistemaSession) Session.get();
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;


    }


    public SideMenu getSideMenu() {
        return sideMenu;
    }

    public Usuario getUsuarioLogado() {
        SistemaSession session = (SistemaSession) Session.get();
        return session.getUsuarioLogado();
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(String result) {
        this.result = result;


    }

    public void setSideMenu(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public void ShowAlertMessage(String message, AjaxRequestTarget target) {
        ShowAlertMessage(message, target, confirmationModal);


    }

    public void ShowAlertMessage(String message, AjaxRequestTarget target, Page responsePage) {
        ShowAlertMessage(message, target, confirmationModal, responsePage);


    }

    public void ShowAlertMessage(String message, AjaxRequestTarget target, UpdatableModalWindow modal) {
        ShowAlertMessage(message, target, modal, null);


    }

    public void ShowAlertMessage(String message, AjaxRequestTarget target, UpdatableModalWindow modal, final Page responsePage) {
        modal.setContent(new ModalAlertPanel(modal.getContentId(), message));
        modal.show(target);

        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            @Override
            public void onClose(AjaxRequestTarget art) {
                if (responsePage != null) {
                    setResponsePage(responsePage);
                }


            }
        });


    }

    /* forcar recarregamento de pagina caso usuario utilize os botoes do browser
    * de avancar e retornar. */
    @Override
    protected void configureResponse() {
    super.configureResponse();
    WebResponse response = getWebRequestCycle().getWebResponse();
    response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store");
    response.setHeader("Expires","-1");
    response.setHeader("Pragma","no-cache");
    }
    protected HttpServletResponse getResponseFrw() {
        HttpServletResponse response = ((BasePage) getPage()).getWebRequestCycle().getWebResponse().getHttpServletResponse();


        return response;


    }

    protected String getTituloKey() {
        return "titulo.basico";


    }
    
    protected void setTitulo(String messageKey) {
        tituloKey = messageKey;


    }
}
