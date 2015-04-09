package com.born2go.client.widgets;

import java.util.ArrayList;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsRoute;
import com.google.maps.gwt.client.DirectionsWaypoint;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
import com.google.maps.gwt.client.places.PlaceResult;

public class TripInfo extends Composite {

	private static TripInfoUiBinder uiBinder = GWT
			.create(TripInfoUiBinder.class);
	
	@UiField TextBox txbName;
	@UiField Label lbPoster;
	@UiField HTMLPanel htmlDestinationTable;
	@UiField DateBox txbDepartureDate;
	@UiField StretchyTextArea txbDescription;
	@UiField TextBox txbOrigin;
	@UiField HTMLPanel mapTable;
	@UiField Anchor btnAddPart;
	
	Locate originPoint;
	List<Locate> listDestinationPoint = new ArrayList<Locate>();
	List<Point> directions = new ArrayList<Point>();
	
	private Trip trip;

	interface TripInfoUiBinder extends UiBinder<Widget, TripInfo> {
	}
	
	public interface Listener {
		void onUpdateTrip(Trip updateTrip);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	@SuppressWarnings("unchecked")
	JsArray<DirectionsWaypoint> waypoints = (JsArray<DirectionsWaypoint>) JsArray.createArray();
	
	void findTheJourney() {
		waypoints.setLength(0);
		for(int i=0; i<listDestinationPoint.size()-1; i++) {
			DirectionsWaypoint waypoint = DirectionsWaypoint.create();
			waypoint.setLocation(listDestinationPoint.get(i).getLatLng());
			waypoints.push(waypoint);
		}
		if(waypoints.length() != 0)
			TripShare.tripMap.findDirection(originPoint.getLatLng(), listDestinationPoint.get(listDestinationPoint.size()-1).getLatLng(), waypoints);
		else
			TripShare.tripMap.findDirection(originPoint.getLatLng(), listDestinationPoint.get(listDestinationPoint.size()-1).getLatLng(), null);
	}

	Journey getJourney() {
		Journey journey = new Journey();
		List<Locate> locates = new ArrayList<Locate>();
		locates.add(originPoint);
		locates.addAll(listDestinationPoint);
		journey.setLocates(locates);
		journey.setDirections(directions);
		return journey;
	}

	public TripInfo() {
		initWidget(uiBinder.createAndBindUi(this));
		
		final Autocomplete autocomplete = Autocomplete
				.create((InputElement) (Element) txbOrigin.getElement());
		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete.getPlace();
				String address = txbOrigin.getText();
				originPoint = new Locate(address, place.getGeometry().getLocation());
				findTheJourney();
			}
		});
	}
	
	public void setTrip(Trip trip) {
		this.trip = trip;
		mapTable.clear();
		mapTable.add(TripShare.tripMap.getMapView());
		txbName.setText(trip.getName());
		if(trip.getPoster() != null)
			lbPoster.setText("Create by "+ trip.getPoster().getUserName());
		else
			lbPoster.setText("Create by Tester");
		txbDepartureDate.setValue(trip.getDepartureDate());
		txbDescription.setValue(trip.getDescription());
		txbDescription.setHeight("");
		txbDescription.setVisible(true);
		//
		originPoint = trip.getJourney().getLocates().get(0);
		txbOrigin.setText(trip.getJourney().getLocates().get(0).getAddressName());
		listDestinationPoint.clear();
		htmlDestinationTable.clear();
		for(int i = 1; i < trip.getJourney().getLocates().size(); i++) {
			listDestinationPoint.add(trip.getJourney().getLocates().get(i));
			htmlDestinationTable.add(addDestination(trip.getJourney().getLocates().get(i).getAddressName()));
		}
		directions.clear();
		directions.addAll(trip.getJourney().getDirections());
	}
	
	public void setDirectionResult(DirectionsResult directionResult) {
		DirectionsRoute directionRouter = directionResult.getRoutes().get(0);
		directions.clear();
		for (int i = 0; i < directionRouter.getOverviewPath().length(); i++) {
			Point p = new Point(directionRouter.getOverviewPath().get(i));
			directions.add(p);
		}
	}

	List<TextBox> destinationBoxMap = new ArrayList<TextBox>();
	List<Anchor> listBtnRemoveDestination = new ArrayList<Anchor>();
	
	public HTMLPanel addDestination(String address) {
		final HTMLPanel tripDestination = new HTMLPanel("");
		tripDestination.setStyleName("trip-destinations");
		Image marker = new Image("/resources/green-spotlight.png");
		marker.setStyleName("TripInfo-Obj5");
		final TextBox txbDestination = new TextBox();
		txbDestination.setStyleName("gwt-TextBox TripInfo-Obj6");
		txbDestination.setText(address);
		final Anchor btnRemove = new Anchor();
		btnRemove.setStyleName("greenbutton CreateTrip-Obj12");
		btnRemove.getElement().setInnerHTML("<i class='fa fa-times fa-lg'></i>");
		final Autocomplete autocomplete2 = Autocomplete
				.create((InputElement) (Element) txbDestination.getElement());
		//
		tripDestination.add(marker);
		tripDestination.add(txbDestination);
		if(!destinationBoxMap.isEmpty())
			tripDestination.add(btnRemove);
		destinationBoxMap.add(txbDestination);
		listBtnRemoveDestination.add(btnRemove);
		//
		autocomplete2.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete2.getPlace();
				String address = txbDestination.getText();
				int index = destinationBoxMap.indexOf(txbDestination);
				Locate locate = new Locate(address, place.getGeometry().getLocation());
				if(listDestinationPoint.size()-1 >= index)
					listDestinationPoint.remove(index);
				listDestinationPoint.add(index, locate);
				findTheJourney();
			}
		});
		btnRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tripDestination.removeFromParent();
				int index = destinationBoxMap.indexOf(txbDestination);
				if(listDestinationPoint.size()-1 >= index)
					listDestinationPoint.remove(index);
				destinationBoxMap.remove(index);
				findTheJourney();
			}
		});
		return tripDestination;
	}

	public void updateTrip() {
		TripShare.loadBox.center();
		trip.setName(txbName.getText());
		trip.setDepartureDate(txbDepartureDate.getValue());
		trip.setDescription(txbDescription.getText());
		trip.setJourney(getJourney());
		TripShare.dataService.updateTrip(trip, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				TripShare.loadBox.hide();
				if(listener != null)
					listener.onUpdateTrip(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				TripShare.loadBox.hide();
				Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
			}
		});
	}

	public void setDisable() {
		txbName.addStyleName("TripInfo-Disable");
		txbName.removeStyleName("TripInfo-Obj3Edit");
		txbOrigin.addStyleName("TripInfo-Disable");
		for(TextBox t: destinationBoxMap) {
			t.addStyleName("TripInfo-Disable");
		}
		txbDepartureDate.addStyleName("TripInfo-Disable");
		txbDescription.addStyleName("TripInfo-Disable");
		txbDescription.removeStyleName("TripInfo-Obj8Edit");
		btnAddPart.removeFromParent();
		for(Anchor a: listBtnRemoveDestination) {
			a.removeFromParent();
		}
	}

	@UiHandler("btnAddPart")
	void onBtnAddPartClick(ClickEvent event) {
		int index = destinationBoxMap.size() - 1;
		if(listDestinationPoint.size()-1 < index || destinationBoxMap.get(index).getText().equals(""))
			destinationBoxMap.get(index).setFocus(true);
		else
			htmlDestinationTable.add(addDestination(""));
	}
	
}
