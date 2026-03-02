package com.frw.base.web;

import java.util.HashMap;

import com.frw.base.dominio.base.Funcionalidade;
import com.frw.base.web.pages.cadastro.ListColetaPesquisaPage;
import com.frw.base.web.pages.cadastro.ListEmpresaPage;
import com.frw.base.web.pages.cadastro.ListPesquisaPage;
import com.frw.base.web.pages.cadastro.ListQuestionarioPage;
import com.frw.base.web.pages.cadastro.ListRespostaPesquisaPage;
import com.frw.base.web.pages.seguranca.ListPerfisPage;
import com.frw.base.web.pages.seguranca.ListUsuarioPage;

/**
 * Classe utilitáia que mapeia funcionalidades para telas
 * 
 * @author juliano
 */
@SuppressWarnings("rawtypes")
public class FuncionalidadesMapper {

	private static final HashMap<Long, Class> map = new HashMap<Long, Class>();

	static {

		map.put(1l, ListUsuarioPage.class);
		map.put(2l, ListPerfisPage.class);
		map.put(3l, ListPesquisaPage.class);
		map.put(4l, ListQuestionarioPage.class);
		
		map.put(5l, ListColetaPesquisaPage.class);
		map.put(6l, ListRespostaPesquisaPage.class);
		map.put(7l, ListEmpresaPage.class);

	}

	public static Class getPageForFuncionalidade(Funcionalidade funcionalidade) {
		return map.get(funcionalidade.getId());
	}

}
