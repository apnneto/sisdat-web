package com.frw.base.web.pages.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.frw.base.web.pages.util.ObservacaoModalAjaxLink;
import com.frw.base.web.pages.util.UpdatableModalWindow;

/**
 *
 * @author Marcelo
 */
public class ObservacaoModalAjaxLinkPanel extends Panel {

    protected ObservacaoModalAjaxLink obsLink;

    public ObservacaoModalAjaxLinkPanel(String id, PropertyModel<String> observacaoModel, UpdatableModalWindow modalWindow) {
        super(id);
        setOutputMarkupId(true);
        obsLink = new ObservacaoModalAjaxLink("obsLink",observacaoModel,modalWindow, this);
        add(obsLink);
    }

    public ObservacaoModalAjaxLink getField() {
        return obsLink;
    }

    public void setField(ObservacaoModalAjaxLink field) {
        this.obsLink = field;
    }

}
