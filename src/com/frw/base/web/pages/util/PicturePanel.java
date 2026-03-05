package com.frw.base.web.pages.util;

import java.util.List;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import java.time.Duration;

import com.frw.base.dominio.sisdat.Foto;

@SuppressWarnings("serial")
public class PicturePanel extends Panel {

	private NonCachingImage imagem;
	private List<Foto> listfotos;
	
	 public PicturePanel(String id, List<Foto> listFotos, Model labelModel) {
		super(id);

		this.listfotos = listFotos;
		addComponents(listfotos);
	}
	
	private void addComponents(final List<Foto> listFotos) {
		
		ListView<Foto> linksView = new ListView<Foto>("imagens", listFotos) {
			
			@Override
			protected void populateItem(final ListItem<Foto> li) {
				
				DynamicImageResource imageResource = new DynamicImageResource() {
					@Override
					protected byte[] getImageData(org.apache.wicket.request.resource.IResource.Attributes attributes) {
						return li.getModelObject() !=null ? li.getModelObject().getFoto() : new byte[0];
					}
				};
				
				Link<String> link = new Link<String>("linkImagem", new PropertyModel<String>(li.getModelObject(), "link")) {
					@Override
					public void onClick() {
						
					}
					@Override
					protected void onComponentTag(ComponentTag tag) {
						
					}
				};
				li.add(link);
				imagem = new NonCachingImage("imagem", imageResource);
				link.add(imagem);
				
			}
			
		};
		add(linksView);
		
		
		linksView.add(new AbstractAjaxTimerBehavior(java.time.Duration.ofSeconds(5)) {
			int index = 0;
			@Override
			protected void onTimer(AjaxRequestTarget ajax) {
				index ++;
				
				if(index == listFotos.size()){
					index = 0;
				}
				
				final Foto p = listFotos.get(index);
				DynamicImageResource imageResource = new DynamicImageResource() {
					@Override
					protected byte[] getImageData(org.apache.wicket.request.resource.IResource.Attributes attributes) {
						return p.getFoto();
					}
					
				};
				imagem.setImageResource(imageResource);
				ajax.add(PicturePanel.this);
			}
			
		});
		
	}
	

}
