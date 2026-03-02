/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.util;

import com.frw.negocio.export.xls.AbstractXLSExport;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Classe utulitária para geração de PDFs a partir de listas de entidades
 * 
 * @author Framework
 */
public class XLSExportUtil extends AbstractXLSExport {

	static class Footer extends PdfPageEventHelper {

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
		}
	}

}
