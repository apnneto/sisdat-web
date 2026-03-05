package com.frw.base.web.pages.seguranca.filter;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.frw.base.negocio.quiz.PerguntaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.util.TextFieldFrw;
import com.frw.manutencao.dominio.dto.FilterPerguntaDescricaoDTO;

@SuppressWarnings("serial")
public class FilterListPerguntaDescricaoPanel extends Panel {

	private FilterPerguntaDescricaoDTO filterDTO;
	
	private BaseWebBeanForm<FilterPerguntaDescricaoDTO> form;
	@EJB 
	private PerguntaFacade perguntaFacade;
	
	public FilterListPerguntaDescricaoPanel(String id, final WebMarkupContainer listContainer, final FilterPerguntaDescricaoDTO filterDTO, PerguntaFacade perguntaFacade) {
		super(id);
		this.perguntaFacade = perguntaFacade;
		this.filterDTO = filterDTO;

		final FeedbackPanel messages = new FeedbackPanel("messages");
		messages.setOutputMarkupId(true);
		add(messages);

		form = new BaseWebBeanForm<FilterPerguntaDescricaoDTO>("filterForm");
		form.setEntity(filterDTO);
		add(form);

		addFilterFields();

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

	private void addFilterFields() {
		TextFieldFrw<String> txtperguntaDescricao = new TextFieldFrw<String>("perguntaDescricao");
		form.add(txtperguntaDescricao);
		
		TextFieldFrw<String> txtperguntaSubDescricao = new TextFieldFrw<String>("perguntaSubDescricao");
		form.add(txtperguntaSubDescricao);

	}


}

