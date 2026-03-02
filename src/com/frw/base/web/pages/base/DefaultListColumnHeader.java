package com.frw.base.web.pages.base;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.util.EntityComparator;

/**
 *
 * @author Framework
 */
public class DefaultListColumnHeader extends Panel {

    private final SortableListPanel listPanel;
    protected EntityColumnInfo columnInfo;
    protected IndicatingAjaxLink sortLink;

    public DefaultListColumnHeader(String id, EntityColumnInfo info, SortableListPanel parent) {
        super(id);
        this.columnInfo = info;
        this.listPanel = parent;

        sortLink = new IndicatingAjaxLink("sortLink") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                listPanel.setSortingField(columnInfo.property, art);
            }
        };

        if(!columnInfo.canBeSorted)
            sortLink.setEnabled(false);


        sortLink.add(new Label("columnLabel", columnInfo.name));
        add(sortLink);
        Label sortDirectionLabel = new Label("sortDirectionLabel", new Model());
        sortDirectionLabel.setEscapeModelStrings(false);
        EntityComparator entityComparator = listPanel.getEntityComparator();

        if (entityComparator.getSortingField() != null && entityComparator.getSortingField().equals(columnInfo.property)) {
            if (entityComparator.getSortDirection() == EntityComparator.SortDirection.ASCENDING) {
                sortDirectionLabel.setDefaultModelObject("&uarr;");
            } else {
                sortDirectionLabel.setDefaultModelObject("&darr;");
            }

        } else {
            sortDirectionLabel.setVisible(false);
        }

        add(sortDirectionLabel);

        if(!columnInfo.canBeSorted)
            sortDirectionLabel.setVisible(false);


        IndicatingAjaxLink hideLink = new IndicatingAjaxLink("hideLink") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                columnInfo.visible = false;
                listPanel.hideColumn(columnInfo, art);

            }
        };
        add(hideLink);

        if(!columnInfo.isCanBeHidden())
            hideLink.setVisible(false);

    }
}
