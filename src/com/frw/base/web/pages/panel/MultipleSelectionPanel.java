package com.frw.base.web.pages.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.web.util.AjaxButtonFrw;

/**
 *
 * @author juliano 
 */
public class MultipleSelectionPanel<T> extends Panel {

    private IModel<List<T>> allAvailableChoicesModel, selectedChoicesModel;
    private ListMultipleChoice availableList, selectedList;

    public MultipleSelectionPanel(String id,final IModel<List<T>> available,final IModel<List<T>> selected) {
        this(id, available, selected, null);
    }

    public MultipleSelectionPanel(String id,final IModel<List<T>> available,final IModel<List<T>> selected, IChoiceRenderer<T> choiceRender) {
        super(id);
        allAvailableChoicesModel=available;
        selectedChoicesModel=selected;

        Form form=new Form("multipleSelectionForm");
        add(form);

        final LoadableDetachableModel<List<T>> availableChoices=new LoadableDetachableModel<List<T>> () {

            @Override
            protected List<T> load() {

                ArrayList<T> list=new ArrayList<T>();
                list.addAll(available.getObject());
                list.removeAll(selectedChoicesModel.getObject());
                return list;

            }

        };

        if(choiceRender == null) {
            availableList=new ListMultipleChoice("available", availableChoices);
        } else {
            availableList=new ListMultipleChoice("available", availableChoices, choiceRender);
        }
        Model<ArrayList<T>> model1=new Model<ArrayList<T>>();
        model1.setObject(new ArrayList<T>());
        availableList.setModel(model1);
        availableList.setOutputMarkupId(true);
        availableList.setMaxRows(10);
        form.add(availableList);

        if(choiceRender == null) {
            selectedList=new ListMultipleChoice("selected",selectedChoicesModel);
        } else {
            selectedList=new ListMultipleChoice("selected",selectedChoicesModel, choiceRender);
        }
        Model<ArrayList<T>> model2=new Model<ArrayList<T>>();
        model2.setObject(new ArrayList<T>());
        selectedList.setModel(model2);
        selectedList.setMaxRows(10);


        form.add(selectedList);
        selectedList.setOutputMarkupId(true);

        AjaxButtonFrw addButton=new AjaxButtonFrw("add") {

            @Override
            protected void onSubmit(AjaxRequestTarget art, Form<?> form) {

                List toAdd=(List) availableList.getModelObject();
                selectedChoicesModel.getObject().addAll(toAdd);

                availableList.setModelObject(new ArrayList());
                availableList.getModel().detach();
                availableChoices.detach();
                availableList.modelChanged();
                selectedList.modelChanged();

                art.addComponent(availableList);
                art.addComponent(selectedList);

            }
        };
        
        form.add(addButton);

        AjaxButtonFrw removeButton=new AjaxButtonFrw("remove") {

            @Override
            protected void onSubmit(AjaxRequestTarget art, Form<?> form) {

                List toRemove=(List) selectedList.getModelObject();
                selectedChoicesModel.getObject().removeAll(toRemove);
                selectedList.setModelObject(new ArrayList());
                availableChoices.detach();
                art.addComponent(availableList);
                art.addComponent(selectedList);
            }
        };
        
        form.add(removeButton);

    }

    public IModel<List<T>> getAllAvailableChoicesModel() {
        return allAvailableChoicesModel;
    }

    public IModel<List<T>> getSelectedChoicesModel() {
        return selectedChoicesModel;
    }

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
        allAvailableChoicesModel.detach();
        selectedChoicesModel.detach();
    }

}
