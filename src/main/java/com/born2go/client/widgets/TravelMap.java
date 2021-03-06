package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.shared.embedclass.Locate;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsService.Callback;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.DirectionsWaypoint;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import com.google.maps.gwt.client.TravelMode;

public class TravelMap {

	private static SimplePanel container = new SimplePanel();
	private static GoogleMap theMap;
	private static DirectionsRenderer directionsRenderer;
	private static List<Marker> markers = new ArrayList<Marker>();
	private List<Marker> markers2 = new ArrayList<Marker>();
	private static Polyline polyline;
	
	public interface Listener {
		void getLocation(String address, LatLng position);
		void getDirectionResult(DirectionsResult directionResult);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	protected TravelMap() {
		super();
	}

	public static TravelMap create() {
		MapOptions options = MapOptions.create();
		LatLng officeP = LatLng.create(21.039971, 105.773398);
		options.setCenter(officeP);
		options.setZoom(16);
		options.setMapTypeId(MapTypeId.ROADMAP);
		options.setDraggable(true);
		options.setMapTypeControl(true);
		options.setScaleControl(true);
		options.setScrollwheel(false);
		theMap = GoogleMap.create(container.getElement(), options);
		
		directionsRenderer = DirectionsRenderer.create();
		directionsRenderer.setMap(theMap);
		
		PolylineOptions polyLineOption = PolylineOptions.create();
		polyLineOption.setStrokeColor("blue");
		polyLineOption.setStrokeOpacity(0.5);
		polyLineOption.setStrokeWeight(8.0);
		polyline = Polyline.create(polyLineOption);
		polyline.setMap(theMap);
		
		container.setSize("100%", "100%");
		addMarker(officeP, "Trip Share Office", true, true, true);
		return new TravelMap();
	}
	
	public LatLng getLatLng(Locate loc) {
		LatLng l = LatLng.create(loc.getLatitude(), loc.getLongitude());
		return l;
	}

	public SimplePanel getMapView() {
		return container;
	}

	public GoogleMap getMap() {
		return theMap;
	}

	public void setMapSize(String width, String height) {
		container.setSize(width, height);
	}
	
	public void clearMap() {
		GoogleMap nullmap = null;
		for( Marker m: markers )
			m.setMap(nullmap);
		markers.clear();
		directionsRenderer.setMap(nullmap);
		polyline.setMap(nullmap);
	}

	public void getCurrentLocation() {
		clearMap();
		Geolocation geoLocation = Geolocation.getIfSupported();
		if (geoLocation == null) {
			Window.alert("!: Your old browser is stuck in the past");
		} else {
			geoLocation.getCurrentPosition(new com.google.gwt.core.client.Callback<Position, PositionError>() {
				
				@Override
				public void onSuccess(Position result) {
					final LatLng l = LatLng.create(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
					theMap.setCenter(l);
					theMap.setZoom(16.0);
					GeocoderRequest geoRequest = GeocoderRequest.create();
					geoRequest.setLocation(l);
					Geocoder geoCode = Geocoder.create();
					geoCode.geocode(geoRequest, new Geocoder.Callback() {
						@Override
						public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
							String address = a.get(0).getFormattedAddress();
							addMarker(l, address, true, true, true);
							if(listener != null)
								listener.getLocation(address, l);
						}
					});
				}
				
				@Override
				public void onFailure(PositionError reason) {
					Window.alert("!: Map API request error.");
				}
			});
		}
	}

