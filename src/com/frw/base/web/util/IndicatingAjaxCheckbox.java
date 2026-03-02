package com.frw.base.web.util;


import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Marcelo Alves
 */
public abstract class IndicatingAjaxCheckbox extends AjaxCheckBox implements IAjaxIndicatorAware
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

	/**
	 * Constructor
	 *
	 * @param id
	 */
	public IndicatingAjaxCheckbox(String id)
	{
		this(id, null);
	}

	/**
	 * Constructor
	 *
	 * @param id
	 * @param model
	 *            model used to set <code>value</code> markup attribute
	 */
	public IndicatingAjaxCheckbox(String id, IModel<Boolean> model)
	{
		super(id, model);
                add(indicatorAppender);
	}

	/**
	 * @see IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
	 * @return the markup id of the ajax indicator
	 *
	 */
	public String getAjaxIndicatorMarkupId()
	{
		return indicatorAppender.getMarkupId();
	}


}
