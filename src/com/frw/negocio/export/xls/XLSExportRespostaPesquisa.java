/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.negocio.export.xls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Empresa;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.util.SistemaUtil;
import com.frw.base.util.enumeration.FormatoDataEnum;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.FrwResourceModel;
import com.frw.base.web.pages.base.Titulo;
import com.frw.manutencao.dominio.dto.FilterListBaseDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Marcos Lisboa
 */
public class XLSExportRespostaPesquisa {

	static class Footer extends PdfPageEventHelper {

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
		}
	}
	public static final String PROPERTY_COL_FOTOS = "fotos";
	public static final String PROPERTY_COL_LATITUDE = "latitudeFinal";
	public static final String PROPERTY_COL_LONGITUDE = "longitudeFinal";
	
	private Empresa empresa;


	private String nomeAba;

	protected FilterListBaseDTO filterDTO;

	protected HSSFCellStyle textStyleCenter;

	/*protected String getNomeAba() {
		return "RespostasPesquisa";
	}*/

	protected HSSFCellStyle textStyleLeft;

	protected HSSFCellStyle textStyleRight;

	protected HSSFWorkbook workbook;
	int maxColumnLength[];
	

	boolean primeira = true;

	public XLSExportRespostaPesquisa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void generateXLSTable(final ByteArrayOutputStream out,
			final LinkedHashMap<String, Titulo> titles, final List objects, PesquisaFacade pesquisaFacade) {

		Titulo tituloLatitude = new Titulo();
		tituloLatitude.setTitulo("Latitude");
		tituloLatitude.setEntityColumnInfo(new EntityColumnInfo(PROPERTY_COL_LATITUDE, new FrwResourceModel("label.latitude.final")));
		titles.put(PROPERTY_COL_LATITUDE, tituloLatitude);
		
		Titulo tituloLongitude = new Titulo();
		tituloLongitude.setTitulo("Longitude");
		tituloLongitude.setEntityColumnInfo(new EntityColumnInfo(PROPERTY_COL_LONGITUDE, new FrwResourceModel("label.latitude.inicial")));
		titles.put(PROPERTY_COL_LONGITUDE, tituloLongitude);

		Titulo tituloFotos = new Titulo();
		tituloFotos.setTitulo("Fotos");
		tituloFotos.setEntityColumnInfo(new EntityColumnInfo(PROPERTY_COL_FOTOS, new FrwResourceModel("questionario.label.fotos")));
		titles.put(PROPERTY_COL_FOTOS, tituloFotos);
		
		try {
			workbook = new HSSFWorkbook();
			criaEstilos();

			HSSFSheet sheet0 = workbook.createSheet("Informações");
			createAba0(sheet0, out, titles, objects);
			
			HSSFSheet sheet1 = workbook.createSheet("Empresa");
			createAba1(sheet1, out, titles, objects);
			
			HSSFSheet sheet2 = workbook.createSheet("RespostasPesquisa");
			createAba2(sheet2, out, titles, objects,pesquisaFacade);
		
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getQuestionario() {
		return "";
	}
	/**
	 * Gera aba 0 - Informacoes
	 */
	private void createAba0(HSSFSheet sheet0, final ByteArrayOutputStream out,
			final LinkedHashMap<String, Titulo> titles, final List objects) {

		writeHeader(sheet0, titles);
		writeBodyAba0(sheet0, titles);

	}
	/**
	 * Gera aba 1 - Empresa
	 */
	private void createAba1(HSSFSheet sheet1, final ByteArrayOutputStream out,
			final LinkedHashMap<String, Titulo> titles, final List objects) {

		writeHeader(sheet1, titles);
		writeBodyAba1(sheet1, titles);

	}
	/**
	 Gera aba 0 - RespostasPesquisa
	 * @param sheet0
	 * @param pesquisaFacade 
	 */
	private void createAba2(HSSFSheet sheet0, final ByteArrayOutputStream out,
			final LinkedHashMap<String, Titulo> titles, final List objects, PesquisaFacade pesquisaFacade) {

		int rowtitle = 0;

		for (int row = 0; row < objects.size(); row++) {

			if (row == 0) {
				writeHeader(sheet0, titles);
				rowtitle = writeTiltes(sheet0, titles);
				rowtitle++;
			}

			Object o = objects.get(row);
			writeBody(row + rowtitle, sheet0, o, titles,pesquisaFacade);
		}

	}

	@SuppressWarnings("finally")
	private String getReposta(ColetaPesquisa coletaPesquisa) {
		String resposta = "";
		try {
			if (coletaPesquisa.getResposta() != null) {
				resposta += coletaPesquisa.getResposta().getDescricao();
			}

			if (coletaPesquisa.getCampoLivre() != null
					&& !coletaPesquisa.getCampoLivre().isEmpty()) {
				resposta += coletaPesquisa.getCampoLivre();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return resposta;
		}
	}

	protected void criaEstilos() {

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 8);
		font.setColor(HSSFColor.BLACK.index);

		textStyleLeft = workbook.createCellStyle();
		textStyleLeft.setFillForegroundColor(HSSFColor.WHITE.index);
		textStyleLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		textStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		textStyleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
		textStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
		textStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		textStyleLeft.setBottomBorderColor(HSSFColor.BLACK.index);
		textStyleLeft.setTopBorderColor(HSSFColor.BLACK.index);
		textStyleLeft.setLeftBorderColor(HSSFColor.BLACK.index);
		textStyleLeft.setRightBorderColor(HSSFColor.BLACK.index);
		textStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		textStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		textStyleLeft.setWrapText(true);
		textStyleLeft.setFont(font);

		textStyleRight = workbook.createCellStyle();
		textStyleRight.setFillForegroundColor(HSSFColor.WHITE.index);
		textStyleRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		textStyleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		textStyleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
		textStyleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
		textStyleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		textStyleRight.setBottomBorderColor(HSSFColor.BLACK.index);
		textStyleRight.setTopBorderColor(HSSFColor.BLACK.index);
		textStyleRight.setLeftBorderColor(HSSFColor.BLACK.index);
		textStyleRight.setRightBorderColor(HSSFColor.BLACK.index);
		textStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		textStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		textStyleRight.setWrapText(true);
		textStyleRight.setFont(font);

		textStyleCenter = workbook.createCellStyle();
		textStyleCenter.setFillForegroundColor(HSSFColor.WHITE.index);
		textStyleCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		textStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		textStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
		textStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
		textStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		textStyleCenter.setBottomBorderColor(HSSFColor.BLACK.index);
		textStyleCenter.setTopBorderColor(HSSFColor.BLACK.index);
		textStyleCenter.setLeftBorderColor(HSSFColor.BLACK.index);
		textStyleCenter.setRightBorderColor(HSSFColor.BLACK.index);
		textStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		textStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		textStyleCenter.setWrapText(true);
		textStyleCenter.setFont(font);

	}

	/**
	 * 
	 * @param sheet
	 * @param titles
	 * @param rowLine
	 * @param titulo
	 * @return
	 */
	protected HSSFRow criaLinha(HSSFSheet sheet,
			LinkedHashMap<String, Titulo> titles, int rowLine,
			HSSFCellStyle titulo) {

		HSSFRow row = sheet.createRow(rowLine);

		Iterator it = titles.keySet().iterator();
		int col = 0;

		while (it.hasNext()) {
			it.next();
			row.createCell(col++).setCellStyle(titulo);
		}

		return row;
	}

	/**
	 * Cria uma nova linha para a sheet (aba), prepara as colunas para 
	 * cada title ou ate a quantidade de colunas {maxTitles}
	 */
	protected HSSFRow criaLinha(HSSFSheet sheet,
			LinkedHashMap<String, Titulo> titles, int maxTitles, int rowLine,
			HSSFCellStyle titulo) {

		HSSFRow row = sheet.createRow(rowLine);

		Iterator it = titles.keySet().iterator();
		int col = 0;

		for (int i = 0; i < maxTitles && it.hasNext(); i++) {
			it.next();
			row.createCell(col++).setCellStyle(titulo);
		}

		return row;
	}
	
	/**
	 * 
	 * @param aligment
	 * @return
	 */
	protected HSSFCellStyle getBodyFont(short aligment) {
		if (aligment == HSSFCellStyle.ALIGN_CENTER)
			return textStyleCenter;
		else if (aligment == HSSFCellStyle.ALIGN_RIGHT)
			return textStyleRight;
		else
			return textStyleLeft;
	}
	
	protected String getLote() {
		return "";
	}

	protected String getNomeArquivo() {
		return "RespostasPesquisa";
	}
	
	protected String getPeriodo() {
		if (filterDTO == null) {
			return "";
		}
		return " - " + filterDTO.getDataInicioFormatada() + " até "
				+ filterDTO.getDataFimFormatada();
	}

	@SuppressWarnings("finally")
	protected String getPropertyValue(int interator, Object entity, PesquisaFacade pesquisaFacade) {

		String value = new String();

		try {
			Pesquisa pesquisa = ((Pesquisa) entity);
			for (ColetaPesquisa coleta : pesquisa.getColetasPesquisa()) {
				if (coleta.getPergunta().getId().intValue() == interator) {
					if(coleta.getPergunta().getTipo().getId()==2){
						value = pesquisaFacade.getReposta(coleta);
					}else
						value = getReposta(coleta);
					
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return value;
		}
	}
	protected String getTitulo() {
		return "RESPOSTAS DA PESQUISA POR QUESTIONARIO";
	}

	protected HSSFCellStyle getTituloSubTitle() {
		HSSFCellStyle titulo3 = workbook.createCellStyle();
		titulo3.setFillForegroundColor(HSSFColor.WHITE.index);
		titulo3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titulo3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titulo3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titulo3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titulo3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titulo3.setBottomBorderColor(HSSFColor.BLACK.index);
		titulo3.setTopBorderColor(HSSFColor.BLACK.index);
		titulo3.setLeftBorderColor(HSSFColor.BLACK.index);
		titulo3.setRightBorderColor(HSSFColor.BLACK.index);
		titulo3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titulo3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFFont font3 = workbook.createFont();
		font3.setFontName(HSSFFont.FONT_ARIAL);
		font3.setFontHeightInPoints((short) 16);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font3.setItalic(true);
		font3.setColor(HSSFColor.BLACK.index);

		titulo3.setFont(font3);

		return titulo3;

	}
	
	protected HSSFCellStyle getTituloTitle() {
		return getTituloTitle(HSSFCellStyle.ALIGN_LEFT);
	}
	
	protected HSSFCellStyle getTituloTitle(short aligment) {

		HSSFCellStyle tituloTitle = workbook.createCellStyle();
		tituloTitle.setFillForegroundColor(HSSFColor.WHITE.index);
		tituloTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		tituloTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		tituloTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		tituloTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		tituloTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		tituloTitle.setBottomBorderColor(HSSFColor.BLACK.index);
		tituloTitle.setTopBorderColor(HSSFColor.BLACK.index);
		tituloTitle.setLeftBorderColor(HSSFColor.BLACK.index);
		tituloTitle.setRightBorderColor(HSSFColor.BLACK.index);
		tituloTitle.setAlignment(aligment);
		tituloTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFFont fontTitle = workbook.createFont();
		fontTitle.setFontName(HSSFFont.FONT_ARIAL);
		fontTitle.setFontHeightInPoints((short) 8);
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontTitle.setItalic(true);
		fontTitle.setColor(HSSFColor.BLACK.index);

		tituloTitle.setFont(fontTitle);

		return tituloTitle;

	}

	protected boolean showLote() {
		return false;
	}

	protected boolean showPeriodo() {
		return false;
	}

	protected void writeBody(int row, HSSFSheet sheet, Object pojo,	LinkedHashMap<String, Titulo> titles, PesquisaFacade pesquisaFacade) {

		try {

			Iterator it = titles.keySet().iterator();

			int col = 0;

			HSSFRow rowText = sheet.createRow(row);

			while (it.hasNext()) {

				Object value = null;
				
				String property = (String) it.next();
				
				if (property.equals(PROPERTY_COL_LATITUDE)) {
					value = ((Pesquisa)pojo).getLatitudeFinal();
				}
				else if (property.equals(PROPERTY_COL_LONGITUDE)) {
					value = ((Pesquisa)pojo).getLongitudeFinal();
				}
				else if (property.equals(PROPERTY_COL_FOTOS)) {
					value = ((Pesquisa) pojo).getNomeFotos();
				}
				else {
					value = getPropertyValue(Integer.parseInt(property), pojo,pesquisaFacade);
				}
			
				Titulo titulo = titles.get(property);
				EntityColumnInfo entityColumnInfo = titulo.getEntityColumnInfo();

				String valueObject = ("");

				HSSFCell celula = rowText.createCell(col);

				if (value != null) {

					if (value instanceof Date) {

						if (entityColumnInfo.isDatahora())
							valueObject = SistemaUtil.formatDateTime((Date) value);
						else
							valueObject = SistemaUtil.formatDate((Date) value);

						celula.setCellValue(valueObject);

					} else if (value instanceof Double) {
						celula.setCellValue((Double) value);
					} else if (value instanceof Integer) {
						celula.setCellValue((Integer) value);
					} else if (value instanceof Number) {
						String v = String.valueOf(value);
						try {
							Double d = new Double(v);
							celula.setCellValue(d);
						} catch (Exception e) {
							e.printStackTrace();
							celula.setCellValue(v);
						}
					} else {
						valueObject = value.toString();
						if (valueObject.trim().isEmpty()) {
							valueObject = "";
						}else if(valueObject.length() > 254){
							HSSFCell cel = rowText.createCell(col);
							cel.setCellStyle(getBodyFont(entityColumnInfo.getAligment()));
							
							String[] respostasMultiplas = valueObject.split(",");
							cel.setCellValue("");
							
							for(String resposta : respostasMultiplas){
								if(celula.getStringCellValue().length() <= 254 && (celula.getStringCellValue().length()+resposta.length())<=254){
									valueObject += ","+resposta;
								}else{
									cel.setCellValue(cel.getStringCellValue()+","+resposta);
								}
							}
							
							row++;
						}
						celula.setCellValue(valueObject);
					}
				} else {
					celula.setCellValue("");
				}

				sheet.autoSizeColumn(col);

				celula.setCellStyle(getBodyFont(entityColumnInfo.getAligment()));

				col++;
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeBodyAba0(HSSFSheet sheet, LinkedHashMap<String, Titulo> titles) {

		try {
			// serao 2 colunas para a aba de informacoes
			int colunas = 2;

			// cria fontes para a aba 0 - Informacoes
			HSSFFont font = workbook.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 8);
			font.setColor(HSSFColor.BLACK.index);
			
			// cria estilo de coluna 0 - Textos
			HSSFCellStyle styleText = workbook.createCellStyle();
			styleText.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			styleText.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleText.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleText.setBottomBorderColor(HSSFColor.BLACK.index);
			styleText.setTopBorderColor(HSSFColor.BLACK.index);
			styleText.setLeftBorderColor(HSSFColor.BLACK.index);
			styleText.setRightBorderColor(HSSFColor.BLACK.index);
			styleText.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleText.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleText.setWrapText(true);
			styleText.setFont(font);
			
			// cria estilo de coluna 1 - Valores
			HSSFCellStyle styleValue = workbook.createCellStyle();
			styleValue.setFillForegroundColor(HSSFColor.WHITE.index);
			styleValue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleValue.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleValue.setBottomBorderColor(HSSFColor.BLACK.index);
			styleValue.setTopBorderColor(HSSFColor.BLACK.index);
			styleValue.setLeftBorderColor(HSSFColor.BLACK.index);
			styleValue.setRightBorderColor(HSSFColor.BLACK.index);
			styleValue.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleValue.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleValue.setWrapText(true);
			styleValue.setFont(font);
			
			
			int[] maxLenght = new int[2];
			maxLenght[0] = 12;
			
			// Linha 2 - Nome do formulario
			HSSFRow row2 = criaLinha(sheet, titles, colunas, 2, getTituloSubTitle());
			row2.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			String nomeFormulario = "Formulário";
			HSSFCell cellText1 = row2.createCell(0);
			cellText1.setCellValue(nomeFormulario);
			cellText1.setCellStyle(getTituloSubTitle());
			cellText1.setCellStyle(styleText);
			
			HSSFCell cellValue1 = row2.createCell(1);
			if (empresa == null) {
				empresa = new Empresa();
				empresa.setRazaoSocial("");
				empresa.setCnpj("");
				empresa.setCrea("");
			}
			cellValue1.setCellValue("Questionários Respondidos");
			cellValue1.setCellStyle(getTituloSubTitle());
			cellValue1.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], nomeFormulario != null ? nomeFormulario.length() : 5);

			// Linha 3 - versao do formulario
			HSSFRow row3 = criaLinha(sheet, titles, colunas, 3, getTituloSubTitle());
			row3.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			String versaoFormulario = "1";
			HSSFCell cellText2 = row3.createCell(0);
			cellText2.setCellValue("Versão");
			cellText2.setCellStyle(getTituloSubTitle());
			cellText2.setCellStyle(styleText);

			HSSFCell cellValue2 = row3.createCell(1);
			cellValue2.setCellValue(versaoFormulario);
			cellValue2.setCellStyle(getTituloSubTitle());
			cellValue2.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], versaoFormulario != null ? versaoFormulario.length() : 5);

			// Linha 4 - Data de criacao
			HSSFRow row4 = criaLinha(sheet, titles, colunas, 4, getTituloSubTitle());
			row4.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			String dataFormulario = new SimpleDateFormat(FormatoDataEnum.YYYYMMDDHHMMSS.toString()).format(new Date());
			HSSFCell cellText3 = row4.createCell(0);
			cellText3.setCellValue("Data de Criação");
			cellText3.setCellStyle(getTituloSubTitle());
			cellText3.setCellStyle(styleText);

			HSSFCell cellValue3 = row4.createCell(1);
			cellValue3.setCellValue(dataFormulario);
			cellValue3.setCellStyle(getTituloSubTitle());
			cellValue3.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], dataFormulario != null ? dataFormulario.length() : 5);

			
			// define largura das colunas
			sheet.setColumnWidth(0, maxLenght[0] * 330);
			sheet.setColumnWidth(1, maxLenght[1] * 345);
			
		} catch (Exception ex) {
			 Logger.getLogger(XLSExportRespostaPesquisa.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void writeBodyAba1(HSSFSheet sheet,
			LinkedHashMap<String, Titulo> titles) {

		try {

			// serao 2 colunas para a aba de empresa
			int colunas = 2;

			// Estilo Titulo 1
			HSSFCellStyle titulo1 = workbook.createCellStyle();
			titulo1.setFillForegroundColor(HSSFColor.WHITE.index);
			titulo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			titulo1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderLeft(HSSFCellStyle.BORDER_THIN);

			titulo1.setBottomBorderColor(HSSFColor.WHITE.index);
			titulo1.setTopBorderColor(HSSFColor.WHITE.index);
			titulo1.setLeftBorderColor(HSSFColor.WHITE.index);
			titulo1.setRightBorderColor(HSSFColor.WHITE.index);

			titulo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titulo1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFFont font1 = workbook.createFont();
			font1.setFontName(HSSFFont.FONT_ARIAL);
			font1.setFontHeightInPoints((short) 16);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font1.setItalic(true);
			font1.setColor(HSSFColor.WHITE.index);

			titulo1.setFont(font1);

			// cria fontes para a aba 1 - Empresa
			HSSFFont font = workbook.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 8);
			font.setColor(HSSFColor.BLACK.index);
			
			// cria estilo de coluna 0 - Textos
			HSSFCellStyle styleText = workbook.createCellStyle();
			styleText.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			styleText.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleText.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleText.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleText.setBottomBorderColor(HSSFColor.BLACK.index);
			styleText.setTopBorderColor(HSSFColor.BLACK.index);
			styleText.setLeftBorderColor(HSSFColor.BLACK.index);
			styleText.setRightBorderColor(HSSFColor.BLACK.index);
			styleText.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleText.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleText.setWrapText(true);
			styleText.setFont(font);
			
			// cria estilo de coluna 1 - Valores
			HSSFCellStyle styleValue = workbook.createCellStyle();
			styleValue.setFillForegroundColor(HSSFColor.WHITE.index);
			styleValue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleValue.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleValue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleValue.setBottomBorderColor(HSSFColor.BLACK.index);
			styleValue.setTopBorderColor(HSSFColor.BLACK.index);
			styleValue.setLeftBorderColor(HSSFColor.BLACK.index);
			styleValue.setRightBorderColor(HSSFColor.BLACK.index);
			styleValue.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleValue.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleValue.setWrapText(true);
			styleValue.setFont(font);
			
			
			int[] maxLenght = new int[2];
			maxLenght[0] = 12;
			
			// Linha 2 - Razao Social
			HSSFRow row2 = criaLinha(sheet, titles, colunas, 2, getTituloSubTitle());
			row2.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cellText1 = row2.createCell(0);
			cellText1.setCellValue("Razão Social");
			cellText1.setCellStyle(getTituloSubTitle());
			cellText1.setCellStyle(styleText);
			
			HSSFCell cellValue1 = row2.createCell(1);
			if (empresa == null) {
				empresa = new Empresa();
				empresa.setRazaoSocial("");
				empresa.setCnpj("");
				empresa.setCrea("");
			}
			cellValue1.setCellValue(empresa.getRazaoSocial() != null ? empresa.getRazaoSocial() : "");
			cellValue1.setCellStyle(getTituloSubTitle());
			cellValue1.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], empresa.getRazaoSocial() != null ? empresa.getRazaoSocial().length() : 0);

			// Linha 3 - CNPJ
			HSSFRow row3 = criaLinha(sheet, titles, colunas, 3, getTituloSubTitle());
			row3.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cellText2 = row3.createCell(0);
			cellText2.setCellValue("CNPJ Empresa");
			cellText2.setCellStyle(getTituloSubTitle());
			cellText2.setCellStyle(styleText);

			HSSFCell cellValue2 = row3.createCell(1);
			cellValue2.setCellValue(empresa.getCnpj() != null ? empresa.getCnpj() : "");
			cellValue2.setCellStyle(getTituloSubTitle());
			cellValue2.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], empresa.getCnpj() != null ? empresa.getCnpj().length() : 0);

			// Linha 4 - CREA
			HSSFRow row4 = criaLinha(sheet, titles, colunas, 4, getTituloSubTitle());
			row4.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cellText3 = row4.createCell(0);
			cellText3.setCellValue("CREA Empresa");
			cellText3.setCellStyle(getTituloSubTitle());
			cellText3.setCellStyle(styleText);

			HSSFCell cellValue3 = row4.createCell(1);
			cellValue3.setCellValue(empresa.getCrea() != null ? empresa.getCrea() : "");
			cellValue3.setCellStyle(getTituloSubTitle());
			cellValue3.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], empresa.getCrea() != null ? empresa.getCrea().length() : 0);

			// Referencias de datas
			final Calendar agora = Calendar.getInstance();

			// Linha 5 - Mes Referencia
			HSSFRow row5 = criaLinha(sheet, titles, colunas, 5, getTituloSubTitle());
			row5.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cellText4 = row5.createCell(0);
			cellText4.setCellValue("Mês Referência");
			cellText4.setCellStyle(getTituloSubTitle());
			cellText4.setCellStyle(styleText);

			HSSFCell cellValue4 = row5.createCell(1);
			int mesReferencia = agora.get(Calendar.MONTH) + 1;
			cellValue4.setCellValue(mesReferencia);
			cellValue4.setCellStyle(getTituloSubTitle());
			cellValue4.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], 2);

			// Linha 6 - Ano Referencia
			HSSFRow row6 = criaLinha(sheet, titles, colunas, 6, getTituloSubTitle());
			row6.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cellText5 = row6.createCell(0);
			cellText5.setCellValue("Ano Referência");
			cellText5.setCellStyle(getTituloSubTitle());
			cellText5.setCellStyle(styleText);

			HSSFCell cellValue5 = row6.createCell(1);
			int anoReferencia = agora.get(Calendar.YEAR);
			cellValue5.setCellValue(anoReferencia);
			cellValue5.setCellStyle(getTituloSubTitle());
			cellValue5.setCellStyle(styleValue);
			
			maxLenght[1] = Math.max(maxLenght[1], 4);
			
			// define largura das colunas
			sheet.setColumnWidth(0, maxLenght[0] * 330);
			sheet.setColumnWidth(1, maxLenght[1] * 345);
			
		} catch (Exception ex) {
			 Logger.getLogger(XLSExportRespostaPesquisa.class.getName()).log(Level.SEVERE,
			 null, ex);
		}
	}

	/**
	 * 
	 * @param sheet
	 * @param titles
	 */
	protected void writeHeader(HSSFSheet sheet,
			LinkedHashMap<String, Titulo> titles) {

		try {

			int colunas = titles.keySet().size();

			// Estilo Titulo 1
			HSSFCellStyle titulo1 = workbook.createCellStyle();
			titulo1.setFillForegroundColor(HSSFColor.WHITE.index);
			titulo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			titulo1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titulo1.setBorderLeft(HSSFCellStyle.BORDER_THIN);

			titulo1.setBottomBorderColor(HSSFColor.WHITE.index);
			titulo1.setTopBorderColor(HSSFColor.WHITE.index);
			titulo1.setLeftBorderColor(HSSFColor.WHITE.index);
			titulo1.setRightBorderColor(HSSFColor.WHITE.index);

			titulo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titulo1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFFont font1 = workbook.createFont();
			font1.setFontName(HSSFFont.FONT_ARIAL);
			font1.setFontHeightInPoints((short) 16);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font1.setItalic(true);
			font1.setColor(HSSFColor.WHITE.index);

			titulo1.setFont(font1);

			// Linha 0
			HSSFRow row0 = criaLinha(sheet, titles, 0, titulo1);
			row0.setHeightInPoints(4.45f * sheet.getDefaultRowHeightInPoints());

			sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
					0, // last row (0-based)
					1, // first column (0-based)
					colunas - 1 // last column (0-based)
			));

			// Incluia a imagem
			try {
				InputStream inputStream = getClass().getResourceAsStream(
						"sisdat.png");
				byte[] bytes = IOUtils.toByteArray(inputStream);
				int pictureIdx = workbook.addPicture(bytes,
						Workbook.PICTURE_TYPE_JPEG);
				inputStream.close();

				CreationHelper helper = workbook.getCreationHelper();
				Drawing drawing = sheet.createDrawingPatriarch();

				ClientAnchor anchor = helper.createClientAnchor();
				anchor.setCol1(0);
				anchor.setRow1(0);
				anchor.setAnchorType(ClientAnchor.MOVE_DONT_RESIZE);

				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// Estilo Titulo 2
			HSSFCellStyle titulo2 = workbook.createCellStyle();
			titulo2.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
			titulo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			titulo2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titulo2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titulo2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titulo2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titulo2.setBottomBorderColor(HSSFColor.WHITE.index);
			titulo2.setTopBorderColor(HSSFColor.WHITE.index);
			titulo2.setLeftBorderColor(HSSFColor.WHITE.index);
			titulo2.setRightBorderColor(HSSFColor.WHITE.index);
			titulo2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			titulo2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFFont font2 = workbook.createFont();
			font2.setFontName(HSSFFont.FONT_ARIAL);
			font2.setFontHeightInPoints((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font2.setItalic(true);
			font2.setColor(HSSFColor.WHITE.index);

			titulo2.setFont(font2);

			// Estilo Titulo 3

			// Linha 2
			HSSFRow row2 = criaLinha(sheet, titles, 1, getTituloSubTitle());
			row2.setHeightInPoints((1 * sheet.getDefaultRowHeightInPoints()));

			HSSFCell cTitulo5 = row2.createCell(0);
			cTitulo5.setCellValue(getTitulo());
			cTitulo5.setCellStyle(getTituloSubTitle());

			sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
					1, // last row (0-based)
					0, // first column (0-based)
					colunas - 1 // last column (0-based)
			));

		} catch (Exception ex) {
			// Logger.getLogger(XLSExportParteDiariaApontamento.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	protected int writeTiltes(HSSFSheet sheet,
			LinkedHashMap<String, Titulo> titles) {

		int row = 2;

		try {

			Iterator it = titles.keySet().iterator();
			int col = 0;
			int colunas = titles.keySet().size();

			maxColumnLength = new int[colunas];

			HSSFRow rowTitle = sheet.createRow(row);

			while (it.hasNext()) {

				String property = it.next().toString();
				Titulo titulo = titles.get(property);
				String title = titulo.getTitulo();

				System.err.println("TITLE " + title);

				maxColumnLength[col] = title.length();

				HSSFCell cTitulo = rowTitle.createCell(col++);
				cTitulo.setCellValue(title);
				cTitulo.setCellStyle(getTituloTitle());

			}

			sheet.setAutoFilter(new CellRangeAddress(row, row, 0, colunas - 1));
			sheet.createFreezePane(0, row + 1);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return row;
	}

}
