package com.frw.base.util.enumeration;

public enum MapMarkerPointEnum {

	BLUE					(4l, "pin-blue.png", "#0000FF"), 
	GREEN					(6l, "pin-green.png", "#00FF00"), 
	ICONE_TRANSPARENTE		(16l, "", "#00FF00"), 
	ORANGE					(2l, "pin-orange.png", "#FFA500"), 
	RED						(1l, "pin-red.png", "#FF0000"), 
	WHITE					(5l, "pin-white.png", "#FFFAFA"), 
	
	YELLOW					(3l, "pin-yellow.png", "#FFFF00");

	private String color;
	private Long id;
	private String image;

	MapMarkerPointEnum(Long id, String image, String color) {
		this.id = id;
		this.image = image;
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public Long getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return image;
	}

}
