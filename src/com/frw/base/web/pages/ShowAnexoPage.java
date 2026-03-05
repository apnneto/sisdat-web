/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.ByteArrayResourceStream;

/**
 * Wicket 9: IRequestTarget removed. Use scheduleRequestHandlerAfterCurrent.
 * This class is now a static utility to trigger file downloads.
 */
public class ShowAnexoPage {

    private final byte[] file;
    private final String fileName;

    public ShowAnexoPage(byte[] arquivo, String fileName) {
        this.file = arquivo;
        this.fileName = fileName;
    }

    public ShowAnexoPage(File zipFile) throws IOException {
        this.file = Files.readAllBytes(zipFile.toPath());
        this.fileName = zipFile.getName();
    }

    /** Schedules the file for download in the current request cycle. */
    public void respond() {
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(
            new ResourceStreamRequestHandler(
                new ByteArrayResourceStream(file, "application/octet-stream"))
                .setFileName(fileName)
                .setContentDisposition(ContentDisposition.ATTACHMENT));
    }

    public byte[] getFile() { return file; }
    public String getFileName() { return fileName; }
}