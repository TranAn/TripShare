package com.born2go.client.widgets;

import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Journey_Catalog extends Composite {

	private static Journey_CatalogUiBinder uiBinder = GWT
			.create(Journey_CatalogUiBinder.class);

	interface Journey_CatalogUiBinder extends UiBinder<Widget, Journey_Catalog> {
	}
	
	@UiField HTMLPanel catalog;
	@UiField HTMLPanel htmlContent;
	@UiField Label totalPost;
	
	public interface Listener {
		void onClose();
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public Journey_Catalog() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setCatalog(String title, List<Path> listContents) {
		htmlContent.clear();
		HTMLPanel titleHtml = new HTMLPanel("<i class='fa fa-list-ul fa-lg'></i>");
		titleHtml.setStyleName("Catalog_Obj6");
		Label titleLb = new Label("");
		titleLb.setText(title);
		titleLb.setStyleName("Catalog_Obj7");
		titleHtml.add(titleLb);
		htmlContent.add(titleHtml);
		for(Path p: listContents) {
			HTML content = new HTML();
			content.setStyleName("Catalog_Obj4");
			String span = "<img src='/resources/1427111517_palm_tree.png' style='height: 22px; width: 22px; vertical-align: bottom; margin-right: 10px; opacity: 0.6;'>";
			String span1 = "<span class='Catalog_Obj5'><a style='color: black;' href='#index_"+ p.getId()+ "' id='#index_"+ p.getId()+ "'>"+ p.getTitle()+ "</a></span>";
			String span2 = "<span style='color: silver;'> - by <a href='/profile/"+ p.getPoster().getUserID()+ "' style='font-style: italic;'>"+ p.getPoster().getUserName()+ "</a></span>";
			String span3 = "<span style='margin-left: 20px; color: silver;'> at "+ TripShare.dateFormat(p.getCreateDate())+ "</span>";
			content.setHTML(span+ span1+ span2+ span3);
			htmlContent.add(content);
			//add evemt
			Event.setEventListener(DOM.getElementById("#index_"+ p.getId()), new EventListener() {
	            @Override
	            public void onBrowserEvent(Event event) {
	                String string = event.getType();
	                if(string.equalsIgnoreCase("click")) {
	                	catalog.setStyleName("Catalog_Obj1");
	                	if(listener != null)
	                		listener.onClose();
	                }
	            }
	        });
			Event.sinkEvents(DOM.getElementById("#index_"+ p.getId()), Event.ONCLICK);
		}
		if(listContents.isEmpty()) {
			HTML content = new HTML();
			content.setStyleName("Catalog_Obj4");
			String span = "<span style='margin-left: 20px; color: silver;'>(Empty)</span>";
			content.setHTML(span);
			htmlContent.add(content);
		}
		Image img = new Image();
		img.setUrl("/resources/food-diary-resize.jpg");
		img.getElement().setAttribute("style", "position: absolute; opacity: 0.4; top: 20px; right: 10px; width: 260px;");
		htmlContent.add(img);
		totalPost.setText("Total - "+ listContents.size()+ " Posts");
	}

}
