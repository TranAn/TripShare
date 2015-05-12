package com.born2go.client.widgets;

import java.util.List;

import com.born2go.shared.Poster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CompanionTable extends Composite {

	private static CompanionTableUiBinder uiBinder = GWT
			.create(CompanionTableUiBinder.class);

	interface CompanionTableUiBinder extends UiBinder<Widget, CompanionTable> {
	}
	
	@UiField HTMLPanel container;

	public CompanionTable() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setTrip(List<Poster> companions) {
		container.clear();
		if(!companions.isEmpty())
			for(Poster p: companions) {
				addACompanion(p);
			}
	}
	
	void addACompanion(Poster companion) {
		HTMLPanel acompanion = new HTMLPanel("");
		acompanion.setStyleName("CompanionTable_Obj1");
		Image companionPic = new Image();
		companionPic.setUrl("https://graph.facebook.com/"+companion.getUserID()+"/picture?width=35&height=35");
		companionPic.getElement().setAttribute("style", "border-radius: 20px;border: 1px silver solid;overflow: hidden;height: 32px;");
		Anchor companionName = new Anchor(companion.getUserName());
		acompanion.add(companionPic);
		acompanion.add(companionName);
		companionName.setStyleName("font-blackTitleLarge link");
		companionName.getElement().setAttribute("style", "margin:10px;font-size: 15px;color:cornflowerblue;font-style: italic;font-weight: 700;font-family: museo-w01-700,serif;");
		companionName.setHref("/profile/"+ companion.getUserID().toString());
		//
		container.add(acompanion);
	}

}
