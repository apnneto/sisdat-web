package com.frw.base.web.pages.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.exception.ForeignKeyViolationException;
import com.frw.base.exception.SistemaException;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.ValidationStyleBehavior;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.pages.util.UpdatableModalWindowPanel;
import com.frw.base.web.util.AjaxButtonFrw;


/**
 * @author Carlos Henrique
 */
public abstract class AbstractEntityEditPanel<T extends EntidadeDominioBase> extends Panel {//EntidadeDominioBase> extends Panel {

    class EditEntityForm extends BaseWebBeanForm<T> {

        public EditEntityForm(String id, final T entity) {

            super(id);
            this.setEntity(entity);
            setEditMode(entity.getId() != null);
            setOutputMarkupId(true);

            container = new WebMarkupContainer("container");
            container.setOutputMarkupId(true);

            /** painel de mensagens */
            feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            feedback.setOutputMarkupPlaceholderTag(true);
            feedback.setVisible(false);
            
            add(feedback);

            /** cria botões padrão e adiciona no formulário */
            saveButton = createSaveButton(confirmationModal);
            newButton = createNewButton(confirmationModal);
            deleteButton = createDeleteButton(confirmationModal);
            returnButton = createReturnButton(confirmationModal);
            container.add(saveButton);
            container.add(newButton);
            container.add(deleteButton);
            container.add(returnButton);

            setVisibleContainnerButtons();
            Fragment fragment = new Fragment("botoesContainer", "botoesContainer", this);
            container.add(fragment);


            addContainerAditionalButtons(fragment, entity, feedback);

           


            add(container);
            setDefaultBehaviour(new ValidationStyleBehavior());

        }

        protected AjaxButtonFrw createDeleteButton(final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("excluir") {

                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.add(feedback);
                    feedback.setVisible(true);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target) {

                    /* não tenta abrir mensagem modal se este painel for um popup */
                    if (!getParent().getParent().getParent().getParent().getClass().isAssignableFrom(UpdatableModalWindowPanel.class)) {
                        target.appendJavaScript("Wicket.Window.unloadConfirmation=false");
                        confirmationModal.setContent(new ModalConfirmationPanel("content", "message.confirmation.delete") {
                            @Override
                            protected boolean onConfirm(AjaxRequestTarget target) {
                                try {
                                    deleteEntity(target, entity, getForm());
                                    onAfterDeletEntity(target);
                                } catch (RuntimeException e) {
                                    Throwable root = ExceptionUtils.getRootCause(e);
                                    if(root instanceof ForeignKeyViolationException) {
                                        error(getString(root.getMessage()));
                                        target.add(feedback);
                                        feedback.setVisible(true);
                                        UpdatableModalWindow.closeCurrent(target);
                                        return false;
                                    }
                                    throw e;
                                }
                                return true;
                            }
                        });
                        confirmationModal.show(target);
                    } else {
                        try {
                            deleteEntity(target, entity, getForm());
                        } catch (RuntimeException e) {
                            Throwable root = ExceptionUtils.getRootCause(e);
                            if(root instanceof ForeignKeyViolationException) {
                                error(getString(root.getMessage()));
                                feedback.setVisible(true);
                                target.add(feedback);
                            } else {
                                throw e;
                            }
                        }
                    }

                }
            };
            button.setDefaultFormProcessing(false);
            button.setModel(new ResourceModel("botao.excluir"));
            return button;
        }

        protected AjaxButtonFrw createNewButton(final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("novo") {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    try {

                        entity = AbstractEntityEditPanel.this.newEntity(getEntity(), target);
                        setEntity(entity);
                        onAfterNewEntityForm(target);
                        deleteButton.setEnabled(false);
                        feedback.setVisible(false);
                        target.add(EditEntityForm.this);

                    } catch (Exception e) {
                    	e.printStackTrace();
                        ((BasePage) getPage()).ShowAlertMessage(e.getMessage(), target);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.add(feedback);
                    feedback.setVisible(true);
                }
            };
            button.setModel(new ResourceModel("botao.novo"));
            button.setDefaultFormProcessing(false);

            return button;

        }

        protected AjaxButtonFrw createReturnButton(final UpdatableModalWindow confirmationWindow) {
       	 
        	AjaxButtonFrw button = new AjaxButtonFrw("voltar") {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						onBackPressed(target);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
	        };
        
        button.setModel(new ResourceModel("botao.voltar"));
        button.setDefaultFormProcessing(false);
        button.setVisible(false);
        return button;
        	
        }
        
