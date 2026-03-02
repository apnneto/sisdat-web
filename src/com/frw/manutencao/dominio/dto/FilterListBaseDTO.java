package com.frw.manutencao.dominio.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.frw.base.util.enumeration.FormatoDataEnum;

public class FilterListBaseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	static SimpleDateFormat formatoData = new SimpleDateFormat(FormatoDataEnum.DD_MM_YYYY.toString());
	public static String getDataFormatada(Date data) {
		return formatoData.format(data);
	}
	
	
	public static String getDataFormatadaMesAno(Date data) {
		return new SimpleDateFormat(FormatoDataEnum.MM_YYYY.toString()).format(data);
	}

	private Date dataFim;
	
	private Date dataInicio;

	
	public FilterListBaseDTO() {
		
		Calendar inicioMes = Calendar.getInstance();
	    inicioMes.set(Calendar.DAY_OF_MONTH, 1);
	    dataInicio = new Date(inicioMes.getTimeInMillis());
	        
	    Calendar finalMes = Calendar.getInstance();
	    dataFim = (new Date(finalMes.getTimeInMillis()));
	    
	}

	public Date getDataFim() {
		return dataFim;
	}
	
	public String getDataFimFormatada() {
		
		return formatoData.format(dataFim);
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public String getDataInicioFormatada() {
		
		return formatoData.format(dataInicio);
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;		
	}

}
