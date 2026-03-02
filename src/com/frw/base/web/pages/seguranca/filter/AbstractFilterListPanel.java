package com.frw.base.web.pages.seguranca.filter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.seguranca.filter.dto.AbstractFilterDTO;
import com.frw.base.web.pages.util.UpdatableModalWindow;

@SuppressWarnings("serial")
public abstract class AbstractFilterListPanel<T extends AbstractFilterDTO> extends Panel{

	private WebMarkupContainer listPanel;
	protected IndicatingAjaxButton btnConsultar;
	protected Boolean btnConsultarIsVisible = new Boolean(true);
	protected BaseWebBeanForm<T> form;
	protected final FeedbackPanel messages = new FeedbackPanel("messages");
	
	public AbstractFilterListPanel(String id, UpdatableModalWindow confirmationModal, T filterDTO, final WebMarkupContainer listContainer) {
		super(id);
		
		beforeLoadPage();
		
		this.listPanel = listContainer;
        messages.setOutputMarkupId(true);
        add(messages);

        form = new BaseWebBeanForm<T>("filterForm");
        form.setEntity(filterDTO);
        add(form);
        
        /* botao de pesquisa */
        btnConsultar = new IndicatingAjaxButton("buscarInformacoes") {
            @Override
            protected void onSubmit(AjaxRequestTarget art, Form<?> form) {
            	onSearch(art, form);
                art.addComponent(listPanel);
            }
        };
        btnConsultar.setModel(new ResourceModel("botao.buscarInformacoes"));
        btnConsultar.setVisible(btnConsultarIsVisible);
        form.add(btnConsultar);

        addComponents(form, confirmationModal, filterDTO);
	}
	
	public AbstractFilterListPanel(String id, UpdatableModalWindow confirmationModal, T filterDTO, final WebMarkupContainer listContainer, String strLabel) {
		super(id);
		
		loadStringLabel(strLabel);
		beforeLoadPage();
		
		this.listPanel = listContainer;
        messages.setOutputMarkupId(true);
        add(messages);

        form = new BaseWebBeanForm<T>("filterForm");
        form.setEntity(filterDTO);
        add(form);
        
        /* botao de pesquisa */
        btnConsultar = new IndicatingAjaxButton("buscarInformacoes") {
            @Override
            protected void onSubmit(AjaxRequestTarget art, Form<?> form) {
            	onSearch(art, form);
                art.addComponent(listPanel);
            }
        };
        btnConsultar.setModel(new ResourceModel("botao.buscarInformacoes"));
        btnConsultar.setVisible(btnConsultarIsVisible);
        form.add(btnConsultar);

        addComponents(form, confirmationModal, filterDTO);
	}
	
	public void updateFilterDTO(T filterDTO, AjaxRequestTarget ajax) {
		form.setEntity(filterDTO);
		ajax.addComponent(form);
	}


	protected abstract void addComponents(BaseWebBeanForm<T> form, UpdatableModalWindow confirmationModal, T filterDTO);

	protected void beforeLoadPage() {
		
	}
	
	protected void loadStringLabel(String str) {
		
	}

	protected void onSearch(AjaxRequestTarget art, Form<?> form) {}

}
