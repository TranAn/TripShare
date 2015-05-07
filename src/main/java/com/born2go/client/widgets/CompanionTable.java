package com.born2go.client.widgets;

import com.born2go.shared.Poster;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
	
	public void setTrip(Trip trip) {
		if(!trip.getCompanion().isEmpty())
			for(Poster p: trip.getCompanion()) {
				addACompanion(p);
			}
	}
	
	void addACompanion(Poster companion) {
		HTMLPanel acompanion = new HTMLPanel("");
		acompanion.setStyleName("CompanionTable_Obj1");
		Image companionPic = new Image();
		companionPic.setUrl("https://graph.facebook.com/"+companion.getUserID()+"/picture?width=40&height=40");
		Label companionName = new Label(companion.getUserName());
		acompanion.add(companionPic);
		acompanion.add(companionName);
		companionName.getElement().setAttribute("style", "margin:10px;");
		//
		container.add(acompanion);
	}

}
