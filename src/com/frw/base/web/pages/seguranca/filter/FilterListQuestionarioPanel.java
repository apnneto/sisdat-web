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

import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.util.AutoCompleteFieldFrw;
import com.frw.manutencao.dominio.dto.FilterListQuestionarioDTO;

/**
 * 
 * @author Marcelo Alves
 */
@SuppressWarnings("serial")
public class FilterListQuestionarioPanel extends Panel {

	private FilterListQuestionarioDTO filterDTO;
	private BaseWebBeanForm<FilterListQuestionarioDTO> form;
	private QuestionarioFacade questionarioFacade;

	public FilterListQuestionarioPanel(String id, final WebMarkupContainer listContainer, final FilterListQuestionarioDTO filterDTO, QuestionarioFacade questionarioFacade) {
		super(id);
		this.questionarioFacade = questionarioFacade;
		this.filterDTO = filterDTO;

		final FeedbackPanel messages = new FeedbackPanel("messages");
		messages.setOutputMarkupId(true);
		add(messages);

		form = new BaseWebBeanForm<FilterListQuestionarioDTO>("filterForm");
		form.setEntity(filterDTO);
		add(form);

		addFilterFields();

		/* botão de pesquisa */
		IndicatingAjaxButton buscarInformacoes = new IndicatingAjaxButton(
				"buscarInformacoes") {
			@Override
			protected void onSubmit(AjaxRequestTarget art, Form<?> form) {
				art.addComponent(listContainer);
			}
		};
		buscarInformacoes
				.setModel(new ResourceModel("botao.buscarInformacoes"));
		form.add(buscarInformacoes);

	}

	private void addFilterFields() {
		AutoCompleteFieldFrw<String> acfNome = new AutoCompleteFieldFrw<String>( "descricao", new PropertyModel<String>(filterDTO, "descricao")) {
			@Override
			public List<String> loadListChoices(String descricao) {
				return questionarioFacade .pesquisaAutocompletePorDescricao(descricao);
			}
		};
		acfNome.setOutputMarkupId(true);
		form.add(acfNome);

	}

}
