package com.frw.base.web.util;

import java.io.Serializable;

import com.frw.base.dominio.base.EntidadeBase;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.util.enumeration.MapMarkerPointEnum;

/**
 * @author Leonardo Barros
 * @author Miller Leonardo
 */
public class MapMarkerPoint implements Serializable{
	
	private static final long serialVersionUID = -6322879201741309404L;
	
	private String bodyInfoWindow;
	private String descricao;
	private boolean isGetLatLong = Boolean.FALSE;
	private boolean isMarkCustomizado;
	private boolean isShowInfoWindow = Boolean.FALSE;
	private boolean isShowRaio = Boolean.FALSE;
	private Double latitude;
	private Double longitude;
	private MapMarkerPointEnum mapMarkerPointEnum;
	private Integer raio;
	private String title;
	private String urlImgMark;
	private Integer velocidade;
	
	private int zoom = 3;
	
	public MapMarkerPoint() {
		
	}
	
	/**
	 * Construtor para ponto invisivel do mapa (usado quando se 
	 * deseja evitar que o mapa marque o ponto (0.0, 0.0). 
	 * @param latitude
	 * @param longitude
	 */
	public MapMarkerPoint(Double latitude, Double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
		setShowInfoWindow(false);
		setMarkCustomizado(true);
		setMapMarkerPointEnum(MapMarkerPointEnum.ICONE_TRANSPARENTE);
		setTitle("");
	}


	// Prepara o Ponto de Pesquisa
	public MapMarkerPoint(Pesquisa entity){
		setLatitude(entity.getLatitudeFinal());
		setLongitude(entity.getLongitudeFinal());
		setShowInfoWindow(true);
		setBodyInfoWindow(getInfoSisDat(entity));
		setMarkCustomizado(true);
		setMapMarketPointSisDat(entity);
		setTitle(entity.getNumero() + (entity.getDevice() != null ? " - " + entity.getDevice() : "" ));
	}

	public String getBodyInfoWindow() {
		return bodyInfoWindow;
	}

	public String getDescricao() {
		return descricao;
	}
	public String getInfoSisDat(Pesquisa pesquisa) {
		StringBuilder info = new StringBuilder();
		
		if (pesquisa.getNumero() != null && !pesquisa.getNumero().trim().isEmpty()) {
			info.append("<b>Pesquisa: </b>"+ pesquisa.getNumero() +"<br/>");
		}
		
		if (pesquisa.getDevice() != null && !pesquisa.getDevice().trim().isEmpty()) {
			info.append("<b>Dispositivo: </b>"+ pesquisa.getDevice() +"<br/>");
		}
		
		if (pesquisa.getFechamento() != null && !pesquisa.getFechamento().trim().isEmpty()) {
			info.append("<b>Fechamento: </b>"+ pesquisa.getFechamento() +"<br/>");
		}
		
		if (pesquisa.getQuestionario() != null) {
			Questionario questionario = pesquisa.getQuestionario();
			if (questionario.getDescricao() != null && !questionario.getDescricao().trim().isEmpty()) {
				info.append("<b>Questionário: </b>"+ pesquisa.getQuestionario().getDescricao() +"<br/>");
			}
			if (questionario.getOrientacao() != null && !questionario.getOrientacao().trim().isEmpty()) {
				info.append("<b>Orientação: </b>"+ pesquisa.getQuestionario().getOrientacao() +"<br/>");
			}
		}
		
		return info.toString();
	}
	/**
	 * Remove caracteres especiais da info window.
	 * @return
	 */
	/*private void removerCaracteresEspeciais(Vaga vaga) {
		String info = vaga.getInformacoesComplementares().replace("\r", "");
		info = info.replace("\n", "");
		info = info.replace("\t", "");
		vaga.setInformacoesComplementares(info);
	}*/

	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public MapMarkerPointEnum getMapMarkerPointEnum() {
		return mapMarkerPointEnum;
	}
	public Integer getRaio() {
		return raio;
	}
	public String getTitle() {
		return title;
	}
	public String getUrlImgMark() {
		return urlImgMark;
	}
	public Integer getVelocidade() {
		return velocidade;
	}
	public int getZoom() {
		return zoom;
	}
	public boolean isGetLatLong() {
		return isGetLatLong;
	}
	public boolean isMarkCustomizado() {
		return isMarkCustomizado;
	}
	public boolean isShowInfoWindow() {
		return isShowInfoWindow;
	}
	public boolean isShowRaio() {
		return isShowRaio;
	}
	public void setBodyInfoWindow(String bodyInfoWindow) {
		this.bodyInfoWindow = bodyInfoWindow;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setGetLatLong(boolean isGetLatLong) {
		this.isGetLatLong = isGetLatLong;
	}
	public void setLatitude(Double latidude) {
		this.latitude = latidude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setMapMarkerPointEnum(MapMarkerPointEnum mapMarkerPointEnum) {
		this.mapMarkerPointEnum = mapMarkerPointEnum;
	}
	public void setMarkCustomizado(boolean isMarkCustomizado) {
		this.isMarkCustomizado = isMarkCustomizado;
	}
	public void setRaio(Integer raio) {
		this.raio = raio;
	}
	public void setShowInfoWindow(boolean isShowInfoWindow) {
		this.isShowInfoWindow = isShowInfoWindow;
	}

	public void setShowRaio(boolean isShowRaio) {
		this.isShowRaio = isShowRaio;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setUrlImgMark(String urlImgMark) {
		this.urlImgMark = urlImgMark;
		this.isMarkCustomizado = true;
	}
	public void setVelocidade(Integer velocidade) {
		this.velocidade = velocidade;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
    
		
	private void setMapMarketPointSisDat(EntidadeBase entity) {
		if (entity instanceof Pesquisa) {
			setMapMarkerPointEnum(MapMarkerPointEnum.RED);
		}
	}
	 
}
