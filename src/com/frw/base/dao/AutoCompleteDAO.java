package com.frw.base.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class AutoCompleteDAO<T> {

	@PersistenceContext(unitName = "xq.pu")
    protected EntityManager em;
	
	public List<String> findAutoComplete(final String input, final String propertyNameEntity, final Class<T> entityClass){
		StringBuilder queryBuilder = new StringBuilder("select :propertyNameEntity from " + entityClass.getSimpleName() + " t where t.excluido <> true ");
		
		if (input !=null && !input.isEmpty()) {
			queryBuilder.append(" and lower(:propertyNameEntity) like :input ");
		}
		
		queryBuilder.append(" order by :propertyNameEntity ");
		
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("propertyNameEntity", "t.".concat(propertyNameEntity));
		query.setParameter("propertyNameEntity", "t.".concat(propertyNameEntity));
		
		if (queryBuilder.toString().contains(":input")) {
			query.setParameter("propertyNameEntity", "t.".concat(propertyNameEntity));
			query.setParameter("input", input.toLowerCase().concat("%"));
		}
		
		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
    }
}
