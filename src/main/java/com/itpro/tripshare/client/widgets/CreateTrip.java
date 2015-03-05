package com.itpro.tripshare.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.DirectionsWaypoint;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
import com.google.maps.gwt.client.places.PlaceResult;
import com.itpro.tripshare.client.TripShare;

public class CreateTrip extends Composite {

	private static CreateTripUiBinder uiBinder = GWT
			.create(CreateTripUiBinder.class);
	
	@UiField HTMLPanel mapContainer;
	@UiField Button btnCreateTrip;
	@UiField Anchor btnAddPart;
	@UiField HTMLPanel htmlDestinationBox;
	@UiField TextBox txbOrigin;
	@UiField TextBox txbDestination;

	interface CreateTripUiBinder extends UiBinder<Widget, CreateTrip> {
	}
	
	LatLng originPoint;
	LatLng destinationPoint;
	List<LatLng> listWayPoint = new ArrayList<LatLng>();
	
	@SuppressWarnings("unchecked")
	JsArray<DirectionsWaypoint> waypoints = (JsArray<DirectionsWaypoint>) JsArray.createArray();
	
	
	void findTheJourney() {
		waypoints.setLength(0);
		for(LatLng latlng: listWayPoint) {
			DirectionsWaypoint waypoint = DirectionsWaypoint.create();
			waypoint.setLocation(latlng);
			waypoints.push(waypoint);
		}
		if(waypoints.length() != 0)
			TripShare.tripMap.findDirection(originPoint, destinationPoint, waypoints);
		else
			TripShare.tripMap.findDirection(originPoint, destinationPoint, null);
	}

	public CreateTrip() {
		initWidget(uiBinder.createAndBindUi(this));
		mapContainer.add(TripShare.tripMap.getMapView());
		
		final Autocomplete autocomplete = Autocomplete
				.create((InputElement) (Element) txbOrigin.getElement());
		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete.getPlace();
				originPoint = place.getGeometry().getLocation();
				if(destinationPoint == null) {
					TripShare.tripMap.addMaker(place.getGeometry().getLocation());
					TripShare.tripMap.getMap().setCenter(place.getGeometry().getLocation());
					TripShare.tripMap.getMap().setZoom(17.0);
				}
				if(destinationPoint != null) {
					findTheJourney();
				}
			}
		});
		
		final Autocomplete autocomplete2 = Autocomplete
				.create((InputElement) (Element) txbDestination.getElement());
		autocomplete2.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				if(originPoint != null) {
					PlaceResult place = autocomplete2.getPlace();
					LatLng destinationP = place.getGeometry().getLocation();
					if(listWayPoint.isEmpty())
						destinationPoint = destinationP;
					else {
						listWayPoint.remove(0);
						listWayPoint.add(0, destinationP);
					}
					findTheJourney();
				}
			}
		});
	}
	
	List<TextBox> destinationBoxMap = new ArrayList<TextBox>();
	
	public TextBox addDestination() {
		final Label lb = new Label("Điểm đến:");
		final TextBox txb = new TextBox();
		final Button btn1 = new Button("Chọn địa danh");
		final Button btn = new Button("Xóa");
		htmlDestinationBox.add(lb);
		htmlDestinationBox.add(txb);
		htmlDestinationBox.add(btn1);
		htmlDestinationBox.add(btn);
		
		final Autocomplete autocomplete3 = Autocomplete
				.create((InputElement) (Element) txb.getElement());
		autocomplete3.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete3.getPlace();
				LatLng destinationP = place.getGeometry().getLocation();
				if(listWayPoint.isEmpty()) {
					listWayPoint.add(0, destinationPoint);
					destinationPoint = destinationP;
				}
				else {
					int index = destinationBoxMap.indexOf(txb);
					if(index == destinationBoxMap.size() - 1){
						if(listWayPoint.size() == index)
							listWayPoint.add(index, destinationPoint);
						destinationPoint = destinationP;
					}
					else {
						listWayPoint.remove(index + 1);
						listWayPoint.add(index + 1, destinationP);
					}
				}
				findTheJourney();
			}
		});
		
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				htmlDestinationBox.remove(lb);
				htmlDestinationBox.remove(txb);
				htmlDestinationBox.remove(btn1);
				htmlDestinationBox.remove(btn);
				int index = destinationBoxMap.indexOf(txb);
				if(index == destinationBoxMap.size() - 1) {
					if(listWayPoint.size() > index) {
						destinationPoint = listWayPoint.get(index);
						listWayPoint.remove(index);
					}
				}
				else {
					if(listWayPoint.size() > index + 1)
						listWayPoint.remove(index + 1);
				}
				destinationBoxMap.remove(index);
				findTheJourney();
			}
		});
		
		return txb;
	}

	@UiHandler("btnAddPart")
	void onBtnAddPartClick(ClickEvent event) {
		if(destinationBoxMap.isEmpty()) {
			if(destinationPoint != null)
				destinationBoxMap.add(addDestination());
		}
		else {
			if(destinationBoxMap.get(destinationBoxMap.size()-1).getText().length() != 0)
				destinationBoxMap.add(addDestination());
		}
	}
}
