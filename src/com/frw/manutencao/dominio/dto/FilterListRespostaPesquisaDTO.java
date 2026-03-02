/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.manutencao.dominio.dto;

import java.util.Calendar;
import java.util.Date;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Pesquisador;
import com.frw.base.web.pages.seguranca.filter.dto.AbstractFilterDTO;

/**
 * @author Marcos Lisboa
 */
public class FilterListRespostaPesquisaDTO extends AbstractFilterDTO {

	private static final long serialVersionUID = 1L;

	private Date dataFim;
	private Date dataInicio;
	private Empresa empresa;
	private Pesquisador pesquisador;
	private Usuario usuario;
	
	public FilterListRespostaPesquisaDTO() {
		dataFim = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -30);
		dataInicio = calendar.getTime();
	}
	
	public FilterListRespostaPesquisaDTO(Date dataInicio, Date dataFim) {
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public Pesquisador getPesquisador() {
		return pesquisador;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setPesquisador(Pesquisador pesquisador) {
		this.pesquisador = pesquisador;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
