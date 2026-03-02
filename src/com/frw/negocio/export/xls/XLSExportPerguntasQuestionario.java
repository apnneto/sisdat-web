/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.negocio.export.xls;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Classe utulitária para geração de PDFs a partir de listas de entidades
 * 
 * @author Framework
 */
public class XLSExportPerguntasQuestionario extends AbstractXLSExport {

	static class Footer extends PdfPageEventHelper {

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
		}
	}

	public XLSExportPerguntasQuestionario() {
		super();
	}

	@Override
	protected String getNomeAba() {
		return "Perguntas por questionário";
	}

	@Override
	protected String getNomeArquivo() {
		return "Perguntas por questionário";
	}

	@Override
	protected String getTitulo() {
		return "PERGUNTAS POR QUESTIONÁRIO";
	}

	@Override
	protected boolean showLote() {
		return false;
	}
	
	@Override
	protected boolean showPeriodo() {
		return false;
	}
	
}
