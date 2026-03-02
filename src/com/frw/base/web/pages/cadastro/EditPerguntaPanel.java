package com.frw.base.web.pages.cadastro;

import javax.ejb.EJB;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.TipoDado;
import com.frw.base.dominio.sisdat.TipoPergunta;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.PerguntaFacade;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextAreaFrw;
import com.frw.base.web.util.TextFieldFrw;

@SuppressWarnings("serial")
public class EditPerguntaPanel extends AbstractEntityEditPanel<Pergunta> {

	private ListRespostasPerguntaPanel listRespostas;
	private ListPerguntaPanel panelList;

	@EJB
	private PerguntaFacade perguntaFacade;
	private Questionario questionario;
	@EJB
	private QuestionarioFacade questionarioFacade;
	private TextFieldFrw<Integer> txtQuadrante;

	public EditPerguntaPanel(String id, UpdatableModalWindow confirmationModal,Pergunta entity, ListPerguntaPanel panelList,Questionario questionario) {
		super(id, confirmationModal, entity);
		this.panelList = panelList;
		this.questionario = questionario;
		entity.setQuestionario(questionario);
		returnButton.setVisible(true);
	}

	@Override
	public void addFormFields(final BaseWebBeanForm<Pergunta> form,
			final Pergunta entity) {

		TextAreaFrw<String> txtDescricao = new TextAreaFrw<String>("descricao");
		TextAreaFrw<String> txtSubdescricao = new TextAreaFrw<String>("subdescricao");
		TextAreaFrw<String> txtComentario = new TextAreaFrw<String>("comentario");
		TextAreaFrw<String> txtDescricaoResumida = new TextAreaFrw<String>("descricaoResumida");
		txtQuadrante = new TextFieldFrw<Integer>("quadrante");
		txtQuadrante.setVisible(false);
		form.add(txtQuadrante);
		
		TextFieldFrw<Integer> txtOrdem = new TextFieldFrw<Integer>("ordem");
		txtOrdem.setRequired(true);
		form.add(txtOrdem);

		// Drop Down Questionario
		IChoiceRenderer<Questionario> iChoiceQuestionario = new IChoiceRenderer<Questionario>() {
			@Override
			public Object getDisplayValue(Questionario arg0) {
				return arg0.getDescricao();
			}

			@Override
			public String getIdValue(Questionario arg0, int arg1) {
				return arg0.getId().toString();
			}
		};
		DropDownChoice<Questionario> txtQuestionario = new DropDownChoice<Questionario>( "questionario", questionarioFacade.buscarTodosQuestionarios(), iChoiceQuestionario);
		txtQuestionario.setRequired(true);
		txtQuestionario.setEnabled(false);
		form.add(txtQuestionario);

		txtDescricao.setRequired(true);

		form.add(txtDescricao);
		form.add(txtSubdescricao);
		form.add(txtComentario);
		form.add(txtDescricaoResumida);
		form.add(createListRespostas());

		// Drop Down TipoPergunta
		IChoiceRenderer<TipoPergunta> iChoiceTipoPergunta = new IChoiceRenderer<TipoPergunta>() {
			@Override
			public Object getDisplayValue(TipoPergunta arg0) {
				return arg0.getDescricao();
			}

			@Override
			public String getIdValue(TipoPergunta arg0, int arg1) {
				return arg0.getId().toString();
			}
		};
		DropDownChoice<TipoPergunta> txtTipo = new DropDownChoice<TipoPergunta>("tipo", perguntaFacade.buscarTodosTipoPerguntas(),iChoiceTipoPergunta);
		txtTipo.setRequired(true);
		form.add(txtTipo);

		// drop down tipo de Dado
		IChoiceRenderer<TipoDado> iChoiceTipoDado = new IChoiceRenderer<TipoDado>() {
			@Override
			public Object getDisplayValue(TipoDado arg0) {
				return arg0.getDescricao();
			}

			@Override
			public String getIdValue(TipoDado arg0, int arg1) {
				return arg0.getId().toString();
			}
		};
		DropDownChoice<TipoDado> ddcTipoDado = new DropDownChoice<TipoDado>( "tipoDado", questionarioFacade.buscarTodosTiposDado(),iChoiceTipoDado);
		ddcTipoDado.setRequired(true);
		form.add(ddcTipoDado);

		// drop down tipoLov
		/*IChoiceRenderer<TipoLov> iChoiceTipoLov = new IChoiceRenderer<TipoLov>() {
			@Override
			public Object getDisplayValue(TipoLov arg0) {
				return arg0.getDescricao();
			}

			@Override
			public String getIdValue(TipoLov arg0, int arg1) {
				return arg0.getId().toString();
			}
		};
		DropDownChoice<TipoLov> txtTipoLov = new DropDownChoice<TipoLov>( "tipoLov", perguntaFacade.buscarTodosTipoLov(), iChoiceTipoLov);
		form.add(txtTipoLov);*/
		
		CheckBox chkObrigatoria = new CheckBox("obrigatoria");
		CheckBox chkMultiplasLinhas = new CheckBox("multiplasLinhas");
		
		form.add(chkObrigatoria);
        form.add(chkMultiplasLinhas);

	}

