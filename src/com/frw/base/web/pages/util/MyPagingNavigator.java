/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages.util;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 *
 * @author Leo
 */
public class MyPagingNavigator extends AjaxPagingNavigator {

    public MyPagingNavigator(String id, IPageable page) {
        super(id, page);
    }
}
