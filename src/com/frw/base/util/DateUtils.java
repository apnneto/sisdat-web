package com.frw.base.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Santos
 * @author Marcelo Alves
 */
public class DateUtils {

    public static class MesAno implements Serializable, Comparable<MesAno> {

        private Integer ano;

        private Integer mes;

        private NumberFormat mesFormat = new DecimalFormat("00");
        public MesAno(Integer mes, Integer ano) {
            this.mes = mes;
            this.ano = ano;
        }

        @Override
        public int compareTo(MesAno other) {
            if(this.getAno() > other.getAno()) {
                return 1;
            } else if(this.getAno() < other.getAno()) {
                return -1;
            } else {
                if(this.getMes() > other.getMes()) {
                    return 1;
                } else if(this.getMes() < other.getMes()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }

        @Override
        public boolean equals(Object object) {
            if (!this.getClass().isInstance(object)) {
                return false;
            }
            MesAno other = (MesAno) object;
            if ((this.mes == null && other.mes != null) || (this.mes != null && !this.mes.equals(other.mes))) {
                return false;
            } else if((this.ano == null && other.ano != null) || (this.ano != null && !this.ano.equals(other.ano))) {
                return false;
            }
            return true;
        }

        public Integer getAno() {
            return ano;
        }

        public Date getEndDate() {
            Calendar endDate = Calendar.getInstance();
            endDate.set(Calendar.DATE, 1);
            endDate.set(Calendar.MONTH, (mes - 1));
            endDate.set(Calendar.YEAR, ano);
            endDate.set(Calendar.DATE, endDate.getActualMaximum(Calendar.DATE));
            return SistemaUtil.adjustEndDate(endDate.getTime());
        }

        public Integer getMes() {
            return mes;
        }

        public Date getStartDate() {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.DATE, 1);
            startDate.set(Calendar.MONTH, (mes - 1));
            startDate.set(Calendar.YEAR, ano);
            return SistemaUtil.adjustStartDate(startDate.getTime());
        }

        @Override
        public int hashCode() {
            int hash = 0;
            hash += (mes != null ? mes.hashCode() : 0);
            hash += (ano != null ? ano.hashCode() : 0);
            return hash;
        }

        public void setAno(Integer ano) {
            this.ano = ano;
        }

        public void setMes(Integer mes) {
            this.mes = mes;
        }

        @Override
        public String toString() {
            return mesFormat.format(mes) + "/" + ano;
        }
    }

    /**
     * Dado um intervalo de datas, gera um mapa de dias para cada mês.
     * 
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    public static Map<MesAno,Integer> calculateDayDistribution(Date dataInicial, Date dataFinal) {

        //Mapa onde a chave é o mês (de 1 a 12) e ano, e o valor é a quantidade de dias que são daquele mês
        Map<MesAno,Integer> distributionMap = null;

        distributionMap = generateDistribution(distributionMap, dataInicial, dataFinal);

        return distributionMap;
    }

    /*
     * Cria uma lista de ano a partir de um intervalo de datas.
     * Permite informar se o mês/ano da data de fim deverá ser considerado ou não.
     */
    public static List<Integer> criarListaAno(Date dataInicio, Date dataFim) {
        List<Integer> result = new ArrayList<Integer>();

        Calendar dataInicioCalendar = Calendar.getInstance();
        dataInicioCalendar.setTime(dataInicio);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataFim);

        /* lista todos os meses decorridos desde o início da obra */
        while (dataInicioCalendar.get(Calendar.YEAR) <= dataFinal.get(Calendar.YEAR)) {
            result.add(dataInicioCalendar.get(Calendar.YEAR));
            dataInicioCalendar.add(Calendar.YEAR, +1);
        }

        return result;
    }

