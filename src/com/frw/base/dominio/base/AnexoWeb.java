/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.dominio.base;

import com.frw.base.dominio.enumeration.TipoArquivoEnum;

/**
 *
 * @author Leo
 */
public class AnexoWeb {

    private byte[] fileArray;
    private String fileName;
    private TipoArquivoEnum tipoArquivo;

    public AnexoWeb() {
    }


    public AnexoWeb(byte[] fileArray,String fileName, TipoArquivoEnum tipoArquivo) {
        this.fileArray = fileArray;
        this.fileName = fileName;
        this.tipoArquivo = tipoArquivo;
    }


    @Override
    public boolean equals(Object object) {
        if (!this.getClass().isInstance(object)) {
            return false;
        }
        AnexoWeb other = (AnexoWeb) object;
        if ((this.fileName == null && other.fileName != null) || (this.fileName != null && !this.fileName.equals(other.fileName))) {
            return false;
        }
        return true;
    }

    /**
     * @return the fileArray
     */
    public byte[] getFileArray() {
        return fileArray;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    public TipoArquivoEnum getTipoArquivo() {
        return tipoArquivo;
    }

    public boolean isImageExtension(){

        if(fileName == null)
            return false;

        if(fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".jpeg")
                || fileName.toLowerCase().endsWith(".png")  || fileName.toLowerCase().endsWith(".tif")
                || fileName.toLowerCase().endsWith(".tiff") || fileName.toLowerCase().endsWith(".bmp")
                ||  fileName.toLowerCase().endsWith(".jpg") )
        {
            return true;
        }

        return false;
    }

    /**
     * @param fileArray the fileArray to set
     */
    public void setFileArray(byte[] fileArray) {
        this.fileArray = fileArray;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;

        if(fileName.toLowerCase().endsWith(".xls")) {
            setTipoArquivo(TipoArquivoEnum.XLS);
        } else if(fileName.toLowerCase().endsWith(".csv") || fileName.toLowerCase().endsWith(".txt")) {
            setTipoArquivo(TipoArquivoEnum.CSV);
        } else if(fileName.toLowerCase().endsWith(".pdf")) {
            setTipoArquivo(TipoArquivoEnum.PDF);
        } else if(isImageExtension()) {
            setTipoArquivo(TipoArquivoEnum.JPEG);
        } else {
            throw new RuntimeException("Tipo de arquivo não suportado.");
        }
    }

    public void setTipoArquivo(TipoArquivoEnum tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

}