	@Override
	public Pergunta deleteEntity(Pergunta entity, AjaxRequestTarget target) throws SistemaException {
		perguntaFacade.excluirPergunta(entity);
		return entity;
	}

	@Override
	public Panel getEditEntityPanel(Pergunta entity) {
		return new EditPerguntaPanel("panel", confirmationModal, entity, panelList, questionario);
	}

	@Override
	public String getEntityDeleteSuccessMessage() {
		return "pergunta.message.excluir.sucesso";
	}

	@Override
	public String getEntitySaveSuccessMessage() {
		return "pergunta.message.salvar.sucesso";
	}

	public Pergunta getPergunta() {
		return entity;
	}

	@Override
	public Pergunta newEntity(Pergunta entity, AjaxRequestTarget target) throws SistemaException {
		Pergunta pojo = new Pergunta();
		pojo.setQuestionario(questionario);
		target.addComponent(listRespostas);
		return pojo;
	}

	@Override
	public Pergunta saveEntity(Pergunta entity, AjaxRequestTarget target)
			throws SistemaException {
		Pergunta pergunta = perguntaFacade .salvarPerguntaComPalavrasChaves(entity);
		target.addComponent(listRespostas);
		return pergunta;
	}

	private Component createListRespostas() {
		listRespostas = new ListRespostasPerguntaPanel("listRespostas",confirmationModal, entity, entity.getTipo(), entity.getTipoDado(), questionarioFacade.isUsuarioAdministrator(getUsuarioLogado()));
		return listRespostas;
	}

	@Override
	protected void onAfterDeletEntity(AjaxRequestTarget target) {
		replaceWith(panelList);
		setOutputMarkupId(true);
		target.addComponent(this);
		target.addComponent(panelList);
	}

	@Override
	protected void onAfterSaveEntity(AjaxRequestTarget target) {
		feedback.setVisible(false);
		target.addComponent(feedback);
	}

	@Override
	protected void onBackPressed(AjaxRequestTarget target) {
		replaceWith(panelList);
		setOutputMarkupId(true);
		target.addComponent(this);
		target.addComponent(panelList);
	}
	
	@Override
	protected boolean validateEntity(Pergunta entity, AjaxRequestTarget target)
			throws SistemaException {
		Boolean validate = new Boolean(true);
		
		/*if(txtQuadrante.getModelObject() != null  && txtQuadrante.getModelObject() > 0){
			validate = true;
		}else{
			error("O campo 'quadrante' deve ser maior que '0'.");
		}*/
		
		feedback.setVisible(!validate);
		target.addComponent(feedback);
		
		return validate;
	}

}
