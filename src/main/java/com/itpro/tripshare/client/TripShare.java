package com.itpro.tripshare.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.LatLng;
import com.itpro.tripshare.client.rpc.DataService;
import com.itpro.tripshare.client.rpc.DataServiceAsync;
import com.itpro.tripshare.client.widgets.CreateTrip;
import com.itpro.tripshare.client.widgets.TravelMap;
import com.itpro.tripshare.client.widgets.TravelMap.Listener;

public class TripShare implements EntryPoint {

	public static TravelMap tripMap = TravelMap.create();
	public static final DataServiceAsync dataService = GWT.create(DataService.class);
	public static final AppStorage appStorage = new AppStorage();

	static final CreateTrip createTrip = new CreateTrip();

	@Override
	public void onModuleLoad() {
		if (RootPanel.get("tripMap") != null)
			RootPanel.get("tripMap").add(tripMap.getMapView());
		if (RootPanel.get("createTrip") != null)
			RootPanel.get("createTrip").add(createTrip);
		
		handlerTripMap();
		
		exportGwtClass();
	}
	
	void handlerTripMap() {
		tripMap.setListener(new Listener() {
			
			@Override
			public void getDirectionResult(DirectionsResult directionResult) {
				createTrip.setDirectionResult(directionResult);
			}
			
			@Override
			public void getCurrentLocation(String address, LatLng position) {
				createTrip.setYourLocation(address, position);
			}
		});
	}

	void exportGwtClass() {
		ExporterUtil.exportAll();
	}

}
