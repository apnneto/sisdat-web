package com.frw.base.web.pages.seguranca.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.AutoCompleteFieldFrw;
import com.frw.base.web.util.LabelFrw;
import com.frw.manutencao.dominio.dto.FilterListPerguntaDTO;

@SuppressWarnings("serial")
public class FilterListPerguntaPanel extends AbstractFilterListPanel<FilterListPerguntaDTO> {

	private FilterListPerguntaDTO filterDTO;

	private List<String> listQuestinarioPesquisados;
	private final int NUMERO_ELEMENTOS_POR_PAGINACAO = 10;
	@EJB private QuestionarioFacade questionarioFacade;
	
	private List<Questionario> questionariosSelecionados;
	
	public FilterListPerguntaPanel(String id,
			UpdatableModalWindow confirmationModal,
			FilterListPerguntaDTO filterDTO, WebMarkupContainer listContainer) {
		super(id, confirmationModal, filterDTO, listContainer);
		this.filterDTO = filterDTO;
	}
	
	private void addQuestinariosListContainer(final WebMarkupContainer questionariosListContainer, BaseWebBeanForm<FilterListPerguntaDTO> form) {
			
	    final LoadableDetachableModel<List<Questionario>> ldmQuestionarios = new LoadableDetachableModel<List<Questionario>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<Questionario> load() {
				Collections.sort(questionariosSelecionados);
				return questionariosSelecionados;
			}
		};
	
		PageableListView<Questionario> listViewQuestionarios = new PageableListView<Questionario>("listViewQuestionarios", ldmQuestionarios, NUMERO_ELEMENTOS_POR_PAGINACAO) {
			@Override
			protected void populateItem(final ListItem<Questionario> li) {
				li.add(new LabelFrw("questionario", new Model<String>(li.getModelObject().getDescricao())));
				
				IndicatingAjaxLink removeLink = new IndicatingAjaxLink("removeQuestionarioLink") {
	                @Override
	                public void onClick(AjaxRequestTarget target) {
	                	questionariosSelecionados.remove(li.getModelObject());
	                	target.add(questionariosListContainer);
	                }
	            };
	            
	            removeLink.add(new AttributeModifier("class", new Model<String>("icone excluir")));
	            li.add(removeLink);
			}
		};
		listViewQuestionarios.setOutputMarkupId(true);
		questionariosListContainer.add(listViewQuestionarios);
	}
	
	private Component createAutoCompleteQuestionario(final WebMarkupContainer questionariosListContainer) {
		AutoCompleteFieldFrw<String> acfQuestionario = new AutoCompleteFieldFrw<String>("acfQuestionario", new Model<String>("")){
	
			@Override
			public List<String> loadListChoices(String input) {
				listQuestinarioPesquisados = questionarioFacade.pesquisarAutoComplete(input);
				for (Questionario p : questionariosSelecionados) {
					if (listQuestinarioPesquisados.contains(p)) {
						listQuestinarioPesquisados.remove(p);
					}
				}
				return listQuestinarioPesquisados;
			}
	
			@Override
			public void onChange(AjaxRequestTarget target) {
				Questionario questionario =  questionarioFacade.buscarPorDescricaoCompleta(getModelObject());
				if(questionario != null && (!questionariosSelecionados.contains(questionario))) {
					questionariosSelecionados.add(questionario);
					target.add(questionariosListContainer);
				}
			}
			
	    };
	    acfQuestionario.setOutputMarkupId(true);
	    return acfQuestionario;
	}

	@Override
	protected void addComponents(BaseWebBeanForm<FilterListPerguntaDTO> form, UpdatableModalWindow confirmationModal, final FilterListPerguntaDTO filterDTO) {
		WebMarkupContainer palavrasListContainer = new WebMarkupContainer("perguntasListContainer");
		palavrasListContainer.setOutputMarkupId(true);
	    form.add(palavrasListContainer);
	    
		WebMarkupContainer questionariosListContainer = new WebMarkupContainer("questionariosListContainer");
		questionariosListContainer.setOutputMarkupId(true);
	    form.add(questionariosListContainer);
	    
		form.add(createAutoCompleteQuestionario(questionariosListContainer));
		addQuestinariosListContainer(questionariosListContainer, form);
		
	}
	
	@Override
	protected void beforeLoadPage() {
		questionariosSelecionados = new ArrayList<Questionario>();
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget art, Form<?> form) {
		filterDTO.setQuestionarios(questionariosSelecionados);
	}

}

