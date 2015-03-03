package com.itpro.tripshare.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsRoute;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsService.Callback;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.TravelMode;

public class GwtGoogleMap {

	private static SimplePanel container = new SimplePanel();
	private static GoogleMap theMap;
	private static DirectionsRenderer directionsRenderer;
	private static List<Marker> markers = new ArrayList<Marker>();
	
	public interface Listener {
		void getCurrentLocation(String address, LatLng position);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	protected GwtGoogleMap() {
		super();
	}

	public static GwtGoogleMap create() {
		MapOptions options = MapOptions.create();
		options.setCenter(LatLng.create(39.509, -98.434));
		options.setZoom(6);
		options.setMapTypeId(MapTypeId.ROADMAP);
		options.setDraggable(true);
		options.setMapTypeControl(true);
		options.setScaleControl(true);
		options.setScrollwheel(true);
		theMap = GoogleMap.create(container.getElement(), options);
		directionsRenderer = DirectionsRenderer.create();
		directionsRenderer.setMap(theMap);
		container.setSize("100%", "100%");
		return new GwtGoogleMap();
	}

	public SimplePanel getMapView() {
		return container;
	}

	public GoogleMap getMap() {
		return theMap;
	}

	public void setMapSize(int width, int height) {
		container.setSize(width + "px", height + "px");
	}

	public void getCurrentLocation() {
		Geolocation geoLocation = Geolocation.getIfSupported();
		if (geoLocation == null) {
			Window.alert("Your old browser is stuck in the past");
		} else {
			geoLocation.getCurrentPosition(new com.google.gwt.core.client.Callback<Position, PositionError>() {
				
				@Override
				public void onSuccess(Position result) {
					final LatLng l = LatLng.create(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
					addMaker(l);
					theMap.setCenter(l);
					theMap.setZoom(17.0);
					GeocoderRequest geoRequest = GeocoderRequest.create();
					geoRequest.setLocation(l);
					Geocoder geoCode = Geocoder.create();
					geoCode.geocode(geoRequest, new Geocoder.Callback() {
						@Override
						public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
							String address = a.get(0).getAddressComponents().get(0).getShortName();
							if(listener != null)
								listener.getCurrentLocation(address, l);
						}
					});
				}
				
				@Override
				public void onFailure(PositionError reason) {
					Window.alert("Some error occur!");
				}
			});
		}
	}

	public void addMaker(final LatLng position) {
		Marker marker = Marker.create();
		markers.add(marker);
		marker.setPosition(position);
		marker.setMap(theMap);
		marker.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				InfoWindow info = InfoWindow.create();
				HTMLPanel html = new HTMLPanel(("I'm Info Window"));
				info.setContent(html.getElement());
				info.setPosition(position);
				info.open(theMap);
			}
		});
	}

	public void findDirection(LatLng originPoint, LatLng destinationPoint) {
		DirectionsRequest directionRequest = DirectionsRequest.create();
		TravelMode mode = TravelMode.DRIVING;
		directionRequest.setTravelMode(mode);
		directionRequest.setOrigin(originPoint);
		directionRequest.setDestination(destinationPoint);
		DirectionsService directionService = DirectionsService.create();
		directionService.route(directionRequest, new Callback() {
			@Override
			public void handle(DirectionsResult a, DirectionsStatus b) {
				directionsRenderer.setDirections(a);
				DirectionsRoute route = a.getRoutes().get(0);
				saveDirection.clear();
				for (int i = 0; i < route.getOverviewPath().length(); i++) {
					Point p = new Point(route.getOverviewPath().get(i));
					saveDirection.add(p);
				}
			}
		});
		for( Marker m: markers ) {
			GoogleMap nullmap = null;
			m.setMap(nullmap);
		}
	}

	private List<Point> saveDirection = new ArrayList<Point>();

	@SuppressWarnings("unchecked")
	public void drawTheJourney() {
		Polyline theJourney = Polyline.create();
		theJourney.setMap(theMap);
		JsArray<LatLng> journey = (JsArray<LatLng>) JsArray.createArray();
		for (Point p : saveDirection) {
			journey.push(p.toLatLng());
		}
		Window.alert(journey.toString());
		theJourney.setPath(journey);
	}

}