        protected AjaxButtonFrw createSaveButton(final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("salvar") {

                @Override
                protected void onError(AjaxRequestTarget target) {
                	feedback.setVisible(true);
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    try {
                        if (validateEntity(getEntity(), target)) {
                            entity = saveEntitySuper(getEntity(), target);
                            webBeanForm.setEntity(entity);

                            /* não tenta abrir mensagem modal se este painel for um popup */
                            if (!getParent().getParent().getParent().getParent().getClass().isAssignableFrom(UpdatableModalWindowPanel.class)) {
                                ((BasePage) getPage()).ShowAlertMessage(getString(getEntitySaveSuccessMessage()), target);
                                confirmationModal.setWindowClosedCallback((new UpdatableModalWindow.WindowClosedCallback() {
                                    @Override
                                    public void onClose(AjaxRequestTarget art) {
                                        updateButtons(getEntity());
                                        //changeFormFieldsStatus(form, true);
                                        changeTabsStatus(art, true);
                                        updateComponents(art);
                                    }
                                }));
                            } else {
                                updateButtons(getEntity());
                                //changeFormFieldsStatus(form, true);
                                changeTabsStatus(target, true);
                                updateComponents(target);
                                info(getString(getEntitySaveSuccessMessage()));
                            }
                            feedback.setVisible(false);
                            target.add(feedback);
                            deleteButton.setEnabled(true);
                            onAfterSaveEntity(target);
                        }
                    } catch (SistemaException ex) {
                        if (!getParent().getParent().getParent().getParent().getClass().isAssignableFrom(UpdatableModalWindowPanel.class)) {
                            ((BasePage) getPage()).ShowAlertMessage(getString(ex.getMessage()), target);
                            feedback.setVisible(false);
                        } else {
                            error(ex.getMessage());
                            feedback.setVisible(true);
                        }
                        
                        target.add(feedback);
                    }
                }
            };
            button.setModel(new ResourceModel("botao.salvar"));

            return button;

        }


