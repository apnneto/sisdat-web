package com.frw.base.negocio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.frw.base.dao.AutoCompleteDAO;

@Stateless
public class AutoCompleteFacade<T> {

	@Inject
	private AutoCompleteDAO<T> dao;
	
	public List<String> findAutoComplete(String input, String propertyNameEntity, Class<T> entityClass) {
		return dao.findAutoComplete(input, propertyNameEntity, entityClass);
	}

}
