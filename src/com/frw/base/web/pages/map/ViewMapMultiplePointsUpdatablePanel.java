package com.frw.base.web.pages.map;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptUrlReferenceHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.frw.base.web.pages.BasePage;
import com.frw.base.web.util.MapMarkerPoint;

/** 
 *
 * @author Leonardo Barros
 * 
 */
public class ViewMapMultiplePointsUpdatablePanel extends Panel {
	
	private static final int DEFAULT_ZOOM = 5;
	
	private HeightMapEnum heightMapEnum;
	private boolean isShowInfoWindow = Boolean.TRUE; 
	private boolean isShowRaio = Boolean.FALSE;
	private List<MapMarkerPoint> listPoint;

	private int zoom=DEFAULT_ZOOM;

	
    public ViewMapMultiplePointsUpdatablePanel(String id, List<MapMarkerPoint> listMapMarkerPoint) {
    	this(id, listMapMarkerPoint, DEFAULT_ZOOM);
    }
    
    public ViewMapMultiplePointsUpdatablePanel(String id, List<MapMarkerPoint> listMapMarkerPoint, HeightMapEnum heightMapEnum) {
    	this(id, listMapMarkerPoint, DEFAULT_ZOOM);
    	this.heightMapEnum = heightMapEnum;
    }
    
    public ViewMapMultiplePointsUpdatablePanel(String id, List<MapMarkerPoint> listMapMarkerPoint, int zoom) {
        super(id);
        this.zoom = zoom;
        setOutputMarkupId(true);
        this.listPoint = listMapMarkerPoint;
        
        WebMarkupContainer mapContainer = new WebMarkupContainer("map");
        add(mapContainer);
        
        Label jsLabel = new Label("javaScriptContainer", getJavaScript(listPoint));
        jsLabel.setEscapeModelStrings(false);
        add(jsLabel);
    	
    }


	public void setListMapMarkerPoint(List<MapMarkerPoint> listMapMarkerPoint){
		listPoint = listMapMarkerPoint;
	}

    
    public void updateListMapMarkerPoint(List<MapMarkerPoint> listMapMarkerPoint, AjaxRequestTarget target) {
		
		if (target != null) {
			target.appendJavaScript("clearMarkers();");
			StringBuilder js = new StringBuilder();
			
			/*if (listMapMarkerPoint != null && listMapMarkerPoint.isEmpty()) {
				listMapMarkerPoint.add(new MapMarkerPoint(-19.918917, -43.938672));
			}*/
			
			addMarkers(listMapMarkerPoint, js);
			target.appendJavaScript(js.toString());
		}
	}
    