	public static void addMarker(final LatLng position, final String info, boolean isOriginPoint, boolean isOpenInfoWindow, boolean isAnimation) {
		MarkerOptions markerOption = MarkerOptions.create();
		if(isAnimation)
			markerOption.setAnimation(Animation.DROP);
		if(!isOriginPoint)
			markerOption.setIcon("/resources/green-spotlight.png");
		else
			markerOption.setIcon("/resources/red-spotlight.png");
		final Marker marker = Marker.create(markerOption);
		markers.add(marker);
		marker.setPosition(position);
		marker.setMap(theMap);
		//set Info window
		final InfoWindow infowindow = InfoWindow.create();
		HTMLPanel html = new HTMLPanel(info);
		infowindow.setContent(html.getElement());
		if(isOpenInfoWindow)
			infowindow.open(theMap, marker);
		//marker onclick listener
		marker.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				infowindow.open(theMap, marker);
			}
		});
	}
	
	public void addMarker2(final LatLng position, final String info, boolean isOpenInfoWindow) {
		GoogleMap nullmap = null;
		for( Marker m: markers2 )
			m.setMap(nullmap);
		markers2.clear();
		MarkerOptions markerOption = MarkerOptions.create();
		markerOption.setAnimation(Animation.BOUNCE);
		markerOption.setIcon("/resources/red-spotlight.png");
		final Marker marker = Marker.create(markerOption);
		markers2.add(marker);
		marker.setPosition(position);
		marker.setMap(theMap);
		//set Info window
		if(isOpenInfoWindow) {
			final InfoWindow infowindow = InfoWindow.create();
			HTMLPanel html = new HTMLPanel(info);
			infowindow.setContent(html.getElement());
			infowindow.open(theMap, marker);
			marker.addClickListener(new ClickHandler() {
				@Override
				public void handle(MouseEvent event) {
					infowindow.open(theMap, marker);
				}
			});
		}
	}

	public void findDirection(LatLng originPoint, LatLng destinationPoint, JsArray<DirectionsWaypoint> waypoints) {
		DirectionsRequest directionRequest = DirectionsRequest.create();
		TravelMode mode = TravelMode.DRIVING;
		directionRequest.setTravelMode(mode);
		directionRequest.setOrigin(originPoint);
		directionRequest.setDestination(destinationPoint);
		directionRequest.setWaypoints(waypoints);
		DirectionsService directionService = DirectionsService.create();
		directionService.route(directionRequest, new Callback() {
			@Override
			public void handle(DirectionsResult a, DirectionsStatus b) {
				clearMap();
				directionsRenderer.setMap(theMap);
				directionsRenderer.setDirections(a);
				if(listener != null)
					listener.getDirectionResult(a);
			}
		});
	}

	int index;
	
	@SuppressWarnings("unchecked")
	public void drawTheJourney(final List<com.born2go.shared.embedclass.Journey.Point> directions, final List<Locate> locates, boolean animationDraw) {
		clearMap();
		final JsArray<LatLng> journey = (JsArray<LatLng>) JsArray.createArray();
		polyline.setPath(journey);	
		//zoom the map
		final LatLngBounds bounds = LatLngBounds.create();
		for (int i = 0; i < locates.size(); i++) {
		    bounds.extend(getLatLng(locates.get(i)));
		}
		bounds.getCenter();
		theMap.fitBounds(bounds);
		//add marker and draw journey
		if(animationDraw) {
			index = 0;
			Timer timer = new Timer() {
			     @Override
			     public void run() {		 
			    	 if(index == locates.size()) {
			    		polyline.setMap(theMap);
			    		index = 1;		    		
			    		Timer timer = new Timer() {
			    			 @Override
			    		     public void run() {
			    				 if(index >= directions.size()) {
			    					 cancel();
			    				 }
			    				 else {
				    				 journey.setLength(0);
				    				 for(int i = 0; i <= index; i++) {
				    					 journey.push(directions.get(i).toLatLng());
				    				 }
				    				 polyline.setPath(journey);			    						
				    				 index++;
			    				 }		    				 
			    			 }
			    		}; timer.scheduleRepeating(5);
			    		theMap.fitBounds(bounds);
//			    		theMap.setZoom(theMap.getZoom()+1);
			 			cancel();
			    	 }
			    	 else {
			    		 if(index == 0)
			    			 addMarker(getLatLng(locates.get(index)), locates.get(index).getAddressName(), true, false, true);
			    		 else {
			    			 if(index == locates.size() - 1)
			    				 addMarker(getLatLng(locates.get(index)), locates.get(index).getAddressName(), false, false, true);
			    			 else
			    				 addMarker(getLatLng(locates.get(index)), locates.get(index).getAddressName(), false, false, true);
			    		 }
			    		 index++;
			    	 }
			     }
			};
			timer.scheduleRepeating(670);
		}
		else {
			for(int i=0; i<locates.size(); i++) {
				if(i == 0)
					addMarker(getLatLng(locates.get(i)), locates.get(i).getAddressName(), true, false, false);
				else 
					addMarker(getLatLng(locates.get(i)), locates.get(i).getAddressName(), false, false, false);
			}
			for(com.born2go.shared.embedclass.Journey.Point p : directions)
				journey.push(p.toLatLng());
			polyline.setMap(theMap);
			polyline.setPath(journey);
//			theMap.setZoom(theMap.getZoom()+1);
		}
	}
	
	public void clickToAddMarker() {
		theMap.addClickListener(new com.google.maps.gwt.client.GoogleMap.ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				final LatLng position = event.getLatLng();
				
				GeocoderRequest geoRequest = GeocoderRequest.create();
				geoRequest.setLocation(position);
				Geocoder geoCode = Geocoder.create();
				geoCode.geocode(geoRequest, new Geocoder.Callback() {
					@Override
					public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
						String address = a.get(0).getFormattedAddress();
						addMarker2(position, address, false);
						if(listener != null)
							listener.getLocation(address, position);
					}
				});
			}
		});
	}
	
	public void removeClickToAddMarker() {
		theMap.clearClickListeners();
	}
	
	public void removeLocationMarker() {
		GoogleMap nullmap = null;
		for( Marker m: markers2 )
			m.setMap(nullmap);
		markers2.clear();
		// Set the map center of polyline
		final LatLngBounds bounds = LatLngBounds.create();
		for (int i = 0; i < markers.size(); i++) {
		    bounds.extend(markers.get(i).getPosition());
		}
		bounds.getCenter();
		theMap.fitBounds(bounds);
	}

}
