package com.frw.base.web.pages.base;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.frw.base.web.pages.BasePage;

/**
 *
 * @author Framework
 */

@AuthorizeInstantiation(value="valid_user")
public class BasePageWithPanelContent extends BasePage {

  
}
