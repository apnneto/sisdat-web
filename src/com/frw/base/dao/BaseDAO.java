package com.frw.base.dao;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


import java.sql.SQLException;

import org.apache.commons.lang.exception.ExceptionUtils;
// PostgreSQL driver kept as compile dependency — import retained for legacy compatibility
// import org.postgresql.util.PSQLException;

import com.frw.base.dominio.base.Entidade;
import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.exception.ForeignKeyViolationException;
import com.frw.base.exception.ObjetoObsoletoException;

/**
 * @author juliano
 */
public class BaseDAO<T extends EntidadeBase> {

    // MySQL FK violation = 23000; PostgreSQL = 23503
    private static final String FOREIGN_KEY_VIOLATION_SQLSTATE = "23000";
    private static final String FOREIGN_KEY_VIOLATION_SQLSTATE_PG = "23503";

    @PersistenceContext(unitName = "xq.pu")
    protected EntityManager em;

    public void delete(T object) {
        try {
            em.remove(em.merge(object));
            em.flush();
        } catch (OptimisticLockException ole) {
            throw new ObjetoObsoletoException("O objeto ["+object+"] não pode ser gravado pois foi atualizado após sua última consulta.", object);
        } catch (RuntimeException ex) {
            Throwable root = ExceptionUtils.getRootCause(ex);
            if(root == null)
                root = ex;

            if(root instanceof SQLException) {
                String SQLState = ((SQLException) root).getSQLState();

                if(SQLState != null && (SQLState.equals(FOREIGN_KEY_VIOLATION_SQLSTATE) || SQLState.equals(FOREIGN_KEY_VIOLATION_SQLSTATE_PG))) {
                    System.err.println("CONSTRAINT VIOLATIONS DELETING : " + object.getClass().getName());
                    throw new ForeignKeyViolationException("exception.foreign.key.violation", object.getClass());
                }
            }

            throw ex;

        }
    }

    public List<T> findAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getEntityClass());

        Root<T> emp = cq.from(getEntityClass());
        cq.select(emp);
        

        addNotDeletedConstraint(cb, cq, emp);

        TypedQuery<T> query = em.createQuery(cq);
        
        List<T> rows = query.getResultList();

        return rows;

    }

    public T findById(Long key) {
        return (T) em.find(getEntityClass(), key);
    }

    public void flush() {
        try {
            em.flush();
        } catch (OptimisticLockException ole) {
            throw new ObjetoObsoletoException("O objeto ["+ole.getEntity()+"] não pode ser gravado pois foi atualizado após sua última consulta.", (Entidade) ole.getEntity());
        }
    }

    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    public T load(Long key, String... init) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getEntityClass());
        Root<T> emp = cq.from(getEntityClass());

        for (String i : init) {
            emp.fetch(i);
        }

        cq.where(cb.equal(emp.get("id"), key));

        cq.select(emp);

        TypedQuery<T> query = em.createQuery(cq);

        return (T) query.getSingleResult();

    }
    
    
    public T merge(T entity) {
        try {
            return em.merge(entity);
        } catch (OptimisticLockException ole) {
            throw new ObjetoObsoletoException("O objeto ["+entity+"] não pode ser gravado pois foi atualizado após sua última consulta.", entity);
        }
    }

    public T saveOrUpdate(T object) {

        try {
            if (object.getId() != null) {
                object = em.merge(object);
            } else {
                em.persist(object);
            }
        } catch (OptimisticLockException ole) {
            throw new ObjetoObsoletoException("O objeto ["+object+"] não pode ser gravado pois foi atualizado após sua última consulta.", object);
        } catch (Exception ex) {
             Throwable t=ex;
            while( t!=null && ! (t instanceof ConstraintViolationException))
                t=t.getCause();

            if(t==null)
                throw (RuntimeException)ex;
            ConstraintViolationException e=(ConstraintViolationException) t;

            System.err.println("CONSTRAINT VIOLATIONS SAVING : " + object.getClass().getName());

            for(ConstraintViolation v: e.getConstraintViolations()){
                StringBuilder str = new StringBuilder();
                str.append("CONSTRAINT: Classe= ").
                        append(v.getRootBeanClass().getName().toUpperCase())
                        .append(" - ")
                        .append(v.getMessage().toUpperCase())
                        .append(" / FIELD = ")
                        .append(v.getPropertyPath().toString().toUpperCase());
             System.err.println(str.toString());
                System.err.println(v.getLeafBean().getClass()+"."+v.getPropertyPath()+":"+ v.getConstraintDescriptor().toString());
            }

            throw (RuntimeException)ex;
        }


        return object;

    }

    public void setFlushMode(FlushModeType flushType) {
        em.setFlushMode(flushType);
    }

    public void setQueryParameters(Query query, Map<String,Object> parameters) {

        for (String key : parameters.keySet()) {
        	Object value = parameters.get(key);
        	
			if(value instanceof Date)
        		query.setParameter(key,(Date)value);
			
        	else if(value instanceof Calendar)
        		query.setParameter(key,(Calendar)value);
			
        	else 
        		query.setParameter(key, value);
        }

    }

    private Class getEntityClass() {
        ParameterizedType t = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) t.getActualTypeArguments()[0];
    }

    protected CriteriaQuery addNotDeletedConstraint(CriteriaBuilder cb, CriteriaQuery cq, Root<T> emp) {

        Class classe = getEntityClass();
        CriteriaQuery query = cq;

        try {
            Method method = classe.getSuperclass().getMethod("isExcluido", null);
            query = cq.where(cb.equal(emp.get("excluido"), "false"));

        } catch (NoSuchMethodException ex) {
        }

        return query;

    }

}
