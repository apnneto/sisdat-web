/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Classe utulitária para geração de PDFs a partir de listas de entidades
 * @author Framework
 */
public class PDFExportUtil {

    static class Footer extends PdfPageEventHelper {

        private String footerText;

        public Footer(String text) {
            footerText = text;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            Rectangle rect = writer.getPageSize();

            Font footerFont = new Font();
            footerFont.setSize(8);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(footerText, footerFont), rect.getWidth() / 2, 5, 0);

        }
    }

    public static void generatePDFTable(OutputStream out, LinkedHashMap<String, String> titles, String pageTitle, List objects) {
        try {
            BeanInfo info = Introspector.getBeanInfo(objects.get(0).getClass());
            Map<String, PropertyDescriptor> descriptorMap = new HashMap<String, PropertyDescriptor>();
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor p : descriptors) {
                descriptorMap.put(p.getName(), p);
            }


            Rectangle a4 = PageSize.A4;
            Rectangle a4Landscape = a4.rotate();
            Document document = new Document(a4Landscape);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setMargins(5, 5, 5, 5);

            writer.setPageEvent(new Footer("EGESA Engenharia S/A"));

            document.open();

            Font titleFont = new Font();
            titleFont.setStyle(Font.BOLD);
            titleFont.setFamily(FontFamily.HELVETICA.name());
            titleFont.setSize(14);

            Paragraph pageTitleParagraph = new Paragraph(pageTitle, titleFont);
            pageTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            pageTitleParagraph.setSpacingAfter(10);
            document.add(pageTitleParagraph);


            PdfPTable table = new PdfPTable(titles.keySet().size());


            Font headerFont = new Font();
            headerFont.setStyle(Font.BOLD);
            headerFont.setFamily(FontFamily.HELVETICA.name());
            headerFont.setSize(8);






            Font bodyFont = new Font();
            bodyFont.setStyle(Font.NORMAL);
            bodyFont.setFamily(FontFamily.HELVETICA.name());
            bodyFont.setSize(6);


            // Adiciona os titulos
            Iterator it = titles.keySet().iterator();

            while (it.hasNext()) {
                String property = (String) it.next();
                String title = titles.get(property);
                if (title == null) {
                    continue; // Skip column
                }
                Phrase titulo = new Phrase(title, headerFont);
                PdfPCell cell = new PdfPCell(titulo);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);



                table.addCell(cell);

            }

            for (Object o : objects) {
                it = titles.keySet().iterator();
                while (it.hasNext()) {
                    String property = (String) it.next();
                    String[] props = property.split("/");

                    PropertyDescriptor pd = descriptorMap.get(props[0]);

                    Method getter = pd.getReadMethod();
                    Object value = getter.invoke(o);

                    String valueObject = new String("");

                    if (props.length > 1) {
                        int type = Integer.parseInt(props[1]);

                        switch (type) {
                            case SistemaUtil.Tipo_NUMERICO:
                                valueObject = SistemaUtil.formatNumeric(value);
                                break;
                            case SistemaUtil.Tipo_CURRENCY:
                                valueObject = SistemaUtil.formatCurrency(value);
                                break;
                            case SistemaUtil.Tipo_CNPJ:
                                valueObject = SistemaUtil.formatCnpj(value.toString());
                                break;
                            case SistemaUtil.Tipo_CPF:
                                valueObject = SistemaUtil.formatCpf(value.toString());
                                break;
                            case SistemaUtil.Tipo_DATA:
                                valueObject = SistemaUtil.formatDate((Date) value);
                                break;
                            default:
                                if(value != null) {
                                    valueObject = value.toString();
                                }
                        }

                    } else {
                        if(value != null) {
                            valueObject = value.toString();
                        }
                    }

                    table.addCell(new Phrase(valueObject, bodyFont));

                }

            }

            document.add(table);
            document.close();






        } catch (Exception ex) {
            Logger.getLogger(PDFExportUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }





    }
}
