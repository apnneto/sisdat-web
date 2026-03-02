package com.frw.base.web.pages.base;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.frw.base.web.pages.util.EntityComparator;

/**
 * @author Carlos Santos
 */
public interface SortableListPanel extends Serializable {

    public EntityComparator getEntityComparator();
    public void hideColumn(EntityColumnInfo entityColumnInfo, AjaxRequestTarget target);
    public void setSortingField(String field, AjaxRequestTarget target);
    
}
