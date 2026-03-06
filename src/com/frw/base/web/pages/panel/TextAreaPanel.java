package com.frw.base.web.pages.panel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/** 
 *
 * @author Leonardo Barros
 * 
 */
public class TextAreaPanel extends Panel {

    class SelectForm extends Form {

        public SelectForm(String id, IModel modal) {
            super(id);

            TextArea textArea = new TextArea("obs",modal);
                add(textArea);


        AjaxSubmitLink confirmLink;
         add(confirmLink=new AjaxSubmitLink("confirm") {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    if(markupContainner != null)
                        target.add(markupContainner);

                    UpdatableModalWindow.closeCurrent(target);
                }
	     
         });

         add(confirmLink);

        }

        @Override
        protected void onSubmit() {
         

        }
    }
    private MarkupContainer markupContainner;
    private SelectForm selectForm;
   

    public TextAreaPanel(String id, IModel modal) {
        this(id,modal,null);
    }
    public TextAreaPanel(String id, IModel modal, MarkupContainer markupContainer) {
        super(id);
        add(new FeedbackPanel("feedback"));

        this.markupContainner = markupContainer;
        selectForm = new SelectForm("form", modal);
        add(selectForm);
    }
}
