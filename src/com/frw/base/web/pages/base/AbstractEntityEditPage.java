package com.frw.base.web.pages.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.exception.ForeignKeyViolationException;
import com.frw.base.exception.SistemaException;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.ValidationStyleBehavior;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.pages.util.ModalConfirmationPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AjaxButtonFrw;

/**
 * @author Carlos Henrique
 */
public abstract class AbstractEntityEditPage<T extends EntidadeDominioBase> extends BasePage {

    protected class EditEntityForm extends BaseWebBeanForm<T> {

        public EditEntityForm(String id, final T entity) {

            super(id);
            setEntity(entity);
            setEditMode(entity.getId() != null);
            setOutputMarkupId(true);

            container = new WebMarkupContainer("container");
            container.setOutputMarkupId(true);

            /** painel de mensagens */
            feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            /** cria botões padrão e adiciona no formulário */
            saveButton = createSaveButton(entity, confirmationModal);
            newButton = createNewButton(entity, confirmationModal);
            deleteButton = createDeleteButton(entity, confirmationModal);
            container.add(saveButton);
            container.add(newButton);
            container.add(deleteButton);
            Fragment fragment = new Fragment("botoesContainer", "botoesContainer",this);
            container.add(fragment);

            addContainerAditionalButtons(fragment, entity, feedback);
            addAditionalButtons(this, entity, feedback);

            /** configura os botões do formulário */
            updateButtons(entity);

            add(container);
            setDefaultBehaviour(new ValidationStyleBehavior());

        }

