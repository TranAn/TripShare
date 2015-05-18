package com.born2go.client;

import java.util.Date;

import org.timepedia.exporter.client.ExporterUtil;

import com.born2go.client.rpc.DataService;
import com.born2go.client.rpc.DataServiceAsync;
import com.born2go.client.widgets.BetaTripSample;
import com.born2go.client.widgets.Create_CreateTrip;
import com.born2go.client.widgets.Destination_EditToolBar;
import com.born2go.client.widgets.Journey_PathView;
import com.born2go.client.widgets.LoadingBox;
import com.born2go.client.widgets.Profile_ProfileView;
import com.born2go.client.widgets.TravelMap;
import com.born2go.client.widgets.TravelMap.Listener;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.LatLng;

public class TripShare implements EntryPoint {
	
	public static final DataServiceAsync dataService = GWT.create(DataService.class);

	public static TravelMap tripMap = TravelMap.create();
	public static final LoadingBox loadBox = new LoadingBox();
	
	public static String access_token;
	public static String user_id;
	
	static final Create_CreateTrip createTrip = new Create_CreateTrip();
	static final Journey_PathView pathView = new Journey_PathView();
	static final Destination_EditToolBar viewFullPath = new Destination_EditToolBar();
	static final Profile_ProfileView profileView = new Profile_ProfileView();
	
	public static String ERROR_MESSAGE = "!: Đã có lỗi xảy ra, vui lòng tải lại trang.";

	@Override
	public void onModuleLoad() {
		//--- Separate pages content ---
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
		
		if (RootPanel.get("profileContent") != null) {
			RootPanel.get("profileContent").add(profileView);
			String userID = Window.Location.getPath().replaceAll("/profile/","");
			if (userID.length() != 0) {
				profileView.showProfileView(userID);
			}
		}
		
		if (RootPanel.get("mtripmap") != null) {
			RootPanel.get("mtripmap").add(tripMap.getMapView());
			String idJourney = Window.Location.getPath().replaceAll("/journey/","");
			if (idJourney.length() != 0) {
				Long idTrip = Long.parseLong(idJourney);
				dataService.findTrip(idTrip, new AsyncCallback<Trip>() {
					@Override
					public void onSuccess(Trip result) {
						tripMap.drawTheJourney(result.getJourney().getDirections(),
								result.getJourney().getLocates());
					}
					@Override
					public void onFailure(Throwable caught) {}
				});
			}
		}
		
		if (RootPanel.get("betaTrip") != null) {
			BetaTripSample trip = new BetaTripSample();
			RootPanel.get("betaTrip").add(trip);
		}
		
		//--- User, login handler ---
		exportGwtClass();
		checkUserStatus();
		//--- Map handler ---
		handlerTripMap();
	}
	
	void exportGwtClass() {
		ExporterUtil.exportAll();
	}

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

	private native void checkUserStatus() /*-{
	    $wnd.FB.init({
	      appId      : '386540048195283',
	      cookie	 : true,
	      xfbml      : true,
	      version    : 'v2.3'
	    });
		
		(function(d, s, id){
		    var js, fjs = d.getElementsByTagName(s)[0];
		    if (d.getElementById(id)) {return;}
		    js = d.createElement(s); js.id = id;
		    js.src = "//connect.facebook.net/en_US/sdk.js";
		    fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
		
		$wnd.FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				var userId = response.authResponse.userID;
			    var accessToken = response.authResponse.accessToken;
			    @com.born2go.client.TripShare::getAccessToken(Ljava/lang/String;Ljava/lang/String;)(accessToken,userId);
				$wnd.FB.api('/me', function(response) {
					var menuProfile = $wnd.document.getElementById('menubutton');
					menuProfile.innerHTML = "Profile";
					var profileHref =  "/profile/"+ userId;
					menuProfile.setAttribute('href', profileHref);
				});
			} else if (response.status === 'not_authorized') {
				 @com.born2go.client.TripShare::getAccessToken(Ljava/lang/String;Ljava/lang/String;)('','');
			} else {
				 @com.born2go.client.TripShare::getAccessToken(Ljava/lang/String;Ljava/lang/String;)('','');
			}
		}, true);
	}-*/;

	public static void getAccessToken(String accessToken, String userId) {
		access_token = accessToken;
		user_id = userId;
		if(access_token.isEmpty()) {}
		else {
			pathView.checkPermission();
			viewFullPath.checkPermission();
			try {
				dataService.getLongLiveToken(accessToken, new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if(result != null && !result.isEmpty())
							access_token = result;
					}
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static final String dateFormat(Date date) {
		String dateString = "yyyy MMM d hh:mm:ss";
		DateTimeFormat formatDate = DateTimeFormat.getFormat(dateString);
		return formatDate.format(date);
	}

}
