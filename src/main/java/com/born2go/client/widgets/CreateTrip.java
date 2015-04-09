package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Journey;
import com.born2go.shared.Journey.Point;
import com.born2go.shared.Locate;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsRoute;
import com.google.maps.gwt.client.DirectionsWaypoint;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
import com.google.maps.gwt.client.places.PlaceResult;

public class CreateTrip extends Composite {

	private static CreateTripUiBinder uiBinder = GWT
			.create(CreateTripUiBinder.class);
	
	@UiField HTMLPanel mapContainer;
	@UiField Anchor btnCreateTrip;
	@UiField Anchor btnAddPart;
	@UiField HTMLPanel htmlDestinationBox;
	@UiField TextBox txbOrigin;
	@UiField TextBox txbDestination;
	@UiField TextBox txbName;
	@UiField StretchyTextArea txbDescription;
	@UiField Anchor btnFindYourLocation;
	@UiField DateBox txbDeparture;
	@UiField Label lbName;
	@UiField Label lbOrigin;
	@UiField Label lbDestination;

	interface CreateTripUiBinder extends UiBinder<Widget, CreateTrip> {
	}
	
	Locate originPoint;
	Locate destinationPoint;
	List<Locate> listWayPoint = new ArrayList<Locate>();
	
	@SuppressWarnings("unchecked")
	JsArray<DirectionsWaypoint> waypoints = (JsArray<DirectionsWaypoint>) JsArray.createArray();
	DirectionsRoute directionRouter;
	
	void findTheJourney() {
		waypoints.setLength(0);
		for(Locate locate: listWayPoint) {
			DirectionsWaypoint waypoint = DirectionsWaypoint.create();
			waypoint.setLocation(locate.getLatLng());
			waypoints.push(waypoint);
		}
		if(waypoints.length() != 0)
			TripShare.tripMap.findDirection(originPoint.getLatLng(), destinationPoint.getLatLng(), waypoints);
		else
			TripShare.tripMap.findDirection(originPoint.getLatLng(), destinationPoint.getLatLng(), null);
	}

	public CreateTrip() {
		initWidget(uiBinder.createAndBindUi(this));
		mapContainer.add(TripShare.tripMap.getMapView());
		
		txbName.getElement().setPropertyString("placeholder", "Tên chuyến đi của bạn");
		txbDescription.getElement().setPropertyString("placeholder", "Mô tả chuyến đi của bạn!");
		txbDeparture.setValue(new Date());
		
		final Autocomplete autocomplete = Autocomplete
				.create((InputElement) (Element) txbOrigin.getElement());
		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete.getPlace();
				String address = txbOrigin.getText();
				originPoint = new Locate(address, place.getGeometry().getLocation());
				if(destinationPoint == null) {
					TripShare.tripMap.addMarker(place.getGeometry().getLocation(), address, true, true);
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
					String address = txbDestination.getText();
					if(listWayPoint.isEmpty())
						destinationPoint = new Locate(address, destinationP);
					else {
						listWayPoint.remove(0);
						listWayPoint.add(0, new Locate(address, destinationP));
					}
					findTheJourney();
				}
			}
		});
	}
	
	public void setDirectionResult(DirectionsResult directionResult) {
		directionRouter = directionResult.getRoutes().get(0);
	}
	
	public void setYourLocation(String address, LatLng position) {
		txbOrigin.setText(address);
		originPoint = new Locate(address, position);
		if(destinationPoint != null) {
			findTheJourney();
		}
	}
	
	List<TextBox> destinationBoxMap = new ArrayList<TextBox>();
	
	public TextBox addDestination() {
		final Label lb = new Label("Điểm đến:");
		final TextBox txb = new TextBox();
		final Anchor btn1 = new Anchor();
		final Anchor btn = new Anchor();
		
		lb.setStyleName("font-blackTitleNormal");
		txb.setStyleName("gwt-TextBox CreateTrip-Obj7");
		btn1.setStyleName("greenbutton CreateTrip-Obj13");
		btn.setStyleName("greenbutton CreateTrip-Obj12");
		btn1.getElement().setInnerHTML("<i class='fa fa-picture-o fa-lg'></i>");
		btn.getElement().setInnerHTML("<i class='fa fa-times fa-lg'></i>");
		
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
				String address = txb.getText();
				if(listWayPoint.isEmpty()) {
					listWayPoint.add(0, destinationPoint);
					destinationPoint = new Locate(address, destinationP);
				}
				else {
					int index = destinationBoxMap.indexOf(txb);
					if(index == destinationBoxMap.size() - 1){
						if(listWayPoint.size() == index)
							listWayPoint.add(index, destinationPoint);
						destinationPoint =  new Locate(address, destinationP);
					}
					else {
						listWayPoint.remove(index + 1);
						listWayPoint.add(index + 1,  new Locate(address, destinationP));
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
			else
				txbDestination.setFocus(true);
		}
		else {
			if(destinationBoxMap.get(destinationBoxMap.size()-1).getText().length() != 0)
				destinationBoxMap.add(addDestination());
			else
				destinationBoxMap.get(destinationBoxMap.size()-1).setFocus(true);
		}
	}
	
	Journey getJourney() {
		Journey journey = new Journey();
		List<Locate> locates = new ArrayList<Locate>();
		locates.add(originPoint);
		locates.addAll(listWayPoint);
		locates.add(destinationPoint);
		List<Point> directions = new ArrayList<Point>();
		for (int i = 0; i < directionRouter.getOverviewPath().length(); i++) {
			Point p = new Point(directionRouter.getOverviewPath().get(i));
			directions.add(p);
		}
		journey.setLocates(locates);
		journey.setDirections(directions);
		return journey;
	}
	
	@UiHandler("btnCreateTrip")
	void onBtnCreateTripClick(ClickEvent event) {
		if(VerifiedField()) {
			TripShare.loadBox.center();
			Trip trip = new Trip();
			trip.setName(txbName.getText());
			trip.setDepartureDate(txbDeparture.getValue());
			trip.setDescription(txbDescription.getText());
			trip.setJourney(getJourney());
			TripShare.dataService.insertTrip(trip, TripShare.access_token, new AsyncCallback<Trip>() {
				
				@Override
				public void onSuccess(Trip result) {
					TripShare.loadBox.hide();
					Window.Location.assign("/journey/"+ result.getId());
				}
				
				@Override
				public void onFailure(Throwable caught) {
					TripShare.loadBox.hide();
					Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
				}
			});
		} 
		else {
			Window.scrollTo(0, 20);
		}
	}
	
	boolean VerifiedField() {
		boolean isFieldComplete = true;
		if(txbName.getText().isEmpty()) {
			lbName.setStyleName("font-redTitleNormal");
			isFieldComplete = false;
		} else {
			lbName.setStyleName("font-blackTitleNormal");
		}
		if(txbOrigin.getText().isEmpty()) {
			lbOrigin.setStyleName("font-redTitleNormal");
			isFieldComplete = false;
		} else {
			lbOrigin.setStyleName("font-blackTitleNormal");
		}
		if(txbDestination.getText().isEmpty()) {
			lbDestination.setStyleName("font-redTitleNormal");
			isFieldComplete = false;
		} else {
			lbDestination.setStyleName("font-blackTitleNormal");
		}
		return isFieldComplete;
	}
	
	@UiHandler("btnFindYourLocation")
	void onBtnFindYourLocationClick(ClickEvent event) {
		TripShare.tripMap.getCurrentLocation();
	}
	
}
