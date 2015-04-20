package com.born2go.client;

import java.util.Date;

import org.timepedia.exporter.client.ExporterUtil;

import com.born2go.client.rpc.DataService;
import com.born2go.client.rpc.DataServiceAsync;
import com.born2go.client.widgets.CreateTrip;
import com.born2go.client.widgets.LoadingBox;
import com.born2go.client.widgets.PathView;
import com.born2go.client.widgets.TravelMap;
import com.born2go.client.widgets.TravelMap.Listener;
import com.born2go.client.widgets.ViewFullPath;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.LatLng;

public class TripShare implements EntryPoint {
	
	public static final DataServiceAsync dataService = GWT.create(DataService.class);

	public static TravelMap tripMap = TravelMap.create();
	public static final LoadingBox loadBox = new LoadingBox();
	
	public static String access_token;
	public static String user_id;
	
	static final CreateTrip createTrip = new CreateTrip();
	static final PathView pathView = new PathView();
	static final ViewFullPath viewFullPath = new ViewFullPath();
	
	public static String ERROR_MESSAGE = "!: Đã có lỗi xảy ra, vui lòng tải lại trang.";

	@Override
	public void onModuleLoad() {
		//---
		exportGwtClass();
		onLoadImpl();
		//---
		if (RootPanel.get("tripMap") != null) {
			RootPanel.get("tripMap").add(tripMap.getMapView());
		}
		
		if (RootPanel.get("createTrip") != null) {
			RootPanel.get("createTrip").add(createTrip);
		}
		
		if (RootPanel.get("tripcontent") != null) {
			String tripId = Window.Location.getPath().replaceAll("/journey/", "");
			if(tripId.length() != 0) {
				pathView.getTrip(Long.valueOf(tripId));
				RootPanel.get("tripcontent").add(pathView);
			}
		}
		
		if (RootPanel.get("gallery") != null) {
			String pathId = Window.Location.getPath().replaceAll("/destination/", "");
			 if(pathId.length() != 0) {
				 viewFullPath.getPath(Long.valueOf(pathId));
				 RootPanel.get("gallery").add(viewFullPath);
			 }
		}
		
		if (RootPanel.get("betaTrip") != null) {
			BetaTrip trip = new BetaTrip();
			RootPanel.get("betaTrip").add(trip);
		}
		//---
		handlerTripMap();
	}
	
	private native void onLoadImpl() /*-{
    	if ($wnd.jscOnLoad && typeof $wnd.jscOnLoad == 'function') $wnd.jscOnLoad();
  	}-*/;
	
	void handlerTripMap() {
		tripMap.setListener(new Listener() {
			@Override
			public void getDirectionResult(DirectionsResult directionResult) {
				if (RootPanel.get("createTrip") != null) 
					createTrip.setDirectionResult(directionResult);
				if (RootPanel.get("tripcontent") != null) 
					pathView.setDirectionResult(directionResult);
			}
			
			@Override
			public void getCurrentLocation(String address, LatLng position) {
				if (RootPanel.get("createTrip") != null) 
					createTrip.setYourLocation(address, position);
			}
		});
	}

	void exportGwtClass() {
		ExporterUtil.exportAll();
	}
	
	public static void getAccessToken(String accessToken, String userId) {
		access_token = accessToken;
		user_id = userId;
		if(access_token.isEmpty()) {
			
		}
		else {
			pathView.checkPermission();
			viewFullPath.checkPermission();
		}
	}
	
	public static final String dateFormat(Date date) {
		String dateString = "yyyy MMM d hh:mm:ss";
		DateTimeFormat formatDate = DateTimeFormat.getFormat(dateString);
		return formatDate.format(date);
	}

}
