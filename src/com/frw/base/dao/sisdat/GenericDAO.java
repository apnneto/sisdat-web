package com.frw.base.dao.sisdat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAO {

	@PersistenceContext
	protected EntityManager em;
}
