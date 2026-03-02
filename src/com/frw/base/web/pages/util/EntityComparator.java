/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import java.util.Comparator;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Framework
 */
public class EntityComparator implements Comparator {

    public enum SortDirection {

        ASCENDING, DESCENDING;
    }
    private Class entityClass;

    private SortDirection sortDirection = SortDirection.ASCENDING;
    private String sortingField;
    public EntityComparator(Class entity) {


        entityClass = entity;


    }

    @Override
    public int compare(Object o1, Object o2) {
        try {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            Object value1 = null;
            Object value2 = null;

            try {
                value1 = PropertyUtils.getProperty(o1, sortingField);
            } catch (NestedNullException e) {
                value1 = "";
            }
            try {
                value2 = PropertyUtils.getProperty(o2, sortingField);
            } catch (NestedNullException e) {
                value2 = "";
            }


            if (value1 == null || value2 == null) {
                return 0;
            }

            if (!(value1 instanceof Comparable)) {
                throw new RuntimeException(value1.getClass().getName() + " is not an instance of java.lang.Comparable");
            }
            
            if (value1 instanceof String && value2 instanceof String ) {
            	value1 = ((String)value1).toUpperCase();
            	value2 = ((String)value2).toUpperCase();
            }

            if (sortDirection == SortDirection.ASCENDING) {
                return ((Comparable) value1).compareTo(value2);
            } else {
                return ((Comparable) value2).compareTo(value1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public String getSortingField() {
        return sortingField;
    }

    public void reverseSortDirection() {
        if (sortDirection == SortDirection.ASCENDING) {
            sortDirection = SortDirection.DESCENDING;
        } else {
            sortDirection = SortDirection.ASCENDING;
        }


    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSortingField(String sortingField) {
        this.sortingField = sortingField;


    }
}