        protected AjaxButtonFrw createDeleteButton(final T entity, final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("excluir") {

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedback);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                    target.appendJavascript("Wicket.Window.unloadConfirmation=false");
                    confirmationModal.setContent(new ModalConfirmationPanel("content", "message.confirmation.delete") {

                        @Override
                        protected boolean onConfirm(AjaxRequestTarget target) {
                            try {
                                deleteEntitySuper(entity, target);
                                T newEntity = newEntity((T) entity.getClass().newInstance(), target);
                                setEntity(newEntity);
                                updateButtons(entity);
                                changeFormFieldsStatus(form, false);
                                changeTabsStatus(target, false);
                                updateComponents(target);
                            } catch(Exception e) {
                                e.printStackTrace();
                                error(getString("exception.delete"));
                                target.addComponent(feedback);
                            }
                            return true;
                        }
                    });
                    confirmationModal.show(target);
                }
            };
            button.setModel(new ResourceModel("botao.excluir"));
            button.setDefaultFormProcessing(false);
            return button;
        }

        protected AjaxButtonFrw createNewButton(final T entity, final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("novo") {

                @Override
                public void onSubmit(AjaxRequestTarget target, final Form<?> form) {
                    try {
                        T newEntity = newEntitySuper((T) entity.getClass().newInstance(), target);
                        setResponsePage(getEditEntityPage(newEntity));
                        changeFormFieldsStatus(form, true);
                        changeTabsStatus(target, false);
                        updateComponents(target);
                    } catch (SistemaException ex) {
                        ((BasePage) getPage()).ShowAlertMessage(getString(ex.getMessage()), target);
                    } catch (Exception e) {
                        ((BasePage) getPage()).ShowAlertMessage(e.getMessage(), target);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedback);
                }
            };
            button.setModel(new ResourceModel("botao.novo"));
            button.setDefaultFormProcessing(false);
            return button;

        }

        protected AjaxButtonFrw createSaveButton(final T entity, final UpdatableModalWindow confirmationWindow) {

            AjaxButtonFrw button = new AjaxButtonFrw("salvar") {

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    try {
                        saveEntitySuper(entity, target);
                        ((BasePage) getPage()).ShowAlertMessage(getString(getEntitySaveSuccessMessage()), target);
                        updateButtons(entity);
                        //changeFormFieldsStatus(form, true);
                        changeTabsStatus(target, true);
                        updateComponents(target);
                    } catch (SistemaException ex) {
                        ((BasePage) getPage()).ShowAlertMessage(getString(ex.getMessage()), target);
                    }
                }
            };
            button.setModel(new ResourceModel("botao.salvar"));
            return button;

        }

        protected void editEntity(T entity) {
            setResponsePage(getEditEntityPage(entity));
        }

    }
    protected WebMarkupContainer container;
    protected AjaxButtonFrw deleteButton;
    protected T entity;
    protected FeedbackPanel feedback;
    protected final BaseWebBeanForm<T> form;
    protected AjaxButtonFrw newButton;
    protected AjaxButtonFrw saveButton;
    protected AjaxTabbedPanel tabbedPanel;

    protected WebMarkupContainer uploadPanel;

    public AbstractEntityEditPage(T entityParam) {

        this.entity = entityParam;
        
        afterLoadPage();

        /** inicialmente, não serão criadas abas */
        List<ITab> tabs = getTabs(entity);
        tabbedPanel = new AjaxTabbedPanel("tabs", tabs);
        tabbedPanel.setOutputMarkupId(true);
        tabbedPanel.setEnabled(entity.getId() != null);
        tabbedPanel.setVisible(!tabs.isEmpty());
        add(tabbedPanel);
        

        /** cria e adiciona campos do formulário */
        form = createForm(entity);
        add(form);
        addFormFields(form, entity);
        form.setEditMode(entity.getId() != null);

        /** cria componente de upload */
        uploadPanel = new WebMarkupContainer("uploadPanel");
        add(uploadPanel);

        replaceUploadPanel();
    }

    public void deleteEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        try {
            deleteEntity(entity, target);
        } catch (RuntimeException e) {
            Throwable root = ExceptionUtils.getRootCause(e);
            if(root == null)
                root = e;
            if(root instanceof ForeignKeyViolationException) {
                error(getString(root.getMessage()));
                target.addComponent(feedback);
            } else {
                throw e;
            }
        }
    }

    public T newEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
      
        return newEntity(entity, target);
    }

    public void saveEntitySuper(T entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        saveEntity(entity, target);
    }

    private void replaceUploadPanel() {
        if(getUploadPanel() != null) {
            uploadPanel.replaceWith(getUploadPanel());
        }
    }

    protected void addAditionalButtons(Form form, T entity, FeedbackPanel feedback) {
    }

    protected void  addContainerAditionalButtons( Fragment fragment, T entity, FeedbackPanel feedback) {}

    protected  abstract void addFormFields(BaseWebBeanForm<T> form, T entity);

    protected void afterLoadPage(){

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
                    changeFormFieldsStatus(form, enabled);
                }
            }
            /** condição necessária para desabilitar botões em componentes filhos (ex.: botão browse do componente de upload */
            else if (!container.getClass().isAssignableFrom(Form.class)) {
                component.setEnabled(enabled);
            }

        }

    }

    protected void changeTabsStatus(AjaxRequestTarget target, boolean enabled) {
        tabbedPanel.setEnabled(!tabbedPanel.getTabs().isEmpty() && enabled);
        target.addComponent(tabbedPanel);
    }

    protected BaseWebBeanForm createForm(T entity) {
        return new EditEntityForm("form", entity);
    }

    protected  abstract void deleteEntity(T entity, AjaxRequestTarget target) throws SistemaException;

    protected  abstract Page getEditEntityPage(T entity);

    protected  abstract String getEntityDeleteSuccessMessage();

    protected  abstract String getEntitySaveSuccessMessage();
    protected List<ITab> getTabs(T entity) {
        return new ArrayList();
    }
    protected Panel getUploadPanel() {
        return null;
    }
    protected  abstract T newEntity(T entity, AjaxRequestTarget target) throws SistemaException;
    protected  abstract void saveEntity(T entity, AjaxRequestTarget target) throws SistemaException;
    protected void updateButtons(T entity) {
        newButton.setEnabled(true);
        saveButton.setEnabled(!entity.isExcluido());
        deleteButton.setEnabled(!entity.isExcluido() && entity.getId() != null);
    }
    protected void updateComponents(AjaxRequestTarget target) {
        target.addComponent(feedback);
        target.addComponent(container);
        target.addComponent(form);
        target.addComponent(tabbedPanel);
    }
}
