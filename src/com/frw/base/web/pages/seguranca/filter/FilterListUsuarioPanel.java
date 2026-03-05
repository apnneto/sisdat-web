/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.pages.seguranca.filter;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.negocio.CadastroFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.util.AutoCompleteFieldFrw;
import com.frw.manutencao.dominio.dto.FilterUsuarioDTO;

/**
 *
 * @author Marcelo Alves
 */
@SuppressWarnings("serial")
public class FilterListUsuarioPanel extends Panel {

	private CadastroFacade cadastroFacade;
	private FilterUsuarioDTO filterDTO;
	private BaseWebBeanForm<FilterUsuarioDTO> form;
	
     public FilterListUsuarioPanel(String id, final WebMarkupContainer listContainer, final FilterUsuarioDTO filterDTO, final CadastroFacade cadastroFacade) {
        super(id);

        this.cadastroFacade = cadastroFacade;
        this.filterDTO = filterDTO;
        
        final FeedbackPanel messages = new FeedbackPanel("messages");
        messages.setOutputMarkupId(true);
        add(messages);

        form = new BaseWebBeanForm<FilterUsuarioDTO>("filterForm");
        form.setEntity(filterDTO);
        add(form);

        addFilterFields(filterDTO, form);

        /* botão de pesquisa */
        IndicatingAjaxButton buscarInformacoes = new IndicatingAjaxButton("buscarInformacoes") {
            @Override
            protected void onSubmit(AjaxRequestTarget art) {
                art.add(listContainer);
            }
        };
        buscarInformacoes.setModel(new ResourceModel("botao.buscarInformacoes"));
        form.add(buscarInformacoes);

     }

	private void addFilterFields(FilterUsuarioDTO filterDTO, BaseWebBeanForm<FilterUsuarioDTO> form) {

//      form.add(new TextFieldFrw("login"));
//      form.add(new TextFieldFrw("nome"));
		AutoCompleteFieldFrw<String> acfLogin = new AutoCompleteFieldFrw<String>("login", new PropertyModel<String>(filterDTO, "login")) {
			@Override
			public List<String> loadListChoices(String login) {
				return cadastroFacade.pesquisaAutocompletePorLogin(login);
			}
		};
		
		acfLogin.setOutputMarkupId(true);
		form.add(acfLogin);
		
		
		AutoCompleteFieldFrw<String> acfNome = new AutoCompleteFieldFrw<String>("nome", new PropertyModel<String>(filterDTO, "nome")) {
			@Override
			public List<String> loadListChoices(String nome) {
				return cadastroFacade.pesquisaAutocompletePorNome(nome);
			}
		};
		acfNome.setOutputMarkupId(true);
		form.add(acfNome);
	}

}
