package com.frw.base.dao.sisdat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class GenericDAO {

	@PersistenceContext(unitName = "xq.pu")
	protected EntityManager em;
}
