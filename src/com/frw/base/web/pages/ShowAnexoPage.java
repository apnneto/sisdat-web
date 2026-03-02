/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.pages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebResponse;

/**
 *
 * @author Leo
 */
public class ShowAnexoPage implements IRequestTarget {

    private byte[] file;
    private String fileName;

    public ShowAnexoPage(byte[] arquivo, String fileName) {

        this.file = arquivo;
        this.fileName = fileName;
    }

    public ShowAnexoPage(File zipFile) throws IOException {
		InputStream in = new FileInputStream(zipFile);
		OutputStream out = new ByteArrayOutputStream((int) zipFile.length());
		byte[] buffer = new byte[(int) zipFile.length()];
		
		int len;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		
		this.file = buffer;
		this.fileName = zipFile.getName();
    	
    }

	@Override
    public void detach(RequestCycle rc) {
           Response rep = rc.getResponse();
          HttpServletResponse response = ((WebResponse) rep).getHttpServletResponse();
          response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }

    ;

    public byte[] getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
    public void respond(RequestCycle rc) {
        Response rep = rc.getResponse();
        HttpServletResponse response = ((WebResponse) rep).getHttpServletResponse();
        ByteArrayInputStream in = null;
        OutputStream out = null;

        try {


            response.addHeader("Content-Description", "File Transfer");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Transfer-Encoding", "binary");
            response.addHeader("Expires", "0");
            response.addHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.addHeader("Pragma", "public");
            response.addHeader("Content-Length", "" + file.length);


            out = response.getOutputStream();

            in = new ByteArrayInputStream(file);

            // rep.write(in);
            int i = 0;
            byte[] bytesIn = new byte[1024];
            while ((i = in.read(bytesIn)) >= 0) {
                out.write(bytesIn, 0, i);
            }
            out.flush();


        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }


    }
    
}
