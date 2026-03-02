package com.frw.base.web.pages.cadastro;

import javax.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Resposta;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.quiz.RespostaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.TextAreaFrw;
import com.frw.base.web.util.TextFieldFrw;

/**
 * @author Igor Pessanha
 *
 */
public class EditRespostaPerguntaPanel extends AbstractEntityEditPanel<Resposta> {

	private static final long serialVersionUID = 1L;
	private ListRespostasPerguntaPanel panelList;
    private Pergunta pergunta;
    private Integer r;
    private Resposta resposta;
	@EJB
    private RespostaFacade respostaFacade;
	private TextFieldFrw txtOrdem;
 


    public EditRespostaPerguntaPanel(String id, UpdatableModalWindow confirmationModal, Resposta entity, Pergunta pergunta, ListRespostasPerguntaPanel panelList) {
        super(id, confirmationModal, entity);
        this.pergunta = pergunta;
        this.resposta = entity;
        this.panelList = panelList;
        returnButton.setVisible(true);
        loadOrdem();

    }


	@Override
    public void addFormFields(BaseWebBeanForm<Resposta> form, final Resposta entity) {

    	TextAreaFrw txtDescricao = new TextAreaFrw("descricao");

    	txtOrdem = new TextFieldFrw("ordem");

    	
        txtOrdem.setRequired(true);
        txtDescricao.setRequired(true);
        
        form.add(txtDescricao);
        form.add(txtOrdem);
       

    }

 
    @Override
    public Resposta deleteEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
    	 SistemaSession.setUserAndCurrentDate(entity);
    	 respostaFacade.excluirResposta(entity);
        return entity;
    }

    @Override
    public Panel getEditEntityPanel(Resposta entity) {
        return new EditRespostaPerguntaPanel("panel", confirmationModal, entity, pergunta, panelList);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "resposta.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "resposta.message.salvar.sucesso";
    }

	@Override
    public Resposta newEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
        final Resposta pojo = new Resposta();
        pesquisarOrdem();
        if (r!= null){
        	r= r+1;
        }else {
        	r=1;
        }
        pojo.setOrdem(r);
        return pojo;
    }
    
    @Override
    public Resposta saveEntity(Resposta entity, AjaxRequestTarget target) throws SistemaException {
    	 SistemaSession.setUserAndCurrentDate(entity);
    	 entity.setPergunta(pergunta);
    	 Resposta r =	respostaFacade.salvarResposta(entity);
    	 //setResponsePage(goToPage());
    	 return r;
    }

    private void loadOrdem() {
		pesquisarOrdem();
		if ((resposta.getId() == null) && (r != null)) {
			r = r + 1;
			resposta.setOrdem(r);
		} else {
			r = 1;
			resposta.setOrdem(r);
		}
	}

	private Integer pesquisarOrdem() {
		r = respostaFacade.pesquisarMaxOrdemPorPergunta(pergunta);
		return r;

	}
	
	@Override
    protected void onAfterSaveEntity(AjaxRequestTarget target) {
    	replaceWith(panelList);
    	setOutputMarkupId(true);
    	//setOutputMarkupPlaceholderTag(true);
    	target.addComponent(this);
    	target.addComponent(panelList);
    	
    }

	@Override
	protected void onBackPressed(AjaxRequestTarget target) {
		replaceWith(panelList);
    	setOutputMarkupId(true);
    	target.addComponent(this);
    	target.addComponent(panelList);
	}

	


}
