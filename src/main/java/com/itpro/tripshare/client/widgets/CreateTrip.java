package com.itpro.tripshare.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Anchor;
import com.itpro.tripshare.client.TripShare;

public class CreateTrip extends Composite {

	private static CreateTripUiBinder uiBinder = GWT
			.create(CreateTripUiBinder.class);
	
	@UiField HTMLPanel mapContainer;
	@UiField Button btnCreateTrip;
	@UiField Anchor btnAddPart;

	interface CreateTripUiBinder extends UiBinder<Widget, CreateTrip> {
	}

	public CreateTrip() {
		initWidget(uiBinder.createAndBindUi(this));
		mapContainer.add(TripShare.tripMap.getMapView());
	}

}
