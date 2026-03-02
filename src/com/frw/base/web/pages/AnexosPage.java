/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.base.AnexoWeb;
import com.frw.base.web.pages.util.UploadFilePanel;
import com.frw.base.web.util.LabelFrw;

/**
 *
 * @author Leo
 */
public class AnexosPage extends WebPage {

    final private List<AnexoWeb> listaAnexos = new ArrayList<AnexoWeb>();
    private WebMarkupContainer markupContainer;

    public AnexosPage() {


        markupContainer = new WebMarkupContainer("container");
        markupContainer.setOutputMarkupId(true);

        LabelFrw lblTitulo = new LabelFrw("titulo", " Ex: Anexando arquivos.");
        add(lblTitulo);

        final UploadFilePanel uploadPanel = new UploadFilePanel("uploadFile", markupContainer, new Model("Upload")) {

            @Override
            public void afterUpload(AjaxRequestTarget target) {

                if (getAnexoFile() != null) {
                    if (!listaAnexos.contains(getAnexoFile())) {
                        listaAnexos.add(getAnexoFile());
                    }
                }

            }
        };

        add(uploadPanel);



        LoadableDetachableModel model = new LoadableDetachableModel() {

            @Override
            protected List<AnexoWeb> load() {
                return listaAnexos;
            }
        };

        ListView<AnexoWeb> listView = new ListView<AnexoWeb>("lista", model) {

            @Override
            protected void populateItem(final ListItem<AnexoWeb> item) {

                LabelFrw nomeArquivo = new LabelFrw("nomeArquivo", item.getModelObject().getFileName());

                Link downloadLink = new Link("downloadLink") {

                    @Override
                    public void onClick() {
                        RequestCycle.get().setRequestTarget( new ShowAnexoPage(item.getModelObject().getFileArray(), item.getModelObject().getFileName()));
                    }
                };


                AjaxLink removeLink = new AjaxLink("removeLink") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        listaAnexos.remove(item.getModelObject());
                        target.addComponent(markupContainer);
                    }
                };

                item.add(downloadLink);
                item.add(removeLink);
                item.add(nomeArquivo);

            }
        };

        markupContainer.add(listView);
        add(markupContainer);


    }
}
