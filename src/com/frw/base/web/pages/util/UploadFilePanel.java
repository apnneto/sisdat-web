package com.frw.base.web.pages.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

import com.frw.base.dominio.base.AnexoWeb;
import com.frw.base.util.ImageConverterUtil;
import com.frw.base.web.util.AjaxButtonFrw;
import com.frw.base.web.util.LabelFrw;

/**
 * @author Leonardo Barros
 */
public class UploadFilePanel extends Panel {

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form {

        private FileUploadField fileUploadField;

        public FileUploadForm(String id, final WebMarkupContainer markupContainer) {
            super(id);


            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));
            fileUploadField.setLabel(new Model<String>("selecion"));
            //setMultiPart(true);

            add(new AjaxButtonFrw("ajaxButton") {

                @Override
                protected void onError(AjaxRequestTarget target) {
                    super.onError(target);
                    info("Erro ao fazer upload, Arquivo não é valido");
                    target.add(feedBackPanel);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    final FileUpload upload = fileUploadField.getFileUpload();
                    if (upload != null) {

                        anexoFile = new AnexoWeb();
                        anexoFile.setFileName(fileUploadField.getFileUpload().getClientFileName());

                        byte[] imgArray = null;

                        if(anexoFile.isImageExtension() && isTransformImage){
                            imgArray = ImageConverterUtil.convertImage(upload.getBytes());
                        } else {
                            imgArray = upload.getBytes();
                        }

                        anexoFile.setFileArray(imgArray);
                        afterUpload(target);
                        target.add(markupContainer);
                    }
                }
            });

        }

        protected final void clearFileInput() {
            if(fileUploadField != null)
                fileUploadField.clearInput();
        }

        @Override
        protected void onSubmit() {
        }
    }
    private static final Bytes MAX_SIZE = Bytes.kilobytes(2048);
    private AnexoWeb anexoFile = null;
    private FeedbackPanel feedBackPanel;
    private boolean isTransformImage = false;
    private LabelFrw tituloUploadPanel;
    private final FileUploadForm uploadForm;

    protected UpdatableModalWindow uploadModal;

    public UploadFilePanel(String id, final WebMarkupContainer markupContainer, Bytes maxUploadSize, IModel labelModel) {
        super(id);

        this.setOutputMarkupId(true);

        tituloUploadPanel = new LabelFrw("tituloUploadPanel", labelModel);
        add(tituloUploadPanel);

        // Add upload form with ajax progress bar
        uploadForm = new FileUploadForm("form", markupContainer);

        feedBackPanel = new FeedbackPanel("feedback");
        feedBackPanel.setOutputMarkupId(true);
        uploadForm.add(feedBackPanel);

      //  uploadForm.setMaxSize(maxUploadSize);
        add(uploadForm);

        uploadModal = new UpdatableModalWindow("uploadModal");
        add(uploadModal);

        uploadModal.setInitialHeight(170);
        uploadModal.setInitialWidth(400);

    }

    public UploadFilePanel(String id, final WebMarkupContainer markupContainer, Bytes maxUploadSize, IModel labelModel, boolean transformFile) {
        this(id, markupContainer, maxUploadSize, labelModel);
        isTransformImage = transformFile;
    }

    public UploadFilePanel(String id, final WebMarkupContainer markupContainer, IModel labelModel) {
        this(id, markupContainer, labelModel, false);
    }

    public UploadFilePanel(String id, final WebMarkupContainer markupContainer, IModel labelModel, boolean transformFile) {
        this(id, markupContainer, MAX_SIZE, labelModel);
        isTransformImage = transformFile;
    }

    public void afterUpload(AjaxRequestTarget target) {
    }

    public void afterUpload(AjaxRequestTarget target, Form<?> form) {
        afterUpload(target);
    }

    public void clearFileInput() {
        if(uploadForm != null)
            uploadForm.clearFileInput();
    }

    /**
     * @return the anexoFile
     */
    public AnexoWeb getAnexoFile() {
        return anexoFile;
    }

    public void setAnexoFile(AnexoWeb anexoFile) {
        this.anexoFile = anexoFile;
    }
}
