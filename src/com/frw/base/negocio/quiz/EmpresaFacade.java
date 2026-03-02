package com.frw.base.negocio.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.frw.base.dao.sisdat.EmpresaDAO;
import com.frw.base.dominio.sisdat.Empresa;

@Stateless
public class EmpresaFacade {
	
	@Inject
	private EmpresaDAO empresaDAO;
	
	public List<Empresa> buscarTodas() {
		return empresaDAO.findAll();
	}
	
	public Empresa deleteEmpresa(Empresa empresa) {
		empresa.setExcluido(true);
		return empresaDAO.saveOrUpdate(empresa);
	}

	public List<Empresa> pesquisarEmpresaPorId(Empresa empresa) {
		if(empresa != null){
			ArrayList<Empresa> empresas = new ArrayList<Empresa>();
			empresas.add(empresaDAO.findById(empresa.getId()));
		return empresas;
		}
		return Collections.emptyList();
				
		}

	public Empresa salvarEmpresa(Empresa empresa) {
		return empresaDAO.saveOrUpdate(empresa);
	}
}
