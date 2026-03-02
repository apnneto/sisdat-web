/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 *
 * @author Marcelo Alves
 */
public class PDFUtil {

    static class Paginacao extends PdfPageEventHelper {

        private Integer pagina = 0;

        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        private Date timestamp;

        public Paginacao(Integer pagina, Date timestamp) {
            this.pagina = pagina;
            this.timestamp = timestamp;
        }

        public Integer getNumeroPaginas() {
            return pagina;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Rectangle rect = writer.getPageSize();

                Font footerFont = new Font();
                footerFont.setFamily(FontFamily.HELVETICA.name());
                footerFont.setSize(7);

                LineSeparator separator = new LineSeparator();
                separator.setLineColor(BaseColor.BLACK);
                separator.drawLine(writer.getDirectContent(), 15, rect.getWidth() - 15, 13);

                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, new Phrase(sdf.format(timestamp), footerFont), 50, 5, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("Gestão de Parcerias", footerFont), rect.getWidth() / 2, 5, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_LEFT, new Phrase("Página: " + pagina.toString(), footerFont), rect.getWidth() - 50, 5, 0);

                //incrementa a paginacao
                pagina++;

            } catch (Exception e) {
                Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, e);
                throw new RuntimeException(e);
            }

        }
    }

    public static int agruparPdfsByte(OutputStream os, List<byte[]> pdfs) {
        Integer numeroPaginas = 0;
        PdfCopyFields copy = null;
        try {
            if(!pdfs.isEmpty()) {
                copy = new PdfCopyFields(os);

                for (byte[] file : pdfs) {
                    PdfReader reader = new PdfReader(file);
                    numeroPaginas += reader.getNumberOfPages();
                    copy.addDocument(reader);
                }
            }
            
            return numeroPaginas;
        } catch (Exception e) {
            Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        } finally {
            if(copy != null)
                copy.close();
        }
    }

    public static Integer agruparPdfsFile(OutputStream os, List<File> pdfs) {
        PdfCopyFields copy = null;
        try {
            if(!pdfs.isEmpty()) {
                copy = new PdfCopyFields(os);

                for (File file : pdfs) {
                    PdfReader reader = new PdfReader(new FileInputStream(file));
                    copy.addDocument(reader);
                }

                return copy.getWriter().getPageNumber();
            } else {
                return 0;
            }

        } catch (Exception e) {
            Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        } finally {
            if(copy != null)
                copy.close();
        }

    }

    public static Integer geraPdfFromJPEG(List<byte[]> jpegs, Integer primeiraPagina, OutputStream os, Date timestamp) {

        if (jpegs == null || jpegs.isEmpty()) {
            try {
                os.close();
                return 0;
            } catch (Exception e) {
                Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, e);
                throw new RuntimeException(e);
            }
        }

        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, os);
            writer.setPageEvent(new Paginacao(primeiraPagina, timestamp));
            document.open();
            for (byte[] jpg : jpegs) {
                Image image;

                try {
                    image = Image.getInstance(jpg);
                } catch (Exception e) {
                    //não é uma imagem, pula para o proximo, por equanto.
                    continue;
                }

//                float percentScale = 1;
//
//                System.out.println("---");
//                System.out.println("Page: Height: " + writer.getPageSize().getHeight() + " - Width: " + writer.getPageSize().getWidth());
//                System.out.println("Image -> Height: " + image.getScaledHeight() + " - Width: " + image.getScaledWidth());
//
//                if (image.getScaledHeight() > writer.getPageSize().getHeight() - 30) {
//                    percentScale = percentScale * (1-((image.getScaledHeight()-(writer.getPageSize().getHeight() - 30))/image.getScaledHeight()));
////                    System.out.println("% Height: " + percentScale);
//                    image.scalePercent(percentScale*100);
//                }
//
////                System.out.println("Image -> Height: " + image.getScaledHeight() + " - Width: " + image.getScaledWidth());
//
//                if (image.getScaledWidth() > writer.getPageSize().getWidth() - 30) {
//                    percentScale = percentScale * (1-((image.getScaledWidth()-(writer.getPageSize().getWidth() - 30))/image.getScaledWidth()));
////                    System.out.println("% Width: " + percentScale);
//                }

                if ((image.getScaledHeight() > writer.getPageSize().getHeight() - 30) || (image.getScaledWidth() > writer.getPageSize().getWidth() - 30)) {
                    float heightDifference = image.getScaledHeight() - (writer.getPageSize().getHeight());
                    float widthDifference = image.getScaledWidth() - (writer.getPageSize().getWidth());

                    float percent;

                    if(heightDifference > widthDifference) {
                        percent = (1-((image.getScaledHeight()-(writer.getPageSize().getHeight() - 30))/image.getScaledHeight()));
                    } else {
                        percent = (1-((image.getScaledWidth()-(writer.getPageSize().getWidth() - 30))/image.getScaledWidth()));
                    }

                    image.scalePercent(percent*100);
                }

//                System.out.println("Image -> Height: " + image.getScaledHeight() + " - Width: " + image.getScaledWidth());
//                System.out.println("---");

                image.setAlignment(Image.ALIGN_CENTER);

                document.add(image);
                document.newPage();
            }
            return ((Paginacao) writer.getPageEvent()).getNumeroPaginas() - primeiraPagina;
        } catch (Exception e) {
            Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        } finally {
            document.close();
        }

    }

//    public static void main(String[] args) throws Exception {
//        File f = new File("D:\\temp\\tenda.jpg");
//        FileInputStream fis = new FileInputStream(f);
//        byte[] file = new byte[(int) f.length()];
//        fis.read(file);
//        fis.close();
//
//        File f2 = new File("D:\\temp\\img010.jpg");
//        FileInputStream fis2 = new FileInputStream(f2);
//        byte[] file2 = new byte[(int) f2.length()];
//        fis2.read(file2);
//        fis2.close();
//
//        PDFUtil.geraPdfFromJPEG(Arrays.asList(file, file2), 59, new FileOutputStream("d:\\temp\\Comprovantes.pdf"));
//    }
}