    private void addMarkers(List<MapMarkerPoint> lista, StringBuilder js) {
        StringBuilder arraylng = new StringBuilder();
        StringBuilder arraylat = new StringBuilder();
        StringBuilder arrayTitle = new StringBuilder();
        StringBuilder imgPin = new StringBuilder();
        StringBuilder arrayBodyInfoView = new StringBuilder();
        StringBuilder arrayRaio = new StringBuilder();
        StringBuilder arrayColorRaio = new StringBuilder();

        for(int x=0; x<lista.size(); x++){
       	 	if(lista.get(x).getLongitude()!=null && lista.get(x).getLatitude()!=null){
       	 		arraylng.append(lista.get(x).getLongitude());
             	arraylat.append(lista.get(x).getLatitude());
             	arrayTitle.append("'").append(lista.get(x).getTitle()).append("'");
             	if(lista.get(x).isMarkCustomizado()){
             		imgPin.append(getUrlMark(lista.get(x).getMapMarkerPointEnum().getImage()));
             		if(lista.get(x).isShowRaio()){
             			isShowRaio = true;
             			arrayRaio.append(lista.get(x).getRaio());
                 		arrayColorRaio.append("'").append(lista.get(x).getMapMarkerPointEnum().getColor()).append("'");
             		}
             	}
             	if(lista.get(x).isShowInfoWindow()){
             		arrayBodyInfoView.append("'").append(lista.get(x).getBodyInfoWindow()).append("'");
             		isShowInfoWindow = Boolean.TRUE;
             	}
             		
        	} 
        	if(x != (lista.size()-1)){
        		arraylat.append(",");
        		arraylng.append(",");
        		arrayTitle.append(",");
        		imgPin.append(",");
        		arrayBodyInfoView.append(",");
        		arrayRaio.append(",");
        		arrayColorRaio.append(",");
        	}
        }
    	
        js.append("var arrayPin = [").append(imgPin).append("];");
    	js.append("var arraylng = [").append(arraylng).append("];");
    	js.append("var arraylat = [").append(arraylat).append("];");
    	js.append("var arrayTitle = [").append(arrayTitle).append("];");
    	js.append("var arrayBodyInfoView = [").append(arrayBodyInfoView).append("]; ");
    	js.append("var arrayRaio = [").append(arrayRaio).append("]; ");
    	js.append("var arrayColorRaio = [").append(arrayColorRaio).append("]; ");
    	
    	js.append("addMarkers(map, arrayPin, arraylat, arraylng, arrayTitle, "+isShowRaio+", arrayRaio, arrayColorRaio, "+isShowInfoWindow+", arrayBodyInfoView);");
        
    }
    
        
    private void definirHeight() {
    	if (heightMapEnum != null) {
			
	    	if(heightMapEnum == HeightMapEnum.GRANDE_600){
	    		Component component = get("map");
				component.add(new AttributeModifier("class", new Model<String>(HeightMapEnum.GRANDE_600.getCss())));
			}
	    	else if(heightMapEnum == HeightMapEnum.MEDIO_400){
	    		Component component = get("map");
				component.add(new AttributeModifier("class", new Model<String>(HeightMapEnum.MEDIO_400.getCss())));
			}
    	}
	}
    
    private String getJavaScript(List<MapMarkerPoint> lista){
    	
    	final StringBuilder js = new StringBuilder("<script type='text/javascript'>");
    	
    	if(lista != null && lista.size() > 0){
        	js.append(" var mapOptions = { center: new google.maps.LatLng("+lista.get(0).getLatitude()+", "+lista.get(0).getLongitude()+"),"); 
//    			js.append("zoom: ").append(getZoom(lista)).append(",");
        		js.append("zoom: ").append(zoom).append(",");
    			js.append("mapTypeId: google.maps.MapTypeId.ROADMAP }; ");
        	js.append(" map = new google.maps.Map(document.getElementById(\"map-canvas\"), mapOptions); ");
        	js.append("var markers = [];"); 
        	
        	if (lista != null && !lista.isEmpty()) {
        		addMarkers(lista, js);
        	}
    	} else {
	    	js.append("var mapOptions = { center: new google.maps.LatLng(-15.8, -47.9),"); 
			js.append("zoom: "+this.zoom+",");
			js.append("mapTypeId: google.maps.MapTypeId.ROADMAP }; ");
        	js.append("map = new google.maps.Map(document.getElementById(\"map-canvas\"), mapOptions); ");
        	js.append("var markers = [];"); 
    	}
        
        js.append("</script>");
        return js.toString();
   	
   }
    
    private String getLatitude(MapMarkerPoint point){
    	return point!=null&&point.getLatitude()!= null ? point.getLatitude().toString():"-20.0";
    }
    
    private String getLongitude(MapMarkerPoint point){
    	return point!=null && point.getLongitude() != null ? point.getLongitude().toString() : "-44.4";
    }
    
    private String getTitle(MapMarkerPoint point){
    	return point.getTitle()!=null?point.getTitle():"";
    }
    
    private String getUrlMark(String urlMark){
    	ResourceReference rr = new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "img/"+urlMark);
    	return "'resources/"+rr.getScope().toString().replaceAll("class ", "")+"/"+rr.getName()+"'";
    }
    
	private String getZoom(List<MapMarkerPoint> point){
    	return !point.isEmpty() && point.size() == 1 ? String.valueOf(point.get(0).getZoom()) :"4";
    }
    
	private boolean isLatitudeLongitude(MapMarkerPoint point){
    	return point!=null && point.getLatitude()!= null && point.getLongitude()!=null;
    }
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		definirHeight();
	}
}
