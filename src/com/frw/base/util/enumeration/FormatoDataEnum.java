package com.frw.base.util.enumeration;

/**
 * @author Carlos Santos
 */
public enum FormatoDataEnum {

    DATA_HORA_MINUTO ("dd/MM/yyyy HH:mm:ss"),
    DD_MM_YYYY("dd-MM-yyyy"),
    DDMMYYYY("dd/MM/yyyy"),
    MM_YYYY("MM-yyyy"),
    MMYYYY("MM/yyyy"),
    YYYYMMDD("yyyy-MM-dd"),
    YYYYMMDDHHMMSS("yyyy-MM-dd HH:mm:ss");

    private String formato;

    FormatoDataEnum(String formato) {
        this.formato = formato;
    }

    @Override
    public String toString() {
        return formato;
    }

}
