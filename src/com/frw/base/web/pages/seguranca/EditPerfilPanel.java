package com.frw.base.web.pages.seguranca;

import java.util.Collections;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.dominio.base.Modulo;
import com.frw.base.dominio.base.Perfil;
import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.exception.SistemaException;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.base.AbstractEntityEditPanel;
import com.frw.base.web.pages.panel.MultipleSelectionPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.IndicatingAjaxDropDownChoice;
import com.frw.base.web.util.TextFieldFrw;
import com.frw.base.web.util.comparator.TipoUsuarioComparator;

/**
 * @author Leonardo Barros
 * @author 
 */
public class EditPerfilPanel extends AbstractEntityEditPanel<Perfil> {

    @EJB
    private CadastroFacade cadastroFacade;
    
    private LoadableDetachableModel<List<Funcionalidade>> funcionalidades;
    private MultipleSelectionPanel<Funcionalidade> selectPanel;
    
    public EditPerfilPanel(String id, UpdatableModalWindow confirmationModal, Perfil entity) {
        super(id, confirmationModal, entity);
        returnButton.setVisible(true);
    }

    @Override
    public void addFormFields(BaseWebBeanForm<Perfil> form, final Perfil entity) {

    	form.add(new TextFieldFrw("nome"));

        LoadableDetachableModel<List<TipoUsuario>> tiposUsuario = new LoadableDetachableModel<List<TipoUsuario>>() {
            @Override
            protected List<TipoUsuario> load() {
                List<TipoUsuario> tipos = cadastroFacade.pesquisarTodosTiposUsuario();
                Collections.sort(tipos, new TipoUsuarioComparator());
                return tipos;
            }
        };

        final DropDownChoice<TipoUsuario> tiposUsuarioChoice = new DropDownChoice("tipoUsuario", tiposUsuario);
        tiposUsuarioChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget art) {
            	entity.getFuncionalidades().clear();
                selectPanel.modelChanged();
                art.add(selectPanel);
            }
        });
        form.add(tiposUsuarioChoice);

        final Model<Modulo> moduloModel = new Model();
        IndicatingAjaxDropDownChoice<Modulo> moduloCmb = new IndicatingAjaxDropDownChoice<Modulo>("modulo", moduloModel, cadastroFacade.pesquisarTodosModulos());
        moduloCmb.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                selectPanel.modelChanged();
                art.add(selectPanel);
            }
        });
        form.add(moduloCmb);

        funcionalidades = new LoadableDetachableModel<List<Funcionalidade>>() {
            @Override
            protected List<Funcionalidade> load() {
               TipoUsuario tipoUsuario = tiposUsuarioChoice.getModelObject();
               if (tipoUsuario == null) {
                   return Collections.EMPTY_LIST;
               }
               if(moduloModel == null || moduloModel.getObject() == null) {
                    return cadastroFacade.pesquisarFuncionalidadesPorTipoUsuario(tipoUsuario);
               } else {
                    return cadastroFacade.pesquisarFuncionalidadesPorTipoUsuarioEModulo(tipoUsuario, moduloModel.getObject());
               }
            }
        };

//        IChoiceRenderer<Funcionalidade> choiceRenderFuncionalidade = new IChoiceRenderer<Funcionalidade>() {
//
//            @Override
//            public Object getDisplayValue(Funcionalidade t) {
//                return t.getModulo().getNome() + " - " + t.getDescricao();
//            }
//
//            @Override
//            public String getIdValue(Funcionalidade t, int i) {
//                return Integer.toString(i);
//            }
//        };

        selectPanel = new MultipleSelectionPanel<Funcionalidade>("selectPanel", funcionalidades, new PropertyModel(entity, "funcionalidades"));
        selectPanel.setOutputMarkupId(true);
        form.add(selectPanel);
    
    }

    @Override
    public Perfil deleteEntity(Perfil entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        cadastroFacade.excluirPerfil(entity);
        return entity;
    }

    @Override
    public Panel getEditEntityPanel(Perfil entity) {
        return new EditPerfilPanel("panel", confirmationModal, entity);
    }

    @Override
    public String getEntityDeleteSuccessMessage() {
        return "perfil.message.excluir.sucesso";
    }

    @Override
    public String getEntitySaveSuccessMessage() {
        return "perfil.message.salvar.sucesso";
    }

    @Override
    public Perfil newEntity(Perfil entity, AjaxRequestTarget target) throws SistemaException {
        final Perfil p = new Perfil();
        return p;
    }

    @Override
    public Perfil saveEntity(Perfil entity, AjaxRequestTarget target) throws SistemaException {
        SistemaSession.setUserAndCurrentDate(entity);
        cadastroFacade.salvarPerfil(entity);
        return entity;
    }
}
