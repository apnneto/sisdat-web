/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.exception;

/**
 *
 * @author Marcelo Alves
 */
public class ForeignKeyViolationException extends RuntimeException {

    private Class entityClass;

    public ForeignKeyViolationException(String message, Class entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public Class getEntityClass() {
        return entityClass;
    }

}