    /*
     * Cria uma lista de meses ano a partir de um intervalo de datas.
     * Permite informar se o mês/ano da data de fim deverá ser considerado ou não.
     */
    public static List<MesAno> criarListaMesAno(Date dataInicio, Date dataFim, boolean considerarMesDataFim) {
        List<MesAno> result = new ArrayList<MesAno>();

        Calendar dataInicioObra = Calendar.getInstance();
        dataInicioObra.setTime(SistemaUtil.adjustStartDate(dataInicio));

        /* não devemos considerar o mês atual na lista de meses/ano */
        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataFim);
        if (!considerarMesDataFim) {
            dataFinal.add(Calendar.MONTH, -1);
        }

        /* lista todos os meses decorridos desde o início da obra */
        while (!dataFinal.before(dataInicioObra)) {
            result.add(DateUtils.dateAsMesAno(dataFinal.getTime()));
            dataFinal.add(Calendar.MONTH, -1);
        }

        return result;
    }

    public static MesAno dateAsMesAno(Date data) {

        Calendar dataBaseCalendar = Calendar.getInstance();
        dataBaseCalendar.setTime(data);

        return new MesAno(dataBaseCalendar.get(Calendar.MONTH) + 1, dataBaseCalendar.get(Calendar.YEAR));
    }

    public static int daysBetween(Date dataInicial, Date dataFinal) {

        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();

        startDate.setTime(dataInicial);
        endDate.setTime(dataFinal);

        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        startDate.setLenient(true);
        endDate.setLenient(true);

        Calendar sDate = (Calendar) startDate.clone();
        int daysBetween = 0;

        int y1 = sDate.get(Calendar.YEAR);
        int y2 = endDate.get(Calendar.YEAR);
        int m1 = sDate.get(Calendar.MONTH);
        int m2 = endDate.get(Calendar.MONTH);

        //**year optimization**
        while (((y2 - y1) * 12 + (m2 - m1)) > 12) {

            //move to Jan 01
            if ( sDate.get(Calendar.MONTH) == Calendar.JANUARY
                 && sDate.get(Calendar.DAY_OF_MONTH) == sDate.getActualMinimum(Calendar.DAY_OF_MONTH)) {

                daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_YEAR);
                sDate.add(Calendar.YEAR, 1);
            } else {
                int diff = 1 + sDate.getActualMaximum(Calendar.DAY_OF_YEAR) - sDate.get(Calendar.DAY_OF_YEAR);
                sDate.add(Calendar.DAY_OF_YEAR, diff);
                daysBetween += diff;
            }
            y1 = sDate.get(Calendar.YEAR);
        }

        //** optimize for month **
        //while the difference is more than a month, add a month to start month
        while ((m2 - m1) % 12 > 1) {
            daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_MONTH);
            sDate.add(Calendar.MONTH, 1);
            m1 = sDate.get(Calendar.MONTH);
        }

        // process remainder date
        while (sDate.before(endDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }

        return daysBetween;

    }


    public static Date endDay(Date dataFim) {
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(dataFim);
		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		
		return endDate.getTime();
	}

    public static Date getFirstDateOfMonth(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 01);

        return cal.getTime();
    }

    public static Date getFirstDateOfNextMonth(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 01);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);

        return cal.getTime();
    }

    public static Date getLastDateOfMonth(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Integer lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);

        return cal.getTime();
    }

    public static MesAno getMesAnoAnterior(MesAno mesAnoAtual) {

        Calendar dataBaseCalendar = Calendar.getInstance();
        dataBaseCalendar.set(Calendar.DATE, 1);
        dataBaseCalendar.set(Calendar.YEAR, mesAnoAtual.ano);
        dataBaseCalendar.set(Calendar.MONTH, mesAnoAtual.mes - 1);
        dataBaseCalendar.add(Calendar.MONTH, -1);

        return new MesAno(dataBaseCalendar.get(Calendar.MONTH) + 1, dataBaseCalendar.get(Calendar.YEAR));
    }

    public static MesAno getMesAnoSeguinte(Date data, boolean considerarMesCorrente) {

        MesAno mesAnoSeguinte = null;

        Calendar dataBaseCalendar = Calendar.getInstance();
        dataBaseCalendar.setTime(data);
        dataBaseCalendar.set(Calendar.DATE, 1);
        dataBaseCalendar.add(Calendar.MONTH, 1);

        /* cria mes/ano seguinte apenas se a data base calculada for anterior ao mês corrente */
        if (considerarMesCorrente || !DateUtils.dateAsMesAno(dataBaseCalendar.getTime()).equals(DateUtils.dateAsMesAno(Calendar.getInstance().getTime()))) {
            mesAnoSeguinte = new MesAno(dataBaseCalendar.get(Calendar.MONTH) + 1, dataBaseCalendar.get(Calendar.YEAR));
        }

        return mesAnoSeguinte;
    }


    public static int getNumeroDiasMes(MesAno mesAno) {
        Calendar mesAnoCalendar = Calendar.getInstance();
        mesAnoCalendar.setTime(mesAno.getStartDate());
        return mesAnoCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static boolean isMesFevereiro(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        return calendar.get(Calendar.MONTH) == 1;
    }

	public static Date startDay(Date dataInicio) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(dataInicio);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        
        return startDate.getTime();
	}

	private static Map<MesAno,Integer> generateDistribution(Map<MesAno,Integer> distributionMap, Date dataInicial, Date dataFinal) {

        boolean firstExecution = false;

        if(distributionMap == null) {
            distributionMap = new HashMap<MesAno, Integer>();
            firstExecution = true;
        }

        Date dInicial = dataInicial;
        Date dFinal = dataFinal;

        Calendar calendarDataInicial = Calendar.getInstance();
        calendarDataInicial.setLenient(true);
        calendarDataInicial.setTime(dataInicial);
        calendarDataInicial.set(Calendar.HOUR_OF_DAY, 0);
        calendarDataInicial.set(Calendar.MINUTE, 0);
        calendarDataInicial.set(Calendar.SECOND, 0);
        calendarDataInicial.set(Calendar.MILLISECOND, 0);

        Calendar calendarDataFinal = Calendar.getInstance();
        calendarDataFinal.setLenient(true);
        calendarDataFinal.setTime(dataFinal);
        calendarDataFinal.set(Calendar.HOUR_OF_DAY, 0);
        calendarDataFinal.set(Calendar.MINUTE, 0);
        calendarDataFinal.set(Calendar.SECOND, 0);
        calendarDataFinal.set(Calendar.MILLISECOND, 0);

        if(calendarDataFinal.compareTo(calendarDataInicial) == 1) {

            Integer mesDataInicial = calendarDataInicial.get(Calendar.MONTH)+1;
            Integer anoDataInicial = calendarDataInicial.get(Calendar.YEAR);

            Integer mesDataFinal = calendarDataFinal.get(Calendar.MONTH)+1;
            Integer anoDataFinal = calendarDataFinal.get(Calendar.YEAR);

            MesAno mesAnoDataInicial = new MesAno(mesDataInicial, anoDataInicial);
            MesAno mesAnoDataFinal = new MesAno(mesDataFinal, anoDataFinal);

            if(mesAnoDataInicial.equals(mesAnoDataFinal)) {
                int diferencaDias = daysBetween(dataInicial, dataFinal);
                //Necessário pois o calculo de diferença não conta a data inicial
                if(!firstExecution) {
                    diferencaDias ++;
                }
                distributionMap.put(mesAnoDataInicial, diferencaDias);
            } else {

                Date ultimoDiaMes = getLastDateOfMonth(dataInicial);

                int diferencaDias = daysBetween(dataInicial, ultimoDiaMes);
                //Necessário pois o calculo de diferença não conta a data inicial
                if(!firstExecution) {
                    diferencaDias ++;
                }
                distributionMap.put(mesAnoDataInicial, diferencaDias);

                Date dataInicialProximoMes = getFirstDateOfNextMonth(dataInicial);

                distributionMap = generateDistribution(distributionMap, dataInicialProximoMes, dataFinal);
            }

            return distributionMap;
        } else {
            return distributionMap;
        }

    }

}
