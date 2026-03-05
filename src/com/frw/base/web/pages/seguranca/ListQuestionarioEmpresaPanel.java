package com.frw.base.web.pages.seguranca;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.negocio.quiz.EmpresaFacade;
import com.frw.base.web.pages.base.AbstractEntityListPanelNew;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.cadastro.ListQuestionarioPanel;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import com.frw.base.web.util.BasePageUtil;
import com.frw.base.web.util.LabelFrw;
import com.frw.negocio.export.xls.AbstractXLSExport;
import com.frw.negocio.export.xls.XLSExportQuestionario;

/**
 *
 * @author juliano
 */
public class ListQuestionarioEmpresaPanel extends AbstractEntityListPanelNew<Questionario> {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;

	@EJB
	private EmpresaFacade empresaFacade;

	private ListQuestionarioPanel lookupPanel;

	public ListQuestionarioEmpresaPanel(String id, final UpdatableModalWindow confirmationModal, Empresa empresa) {
		super(id, confirmationModal);
		setOutputMarkupId(true);
		this.empresa = empresa;

		super.enableColunaAcao = true;
		super.enableColunaCodigo = true;
		super.enableDeleteLink = true;
		super.enableEditLink = false;
		super.enableSelectLink = false;
	}

	@Override
	public boolean isContentExportableToExcel() {
		return true;
	}

	@Override
	protected void addTableItems(Questionario entity, EntityLabelMap componentMap) {
		componentMap.add("codigo", new LabelFrw("label", entity.getCodigo()));
		componentMap.add("descricao", new LabelFrw("label", entity.getDescricao()));
		componentMap.add("orientacao", new LabelFrw("label", entity.getOrientacao() != null ? entity.getOrientacao() : "-"));
	}

	@Override
	protected Panel getEditPanel(Questionario entity, AjaxRequestTarget target) {
		lookupPanel = new ListQuestionarioPanel("content", confirmationModal, true);
		lookupPanel.setOutputMarkupId(true);

		OnClickHandler onClickQuestionario = new OnClickHandler() {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Questionario questionario = lookupPanel.getSelectedEntity();

				if (!empresa.getQuestionarios().contains(questionario)) {
					empresa.getQuestionarios().add(questionario);
					empresaFacade.salvarEmpresa(empresa);
					target.addComponent(ListQuestionarioEmpresaPanel.this);

				} else if (empresa.getQuestionarios() == null) {
					empresa.setQuestionarios(new ArrayList<Questionario>());
					empresa.getQuestionarios().add(questionario);
					empresaFacade.salvarEmpresa(empresa);
					target.addComponent(ListQuestionarioEmpresaPanel.this);
				}
				ModalWindow.closeCurrent(target);
			}
		};
		lookupPanel.setOnClickHandler(onClickQuestionario);

		confirmationModal.setInitialHeight(490);
		confirmationModal.setInitialWidth(900);
		confirmationModal.setContent(lookupPanel);
		confirmationModal.setTitle("Pesquisa de Questionários");
		confirmationModal.show(target);

		return null;
	}

	@Override
	protected List<EntityColumnInfo> getEntityColumnInfo() {
		List<EntityColumnInfo> columns = new ArrayList<EntityColumnInfo>();

		columns.add(new EntityColumnInfo("codigo", new FrwResourceModel("questionario.label.id")));
		columns.add(new EntityColumnInfo("descricao", new FrwResourceModel("questionario.label.descricao")));
		columns.add(new EntityColumnInfo("orientacao", new FrwResourceModel("questionario.label.orientacao")));

		return columns;
	}

	@Override
	protected String getNewEntiyLinkLabel() {
		return BasePageUtil.getInstance().getString("adicionar.generico");
	}

	@Override
	protected AbstractXLSExport getXLSExportUtil() {
		return new XLSExportQuestionario();
	}

	@Override
	protected List<Questionario> loadList() {
		return empresa.getQuestionarios();
	}

	@Override
	protected void removeEntity(Questionario questionario, AjaxRequestTarget target) {
		empresa.getQuestionarios().remove(questionario);
		empresaFacade.salvarEmpresa(empresa);
	}

}
