package com.frw.base.web.util;

import java.util.List;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Marcelo Alves
 */
public class IndicatingAjaxDropDownChoice<T extends Object> extends DropDownChoice<T> implements IAjaxIndicatorAware {

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
    public IndicatingAjaxDropDownChoice(String id) {
        this(id, null);
    }

    /**
     * Constructor
     *
     * @param id
     * @param model
     *            model used to set <code>value</code> markup attribute
     */
    public IndicatingAjaxDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
        add(indicatorAppender);
    }

    public IndicatingAjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
        add(indicatorAppender);
    }

    public IndicatingAjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        add(indicatorAppender);
    }

    public IndicatingAjaxDropDownChoice(String id, IModel<T> model, List<? extends T> choices) {
        super(id, model, choices);
        add(indicatorAppender);
    }

    /**
     * @see IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
     * @return the markup id of the ajax indicator
     *
     */
    public String getAjaxIndicatorMarkupId() {
        return indicatorAppender.getMarkupId();
    }

    protected boolean wantOnSelectionChangedNotifications() {
        return true;
    }
}
