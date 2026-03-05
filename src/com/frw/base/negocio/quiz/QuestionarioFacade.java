package com.frw.base.negocio.quiz;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.sisdat.QuestionarioDAO;
import com.frw.base.dao.sisdat.TipoDadoDAO;
import com.frw.base.dao.sisdat.TipoLovDAO;
import com.frw.base.dominio.base.TipoUsuario;
import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.TipoDado;
import com.frw.base.dominio.sisdat.TipoLov;
import com.frw.base.negocio.CadastroFacade;
import com.frw.manutencao.dominio.dto.FilterListQuestionarioDTO;

@Stateless
public class QuestionarioFacade {

	@EJB
	private CadastroFacade cadastroFacade;
	
	@Inject
	private QuestionarioDAO questionarioDAO;
	
	@Inject
	private TipoDadoDAO tipoDadoDAO;
	
	@Inject
	private TipoLovDAO tipoLovDAO;
	
	public Questionario buscaQuestionarioPorCodigo(String codigoQuest) {
		return questionarioDAO.buscarQuestionarioPorCodigo(codigoQuest);
	}

	public Questionario buscarPorDescricaoCompleta(String input) {
		return questionarioDAO.findByDescricaoCompleta(input);
	}
	
	public Questionario buscarQuestionario(Long id) {
		return questionarioDAO.findById(id);
	}

	public List<Questionario> buscarQuestionarioComPesquisaPorUsuario(Usuario usuario){
		if (usuario.getTipoUsuario().getId().equals(TipoUsuario.ADM)) {
			return questionarioDAO.findOnlyQuestionarioWithPesquisa();
		}
		return questionarioDAO.findOnlyQuestionarioWithPesquisaPorUsuario(usuario.getId());
	}

	public List<Questionario> buscarTodosQuestionarios() {
		return questionarioDAO.findAll();
	}

	public List<Questionario> buscarTodosQuestionariosPublicados() {
		return questionarioDAO.buscarTodosQuestionariosPublicados();
	}

	public List<TipoDado> buscarTodosTiposDado() {
		return tipoDadoDAO.findAll();
	}
	
	public List<TipoLov> buscarTodosTiposLov() {
		return tipoLovDAO.findAll();
	}
	public List<Usuario> buscarTodosUsuariosComEmpresa() {
		return cadastroFacade.buscarTodosUsuariosComEmpresa();
	}
	
	public List<Usuario> buscarUsuariosPorEmpresa(Empresa empresa) {
		return cadastroFacade.buscarUsuariosPorEmpresa(empresa);
	}

	public void excluirQuestionario(Questionario questionario) {
		questionario.setExcluido(true);
		questionarioDAO.saveOrUpdate(questionario);
	}

	public boolean isUsuarioAdministrator(Usuario usuario) {
		if (usuario != null && usuario.getTipoUsuario() != null) {
			TipoUsuario tipoUsuario = usuario.getTipoUsuario();
			return tipoUsuario.getId().equals(TipoUsuario.ADM);
		}
		return false;
	}

	public List<String> pesquisaAutocompletePorDescricao(String descricao) {
		return questionarioDAO.findAutocompleteByDescricao(descricao);
	}
	
	public List<String> pesquisarAutoComplete(String input) {
		return questionarioDAO.buscarAutoComplete(input);
	}

	public List<Questionario> pesquisarQuestionarioPorFiltro(FilterListQuestionarioDTO filterDTO){
		return questionarioDAO.buscarQuestionarioPorFiltro(filterDTO);
	}

	public Questionario publicar(Questionario questionario){
		return questionarioDAO.saveOrUpdate(questionario);
	}

	public Questionario salvarQuestionario(Questionario questionario) {
		return questionarioDAO.saveOrUpdate(questionario);
	}
}
