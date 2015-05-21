package com.born2go.client.widgets;

import java.util.List;

import com.born2go.shared.Poster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CompanionTable extends Composite {

	private static CompanionTableUiBinder uiBinder = GWT
			.create(CompanionTableUiBinder.class);

	interface CompanionTableUiBinder extends UiBinder<Widget, CompanionTable> {
	}
	
	@UiField HTMLPanel container;
	Label viewMore;

	public CompanionTable() {
		initWidget(uiBinder.createAndBindUi(this));
		
		container.getElement().setInnerHTML("<span style='margin-left: 5px; font-weight: normal; font-family: Arial Unicode MS, Arial, sans-serif;'>Searching people...</span>");
	}
	
	public void setTrip(List<Poster> companions) {
		container.getElement().setInnerHTML("");
		if(!companions.isEmpty()) {
			if(companions.size() <= 3) {
				for(int i=0; i<companions.size(); i++) {
					String newCompanion = "<a href='/profile/"+ companions.get(i).getUserID().toString()+ "' style='margin-left: 5px; color: cornflowerblue !important;'>"+ companions.get(i).getUserName()+ ", </a>";
					container.getElement().setInnerHTML(container.getElement().getInnerHTML()+ newCompanion);
				}
			}
			else {
				for(int i=0; i<3; i++) {
					String newCompanion = "<a href='/profile/"+ companions.get(i).getUserID().toString()+ "' style='margin-left: 5px; color: cornflowerblue !important;'>"+ companions.get(i).getUserName()+ ", </a>";
					container.getElement().setInnerHTML(container.getElement().getInnerHTML()+ newCompanion);
				}
				String viewMore = "and +"+ (companions.size()-3)+ " joined";
				this.viewMore = new Label();
				this.viewMore.setText(viewMore);
				this.viewMore.getElement().setAttribute("style", "float: right; margin-left: 5px; font-style: italic; font-weight: normal; font-family: Arial Unicode MS, Arial, sans-serif;");
				container.add(this.viewMore);
			}
		}
		else {
			container.getElement().setInnerHTML("<span style='margin-left: 5px; font-weight: normal; font-family: Arial Unicode MS, Arial, sans-serif;'>Nobody joined</span>");
		}
	}
	
	void addACompanion(Poster companion) {
//		HTMLPanel acompanion = new HTMLPanel("");
//		acompanion.setStyleName("CompanionTable_Obj1");
//		Image companionPic = new Image();
//		companionPic.setUrl("https://graph.facebook.com/"+companion.getUserID()+"/picture?width=35&height=35");
//		companionPic.getElement().setAttribute("style", "border-radius: 20px;border: 1px silver solid;overflow: hidden;height: 32px;");
//		Anchor companionName = new Anchor(companion.getUserName());
//		acompanion.add(companionPic);
//		acompanion.add(companionName);
//		companionName.setStyleName("font-blackTitleLarge link");
//		companionName.getElement().setAttribute("style", "margin:10px;font-size: 15px;color:cornflowerblue;font-style: italic;font-weight: 700;font-family: museo-w01-700,serif;");
//		companionName.setHref("/profile/"+ companion.getUserID().toString());
//		//
//		container.add(acompanion);
	}

}
