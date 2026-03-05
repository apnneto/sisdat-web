package com.frw.negocio.export.xls;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.EJB;

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
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.util.SistemaUtil;
import com.frw.base.web.pages.base.EntityColumnInfo;
import com.frw.base.web.pages.base.Titulo;
import com.frw.manutencao.dominio.dto.FilterListBaseDTO;

public abstract class AbstractXLSExportRespostasPesquisa {

	@SuppressWarnings("finally")
	private static String getReposta(ColetaPesquisa coletaPesquisa){
		String resposta = "";
    	try {
			if(coletaPesquisa.getResposta() != null ){
				resposta += coletaPesquisa.getResposta().getDescricao();
			}
			
			if(coletaPesquisa.getCampoLivre() != null && !coletaPesquisa.getCampoLivre().isEmpty()){
				resposta += coletaPesquisa.getCampoLivre(); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return resposta;
		}
	}
	@EJB
	private ColetaPesquisaFacade coletaPesquisaFacade;
	
	@EJB
    private PesquisaFacade pesquisaFacade;
	
	protected FilterListBaseDTO filterDTO;

	protected HSSFCellStyle textStyleCenter;
	
	protected HSSFCellStyle textStyleLeft;

	protected HSSFCellStyle textStyleRight;

	protected HSSFWorkbook workbook;
	
	int maxColumnLength[];

	boolean primeira = true;
	public AbstractXLSExportRespostasPesquisa() {
	}
	public AbstractXLSExportRespostasPesquisa(FilterListBaseDTO filterDTO) {
		this.filterDTO = filterDTO;
	}
	
	public void generateXLSTable(ByteArrayOutputStream out, LinkedHashMap<String, Titulo> titles, List objects) {
		
		try {

			workbook = new HSSFWorkbook();
			
			HSSFSheet sheet = workbook.createSheet(getNomeAba());

			criaEstilos();

			int rowtitle = 0;
			
			for (int row = 0; row < objects.size(); row++) {

				if (row == 0) {
					writeHeader(sheet, titles);
					rowtitle = writeTiltes(sheet, titles);
					rowtitle++;
				}

				Object o = objects.get(row);
				writeBody(row + rowtitle, sheet, o, titles);

			}
			
			workbook.write(out);
			

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public String getQuestionario() {
		return ""; 
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
	protected HSSFRow criaLinha(HSSFSheet sheet, LinkedHashMap<String, Titulo> titles, int rowLine, HSSFCellStyle titulo) {
		
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


	protected String getNomeAba() {
		return "Apontamentos";
	}

	protected String getNomeArquivo() {
		return "";
	}

	protected String getPeriodo() {
		if (filterDTO == null) {
			return "";
		}
		return " - " + filterDTO.getDataInicioFormatada() + " até " + filterDTO.getDataFimFormatada();
	}


	@SuppressWarnings("finally")
	protected String getPropertyValue(int interator , Object entity) {
		
		String value = new String();

		try {
			for (ColetaPesquisa coleta : ((Pesquisa) entity).getColetasPesquisa()) {
				if(coleta.getPergunta().getId().intValue() == interator){
					value = pesquisaFacade.getReposta(coleta);
					break;
				}
			}

			/*for (int i = 0; i < interator; i++) {
				value = getReposta(((Pesquisa) entity).getColetasPesquisa().get(i));
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return value;
		}
	}

	protected String getTitulo() {
		return "SISDAT";
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
		return getTituloTitle(HSSFCellStyle.ALIGN_LEFT );
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
		return true;
	}

	protected boolean showPeriodo() {
		return true;
	}
	
	protected void writeBody(int row, HSSFSheet sheet, Object pojo, LinkedHashMap<String, Titulo> titles) {

		
		try {

			Iterator it = titles.keySet().iterator();
			
			int col = 0;

			HSSFRow rowText = sheet.createRow(row);

			
			while (it.hasNext()) {


				String property = (String) it.next();
				String[] props = new String[Integer.parseInt(property)];

				Object value = getPropertyValue(Integer.parseInt(property), pojo);
				String valueObject = ("");

				Titulo titulo = titles.get(property);
				EntityColumnInfo entityColumnInfo = titulo.getEntityColumnInfo();

				HSSFCell cTitulo = rowText.createCell(col);
				
				if (value != null) {
					
					if (value instanceof Date) {
						
						if(entityColumnInfo.isDatahora())valueObject = SistemaUtil.formatDateTime((Date) value);
						else
							valueObject = SistemaUtil.formatDate((Date) value);
						
						cTitulo.setCellValue(valueObject);
						
					} else if (value instanceof Double) {
						cTitulo.setCellValue((Double)value);
					} else if (value instanceof Integer) {
						cTitulo.setCellValue((Integer)value);
					} else if (value instanceof Number) {
						String v = String.valueOf(value);
						try {
							Double d = new Double(v);
							cTitulo.setCellValue(d);
						} catch (Exception e) {
							e.printStackTrace();
							cTitulo.setCellValue(v);
						}
					} else {
						valueObject = value.toString();
						if (valueObject.trim().isEmpty()) {
							valueObject = "";
						}
						cTitulo.setCellValue(valueObject);
					}
				} else {
					cTitulo.setCellValue("");
				}

				maxColumnLength[col] = Math.max(maxColumnLength[col], valueObject.length());
				sheet.setColumnWidth(col, maxColumnLength[col] * 330);
			
				cTitulo.setCellStyle(getBodyFont(entityColumnInfo.getAligment()));
					
				col++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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

			sheet.addMergedRegion(new CellRangeAddress(
		            0, //first row (0-based)
		            0, //last row  (0-based)
		            1, //first column (0-based)
		            colunas - 1  //last column  (0-based)
		    ));

			// Incluia a imagem
			try {
				InputStream inputStream = getClass().getResourceAsStream("sisdat.png");
				byte[] bytes = IOUtils.toByteArray(inputStream);
				int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
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

			sheet.addMergedRegion(new CellRangeAddress(
		            1, //first row (0-based)
		            1, //last row  (0-based)
		            0, //first column (0-based)
		            colunas - 1  //last column  (0-based)
		    ));

		} catch (Exception ex) {
			//Logger.getLogger(XLSExportParteDiariaApontamento.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	protected int writeTiltes(HSSFSheet sheet, LinkedHashMap<String, Titulo> titles) {
		
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
