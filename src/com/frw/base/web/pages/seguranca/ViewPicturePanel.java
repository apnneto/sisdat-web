package com.frw.base.web.pages.seguranca;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.frw.base.dominio.sisdat.Foto;
import com.frw.base.web.pages.util.PicturePanel;

/**
 * 
 * @author Marcos Lisboa
 * 
 */
@SuppressWarnings("serial")
public class ViewPicturePanel extends Panel {

	public ViewPicturePanel(String id, Foto foto) {
		super(id);
		WebMarkupContainer containerPicture  = new WebMarkupContainer("containerPicture");
		containerPicture.setOutputMarkupId(true);
		add(containerPicture);
		List<Foto> listFotos = new ArrayList<Foto>();
		listFotos.add(foto);
		PicturePanel picturePanel = new PicturePanel("uploadPanel", listFotos, new Model("Banner"));
  		  
		picturePanel.setOutputMarkupId(true);
		containerPicture.add(picturePanel);
	
	}

	public ViewPicturePanel(String id, List<Foto> listFotos) {
		super(id);
		WebMarkupContainer containerPicture  = new WebMarkupContainer("containerPicture");
		containerPicture.setOutputMarkupId(true);
		add(containerPicture);
		
		PicturePanel picturePanel = new PicturePanel("uploadPanel", listFotos, new Model("Banner"));
  		  
		picturePanel.setOutputMarkupId(true);
		containerPicture.add(picturePanel);
	}
	
	
	
}