        protected void editEntity(T entity) {
            try {
                this.getParent().replaceWith(getEditEntityPanel(entity));
            } catch (Exception ex) {
                Logger.getLogger(AbstractEntityEditPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

		protected T newEntity(Class entity) throws SistemaException {
            T newEntity = null;
            deleteButton.setEnabled(false);
            try {
                newEntity = (T) entity.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return newEntity;
        }

    }
    public Boolean isContainnerButtonsVisible = true;
    protected UpdatableModalWindow confirmationModal;
    protected WebMarkupContainer container;
    protected AjaxButtonFrw deleteButton;
    protected T entity;
    protected FeedbackPanel feedback;
    protected Boolean isDeleteButtonVisible = true;
    protected Boolean isNewButtonVisible = true;
    protected Boolean isSaveButtonVisible = true; 
    protected AjaxButtonFrw newButton;
    protected AjaxButtonFrw returnButton;
    protected AjaxButtonFrw saveButton;
    protected AjaxTabbedPanel tabbedPanel;
    protected WebMarkupContainer uploadPanel;

    protected BaseWebBeanForm<T> webBeanForm;

    public AbstractEntityEditPanel(String id, UpdatableModalWindow confirmationModal, T entity) {

        super(id);

        this.confirmationModal = confirmationModal;
        this.entity = entity;

        beforeLoadPage();

        try {

            /** inicialmente, não serão criadas abas */
            List<ITab> tabs = getTabs(entity);
            tabbedPanel = new AjaxTabbedPanel("tabs", tabs);
            tabbedPanel.setOutputMarkupId(true);
            tabbedPanel.setVisible(!tabs.isEmpty());
            tabbedPanel.setEnabled(entity.getId() != null);
            add(tabbedPanel);

        } catch (Exception ex) {
            Logger.getLogger(AbstractEntityEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        /** cria e adiciona campos do formulário */
        webBeanForm = createForm(entity);
        add(webBeanForm);
        addFormFields(webBeanForm, entity);
        webBeanForm.setEditMode(entity.getId() != null);

        /** configura os botões do formulário */
        updateButtons(entity);

        /** cria componente de upload */
        uploadPanel = new WebMarkupContainer("uploadPanel");
        add(uploadPanel);

        replaceUploadPanel();

    }

    public T deleteEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        return deleteEntity(entity, target);
    }

    public T newEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
        return newEntity(entity, target);
    }

    public T saveEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        return saveEntity(entity, target);
    }

    public void setVisibleContainnerButtons(){
        container.setVisible(isContainnerButtonsVisible);
    }

    private void replaceUploadPanel() {
        if (getUploadPanel() != null) {
            uploadPanel.replaceWith(getUploadPanel());
        }
    }

    protected void addContainerAditionalButtons(Fragment fragment, T entity, FeedbackPanel feedback) {

    }

    protected abstract void addFormFields(BaseWebBeanForm<T> form, T entity);

    protected void beforeLoadPage() {
    }

    protected void changeFormFieldsStatus(WebMarkupContainer container, boolean enabled) {

        Iterator<? extends Component> iterator = container.iterator();
        for (Component component; iterator.hasNext();) {

            component = iterator.next();

            /** desabilita todos os componentes que não forem botões */
            if (!component.getClass().isAssignableFrom(Button.class)) {
                component.setEnabled(enabled);

                /** se o componente for um container, desabilita os componentes filhos */
                if (component.getClass().isAssignableFrom(WebMarkupContainer.class)) {
                    changeFormFieldsStatus(webBeanForm, enabled);
                }
            } /** condição necessária para desabilitar botões em componentes filhos (ex.: botão browse do componente de upload */
            else if (!container.getClass().isAssignableFrom(Form.class)) {
                component.setEnabled(enabled);
            }

        }

    }

    protected void changeTabsStatus(AjaxRequestTarget target, boolean enabled) {
        tabbedPanel.setEnabled(tabbedPanel.getTabs().size() > 0 && enabled);
        target.add(tabbedPanel);
    }

    protected BaseWebBeanForm createForm(T entity) {
        BaseWebBeanForm form = new EditEntityForm("form", entity);
        form.setOutputMarkupId(Boolean.TRUE);
        return form;
    }

    protected void deleteEntity(AjaxRequestTarget target, T entity, Form form) {
      try {
            entity = deleteEntitySuper(entity, target);
            webBeanForm.setEntity(entity);
            changeFormFieldsStatus(form, false);
            updateButtons(entity);
            changeTabsStatus(target, false);
            updateComponents(target);
        } catch (SistemaException ex) {
            if (!getParent().getParent().getClass().isAssignableFrom(UpdatableModalWindowPanel.class)) {
                ((BasePage) getPage()).ShowAlertMessage(getString(ex.getMessage()), target);
            } else {
                error(ex.getMessage());
            }
        }
    }

    protected abstract T deleteEntity(T entity, AjaxRequestTarget target) throws SistemaException;

    protected abstract Panel getEditEntityPanel(T entity) throws Exception;

    protected abstract String getEntityDeleteSuccessMessage();

    

    protected abstract String getEntitySaveSuccessMessage();

    protected List<ITab> getTabs(T entity) {
        return new ArrayList();
    }

    protected Panel getUploadPanel() {
        return null;
    }

    protected Usuario getUsuarioLogado() {
        SistemaSession session = (SistemaSession) Session.get();
        return session.getUsuarioLogado();
    }

    protected abstract T newEntity(T entity, AjaxRequestTarget target) throws SistemaException;

    protected void onAfterDeletEntity(AjaxRequestTarget target) {}

    protected void onAfterNewEntityForm(AjaxRequestTarget target) {}

    protected void onAfterSaveEntity(AjaxRequestTarget target) {}

    protected void onBackPressed(AjaxRequestTarget target) throws InstantiationException, IllegalAccessException {
    	setResponsePage(returnToPage(target));
    }
    
    protected WebPage returnToPage(AjaxRequestTarget ajax) throws InstantiationException, IllegalAccessException {
    	return ((BasePageWithPanelContent) getPage()).getClass().newInstance();
	}
    protected abstract T saveEntity(T entity, AjaxRequestTarget target) throws SistemaException;
   
    protected void updateButtons(T entity) {
        newButton.setEnabled(true);
        saveButton.setEnabled(!entity.isExcluido());
//        saveButton.setEnabled(true);
        deleteButton.setEnabled(!entity.isExcluido() && entity.getId() != null);
//        deleteButton.setEnabled(entity.getId() != null);
        returnButton.setEnabled(true);
    }
    protected void updateComponents(AjaxRequestTarget target) {
        target.add(feedback);
        target.add(container);
        target.add(webBeanForm);
        target.add(tabbedPanel);
    }
    protected boolean validateEntity(T entity, AjaxRequestTarget target) throws SistemaException {
        return true;
    }
}
