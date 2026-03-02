package com.frw.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

import com.frw.base.util.enumeration.FormatoDataEnum;

/**
 * @author Carlos Santos
 */
public class SistemaUtil {

    public static final String CSVSeparator = ";";
    public final static int Tipo_CNPJ = 4;
    public final static int Tipo_CPF = 5;
    public final static int Tipo_CURRENCY = 2;
    public final static int Tipo_DATA = 6;

    public final static int Tipo_NUMERICO = 1;

    public final static int Tipo_STRING = 3;

    private static final int CNPJ_LENGTH = 14;
    private static final int CPF_LENGTH = 11;

    private static final NumberFormat currencyNF;

    private static NumberFormat percentFormatter = new DecimalFormat("#0.00");

    static {
        currencyNF = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        currencyNF.setMinimumFractionDigits(2);
        currencyNF.setMaximumFractionDigits(2);
    }

    public static Date adjustEndDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date adjustStartDate(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean createZipFile(File fileOrigin, File zipDestination, String namefileEntryZip) {
    	prepareFilePermissions(fileOrigin, zipDestination);
    	return generateZipFile(fileOrigin, zipDestination, namefileEntryZip);
    }

    public static boolean createZipFile(String pathFileOrigin, String pathZipDestination, String namefileEntryZip) {
    	return generateZipFile(new File(pathFileOrigin), new File(pathZipDestination), namefileEntryZip);
    }

    public static BigDecimal dateDifferenceInDays(Date start, Date end) {

        if (start == null || end == null) {
            return BigDecimal.ZERO;
        }
        
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        double startMiliseconds = startCalendar.getTimeInMillis();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        double endMiliseconds = endCalendar.getTimeInMillis();

        return new BigDecimal(Math.ceil(endMiliseconds - startMiliseconds) / (1000 * 60 * 60 * 24), new MathContext(2, RoundingMode.HALF_UP));
        
    }
    
    public static boolean datesInSameMonthAndYear(Date start, Date end) {

        if (start == null || end == null) {
            return false;
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startYear = startCalendar.get(Calendar.YEAR);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endYear = endCalendar.get(Calendar.YEAR);

        return startMonth == endMonth && startYear == endYear;

    }

    public static String formatCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != CNPJ_LENGTH) {
            return "";
        }

        StringBuilder cnpjFormatado = new StringBuilder(cnpj);
        cnpjFormatado.insert(12, "-");
        cnpjFormatado.insert(8, "/");
        cnpjFormatado.insert(5, ".");
        cnpjFormatado.insert(2, ".");

        return cnpjFormatado.toString();
    }

    public static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != CPF_LENGTH) {
            return "";
        }

        StringBuilder cpfFormatado = new StringBuilder(cpf);
        cpfFormatado.insert(9, "-");
        cpfFormatado.insert(6, ".");
        cpfFormatado.insert(3, ".");

        return cpfFormatado.toString();
    }

    public static String formatCurrency(Object value) {
        return value != null ? currencyNF.format(value) : "";
    }
    
    
    public static String formatDate(Date date) {
        return formatDate(date, FormatoDataEnum.DDMMYYYY);
    }
	
    public static String formatDate(Date date, FormatoDataEnum formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato.toString());
        return date != null ? sdf.format(date) : "";
    }

	public static String formatDateTime(Date date) {
        return formatDate(date, FormatoDataEnum.DATA_HORA_MINUTO);
    }
	
	public static String formatNumeric(Object value) {
        return formatNumeric(value, true, true, 2);
    }

    public static String formatNumeric(Object value, boolean isMilhar, boolean isDecimal, int decimalDigits) {

        if (value == null) {
            return "";
        }

        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        String pattern = new String("{1}0{2}");

        //casa de milhar
        if (isMilhar) {
            pattern = pattern.replace("{1}", "###,###,##");
        } else {
            pattern = pattern.replace("{1}", "");
        }

        if (isDecimal) {

            StringBuilder patterDecimais = new StringBuilder(".");
            for (int i = 0; i < decimalDigits; i++) {
                patterDecimais.append("0");
            }

            pattern = pattern.replace("{2}", patterDecimais);
            formatter = new DecimalFormat(pattern);
            formatter.setRoundingMode(RoundingMode.HALF_DOWN);
        } else {
            pattern = pattern.replace("{2}", "");
            formatter = new DecimalFormat(pattern);
            formatter.setRoundingMode(RoundingMode.DOWN);
        }

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(value);

    }
    public static String formatNumeric(Object value, int decimal) {
    	if (value instanceof Integer) {
    		return formatNumeric(value, true, false, 0);
		}
        return formatNumeric(value, true, true, decimal);
    }
    public static String formatPercent(Object value) {
        return value != null ? percentFormatter.format(value) + "%" : "";
    }
    public static String getBooleanKey(boolean value) {
        return value ? "boolean.true.value" : "boolean.false.value";
    }
    private static boolean generateZipFile(File fileOrigin, File zipDestination, String namefileEntryZip) {
		
		byte[] buffer = new byte[1024];
		
		if (namefileEntryZip == null) {
			namefileEntryZip = "SisDAT-file" + new Date().getTime();
		}
		
    	try{
    		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipDestination));
    		zipOutputStream.putNextEntry(new ZipEntry(namefileEntryZip+ "." + FilenameUtils.getExtension(fileOrigin.getName())));
    		
    		FileInputStream in = new FileInputStream(fileOrigin);
 
    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zipOutputStream.write(buffer, 0, len);
    		}
 
    		in.close();
    		zipOutputStream.closeEntry();
 
    		zipOutputStream.close();
 
    		return zipDestination.exists();
 
    	}catch(IOException ex){
    	   ex.printStackTrace();
    	   return false;
    	}
	}
    private static void prepareFilePermissions(File fileOrigin, File fileDestination) {
		fileOrigin.setReadable(true);
    	fileOrigin.setExecutable(false);
    	fileOrigin.setWritable(false);
    	
    	fileDestination.setReadable(true);
    	fileDestination.setExecutable(false);
    	fileDestination.setWritable(true);
	}
	

}
