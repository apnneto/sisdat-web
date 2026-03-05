package com.frw.base.web.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author Miller Leonardo
 */
@SuppressWarnings("serial")
public abstract class AutoCompleteFieldFrw<T extends Serializable> extends AutoCompleteTextField<T> implements IAjaxIndicatorAware {

	private AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
	/*@EJB
	private AutoCompleteFacade facade;
	private EntidadeDominioBase entity;
	private Class<? extends EntidadeDominioBase> entityClass;
	private String propertyNameEntity;*/

	public AutoCompleteFieldFrw(String id) {
		super(id, new Model<T>());
		init();
	}
	
	public AutoCompleteFieldFrw(String id, IModel<T> model) {
		super(id, model);
		init();
	}

	/*public AutoCompleteFieldFrw(String id, Model<T> model, Class<? extends EntidadeDominioBase> entityClass, String propertyNameEntity) {
		super(id, model);
		init();
		initDAO(entityClass, propertyNameEntity);
	}
	
	public AutoCompleteFieldFrw(String id, Class<? extends EntidadeDominioBase> entityClass, PropertyModel<T> propertyModel) {
		super(id, propertyModel);
		init();
		initDAO(entityClass, propertyModel.getPropertyField().getName());
	}
	
	private void initDAO(Class<? extends EntidadeDominioBase> entityClass, String propertyNameEntity) {
		this.entityClass = entityClass;
		this.propertyNameEntity = propertyNameEntity;
	}*/

	@Override
	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}
	
	/**
	 * Define CSS da lista com resultados da consulta.
	 */
	public void init() {
		
                add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onChange(target);
			};
		});
		add(indicatorAppender);
	}

	/**
	 * Consulta que sera realizada no ato de auto completar.
	 * @param input
	 * @return
	 */
	public abstract List<String> loadListChoices(String input);
	/*public List<String> loadListChoices(String input){
		return facade.findAutoComplete(input, propertyNameEntity, entityClass);
	}*/

	/**
	 * Evento disparado ao alterar o valor do campo de auto complete.
	 * @param target
	 */
	public void onChange(AjaxRequestTarget target) {}

	@Override
	protected Iterator<T> getChoices(String input) {
		if (input != null) {
			return (Iterator<T>) loadListChoices(input).iterator();
		}
		List<T> emptyList = Collections.emptyList();
		return emptyList.iterator();
	}
		
}