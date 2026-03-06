/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

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
        AbstractResourceStreamWriter writer = new AbstractResourceStreamWriter() {
            @Override
            public void write(OutputStream output) throws IOException {
                output.write(file);
            }
            @Override
            public String getContentType() {
                return "application/octet-stream";
            }
        };
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(
            new ResourceStreamRequestHandler(writer)
                .setFileName(fileName)
                .setContentDisposition(ContentDisposition.ATTACHMENT));
    }

    public byte[] getFile() { return file; }
    public String getFileName() { return fileName; }
}