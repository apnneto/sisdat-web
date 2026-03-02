package com.frw.base.web.pages.map;

public enum HeightMapEnum {
	GRANDE_600("mapa-viewSaude"),
	MEDIO_400("mapa-view");
	
	private String css;

	HeightMapEnum(String css) {
		this.css = css;
	}
	
	public String getCss() {
		return css;
	}
	
}
