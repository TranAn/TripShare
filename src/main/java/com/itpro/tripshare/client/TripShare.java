package com.itpro.tripshare.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.itpro.tripshare.client.widgets.CreateTrip;
import com.itpro.tripshare.client.widgets.GwtGoogleMap;
import com.itpro.tripshare.client.widgets.Menu;

public class TripShare implements EntryPoint {

	public static GwtGoogleMap tripMap = GwtGoogleMap.create();

	Menu menu = new Menu();
	CreateTrip createTrip = new CreateTrip();

	@Override
	public void onModuleLoad() {
		if (RootPanel.get("menu") != null)
			RootPanel.get("menu").add(menu);
		if (RootPanel.get("tripMap") != null)
			RootPanel.get("tripMap").add(tripMap.getMapView());
		if (RootPanel.get("createTrip") != null)
			RootPanel.get("createTrip").add(createTrip);
	}

}
