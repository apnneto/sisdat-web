/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.web.util;

import com.frw.base.web.pages.BasePage;

/**
 *
 * @author Marcelo Alves
 */
public class BasePageUtil extends BasePage {

    private static BasePageUtil instance;

    public static BasePageUtil getInstance() {
        if(instance == null) {
            instance = new BasePageUtil();
        }
        return instance;
    }

    private BasePageUtil() {}

}
