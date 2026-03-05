package com.frw.base.web.pages.map;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.frw.base.util.enumeration.MapMarkerPointEnum;
import com.frw.base.web.pages.BasePage;
import com.frw.base.web.util.MapMarkerPoint;

/**
 * @author Miller Leonardo
 */
public class MapSinglePointPanel extends Panel { 
    
	private static final long serialVersionUID = 515759983941345196L;
	
	private String defaultTittlePointsMap = "Local do Ponto";
	private MapMarkerPoint point;

	private int zoom = 17;

	public MapSinglePointPanel(String id, MapMarkerPoint point) {
        super(id);
        setOutputMarkupId(true);
        this.point = point;
    }
	
	/**
	 * Define o icone correspondente para o ponto do mapa (conforme o MapMarkerPointEnum).
	 * @param point
	 * @return
	 */
	private String getIconPoint(MapMarkerPoint point) {
		ResourceReference rr;
		
		if(point.isMarkCustomizado() && point.getMapMarkerPointEnum() !=null){
			rr = new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "img/"+point.getMapMarkerPointEnum().getImage());
		}else {
			rr = new org.apache.wicket.request.resource.PackageResourceReference(BasePage.class, "img/"+MapMarkerPointEnum.RED.getImage());
		}
		return "resources/"+rr.getScope().toString().replaceAll("class ", "")+"/"+rr.getName();
	}

	@Override
    protected void onAfterRender() {
    	super.onAfterRender();
    	AjaxRequestTarget.get().appendJavaScript(
    			"loadMap("+point.getLatitude()+","+point.getLongitude()+",'"+point.getBodyInfoWindow()+"','"+ getIconPoint(point) +"');");
    }
	
}
