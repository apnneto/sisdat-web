package com.frw.base.web.pages.base;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.web.pages.util.UpdatableModalWindow;

/**
 * @author Marcelo Alves
 */
public abstract class AbstractEntityListEditablePanelNew<T extends EntidadeBase> extends AbstractEntityListPanelNew<T> {

    public AbstractEntityListEditablePanelNew(String id, UpdatableModalWindow confirmationModal) {
        super(id, confirmationModal);
    }

    public void refreshModel() {
        List<T> list = loadList();

        if (entityComparator.getSortingField() != null) {
            Collections.sort(list, entityComparator);
        }

        if(list == null || list.isEmpty() || !isContentExportableToExcel()) {
            exportarExcelLink.setVisible(false);
        } else {
            exportarExcelLink.setVisible(true);
        }

        this.model.setObject(list);
        listaView.setCurrentPage(0);
    }

    @Override
    protected IModel getListModel() {
        return new ListModel<T>();
    }

}
