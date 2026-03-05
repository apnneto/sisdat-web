package com.frw.base.web.pages.seguranca.filter;

import java.util.Date;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.negocio.CadastroFacade;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.web.BaseWebBeanForm;
import com.frw.base.web.SistemaSession;
import com.frw.base.web.pages.util.DateTimeTextFieldCustom;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.LabelFrw;
import com.frw.manutencao.dominio.dto.FilterListRespostaPesquisaDTO;
/**
 * 
 * @author Marcos Lisboa
 *
 */
public class FilterListRespostaPesquisaPanel extends AbstractFilterListPanel<FilterListRespostaPesquisaDTO> {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private CadastroFacade cadastroFacade;
	
	private DropDownChoice<Empresa> empresaChoice;
	
	@EJB
	private EmpresaFacade empresaFacade;
	
	private FilterListRespostaPesquisaDTO filterDTO; 
	private List<Empresa> listEmpresas; 
	private List<Usuario> listUsuarios;
	private String strLabel;
	private DateTimeTextFieldCustom txtDataFim;
	private DateTimeTextFieldCustom txtDataInicio;
	private DropDownChoice<Usuario> usuarioChoice;
	
	public FilterListRespostaPesquisaPanel(String id, UpdatableModalWindow confirmationModal, WebMarkupContainer listContainer, FilterListRespostaPesquisaDTO filterDTO, String strLabel) {
		super(id, confirmationModal, filterDTO, listContainer, strLabel);
		this.filterDTO = filterDTO;
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}
	
	private void buscarDados() {
		
		if (getUsuarioLogado() != null && getUsuarioLogado().getTipoUsuario() != null && getUsuarioLogado().getTipoUsuario().getId().equals(TipoUsuario.ADM)) {
			listEmpresas = empresaFacade.buscarTodas();
			if(filterDTO != null && filterDTO.getEmpresa() != null)
				listUsuarios = cadastroFacade.buscarUsuariosPorEmpresa(filterDTO.getEmpresa());
			else
				listUsuarios = getUsuariosComEmpresa();
		}else{
			 listEmpresas = empresaFacade.pesquisarEmpresaPorId(getUsuarioLogado().getEmpresa());
			 listUsuarios = getUsuariosPorEmpresa();
		}
	}
	
	private Usuario getUsuarioLogado() {
		return ((SistemaSession)getSession()).getUsuarioLogado();

	};
	
	private List<Usuario> getUsuariosComEmpresa() {
		return cadastroFacade.buscarTodosUsuariosComEmpresa();
	}
	
	private List<Usuario> getUsuariosPorEmpresa() {
		return cadastroFacade.buscarUsuariosPorEmpresa(getUsuarioLogado().getEmpresa());
	}

	@Override
	protected void addComponents(BaseWebBeanForm<FilterListRespostaPesquisaDTO> form, UpdatableModalWindow confirmationModal, final FilterListRespostaPesquisaDTO filterDTO) {
		LabelFrw labelQuestionario = new LabelFrw("labelQuestionario",strLabel != null ? strLabel: "");
		labelQuestionario.setOutputMarkupId(true);
		labelQuestionario.setOutputMarkupPlaceholderTag(true);
		form.add(labelQuestionario);
		
		buscarDados();
		
		IChoiceRenderer<Empresa> iEmpresaChoice = new IChoiceRenderer<Empresa>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Empresa u) {
				return u.getRazaoSocial() ;
			}

			@Override
			public String getIdValue(Empresa arg, int arg1) {
				return arg.getId().toString();
			}
		};
        
        empresaChoice = new DropDownChoice<Empresa>("empresa", listEmpresas, iEmpresaChoice);
        empresaChoice.setNullValid(true);
        empresaChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
            protected void onUpdate(AjaxRequestTarget art) {
               filterDTO.setEmpresa(empresaChoice.getModelObject());
               List<Usuario> usuarios = cadastroFacade.buscarUsuariosPorEmpresa(filterDTO.getEmpresa());
               if(usuarios != null && !usuarios.isEmpty()){
            	   usuarioChoice.setChoices(usuarios);
               }else{
            	buscarDados();
            	usuarioChoice.setChoices(getListUsuarios());
               }
                art.add(usuarioChoice);
            }
        });
        form.add(empresaChoice);
        
        

		IChoiceRenderer<Usuario> iChoiceUsuario = new IChoiceRenderer<Usuario>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Usuario u) {
				return u.getNome().toUpperCase();
			}

			@Override
			public String getIdValue(Usuario arg, int arg1) {
				return arg.getId().toString();
			}
		};

        usuarioChoice = new DropDownChoice<Usuario>("usuario",getUsuariosPorEmpresa(), iChoiceUsuario);
        usuarioChoice.setNullValid(true);
        usuarioChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
            protected void onUpdate(AjaxRequestTarget art) {
            		filterDTO.setUsuario(usuarioChoice.getModelObject());
            }
        });
        form.add(usuarioChoice);
        
        txtDataInicio = new DateTimeTextFieldCustom("dataInicio", new PropertyModel<Date>(filterDTO, "dataInicio"));
		txtDataInicio.setOutputMarkupId(true);

		txtDataFim = new DateTimeTextFieldCustom("dataFim", new PropertyModel<Date>(filterDTO, "dataFim"));
		txtDataFim.setOutputMarkupId(true);
		
		form.add(txtDataInicio);
		form.add(txtDataFim);
		
	}
	@Override
	protected void loadStringLabel(String strLabel) {
		this.strLabel = strLabel;
	}

	@Override
	protected void onSearch(AjaxRequestTarget target, Form<?> form) {
		if(usuarioChoice.getModelObject() != null){
			filterDTO.setUsuario(usuarioChoice.getModelObject());
		}
	}

	
}

