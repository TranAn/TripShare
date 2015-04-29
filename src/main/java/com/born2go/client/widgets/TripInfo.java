package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
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
	@UiField Anchor lbPoster;
	@UiField HTMLPanel htmlDestinationTable;
	@UiField DateBox txbDepartureDate;
	@UiField TextBox txbOrigin;
	@UiField HTMLPanel mapTable;
	@UiField Anchor btnAddPart;
	@UiField HTMLPanel editContent;
	
	Locate originPoint;
	List<Locate> listDestinationPoint = new ArrayList<Locate>();
	List<Point> directions = new ArrayList<Point>();
	
	private Trip trip;
	private boolean isEdit = true;

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
	
	CKEditor txbRichDescription;
	
	public CKConfig getCKConfig() {
		// Creates a new config, with FULL preset toolbar as default
		CKConfig ckf = new CKConfig();
		
		// Setting background color
		ckf.setUiColor("#f6f7f8");
		
		// Setting size
//		ckf.setHeight("180px");
		ckf.setResizeMinHeight(180);

		// Creating personalized toolbar
		ToolbarLine line1 = new ToolbarLine();
//		line1.add(TOOLBAR_OPTIONS.Source);
//		line1.add(TOOLBAR_OPTIONS._);
		line1.add(TOOLBAR_OPTIONS.Undo);
		line1.add(TOOLBAR_OPTIONS.Redo);

		// Creates the toolbar
		Toolbar t = new Toolbar();
		t.add(line1);
		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t2 = { TOOLBAR_OPTIONS.Bold, TOOLBAR_OPTIONS.Italic,
				TOOLBAR_OPTIONS.Underline, TOOLBAR_OPTIONS._,
				TOOLBAR_OPTIONS.RemoveFormat, TOOLBAR_OPTIONS._ };
		ToolbarLine line2 = new ToolbarLine();
		line2.addAll(t2);
		t.add(line2);
		t.addSeparator();

		// Define the third line
//		TOOLBAR_OPTIONS[] t3 = { TOOLBAR_OPTIONS.Find, TOOLBAR_OPTIONS.Replace, };
//		ToolbarLine line3 = new ToolbarLine();
//		line3.addAll(t3);
//		t.add(line3);
//		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t4 = {
		TOOLBAR_OPTIONS.JustifyLeft, TOOLBAR_OPTIONS.JustifyCenter,
				TOOLBAR_OPTIONS.JustifyRight, TOOLBAR_OPTIONS.JustifyBlock };
		
		ToolbarLine line4 = new ToolbarLine();
		line4.addAll(t4);
		t.add(line4);
		t.addSeparator();

		ToolbarLine line5 = new ToolbarLine();
		line5.add(TOOLBAR_OPTIONS.Link);
		line5.add(TOOLBAR_OPTIONS.Unlink);
		t.add(line5);
		t.addSeparator();

		ToolbarLine line6 = new ToolbarLine();
		line6.add(TOOLBAR_OPTIONS.Image);
		line6.add(TOOLBAR_OPTIONS.Smiley);
		t.add(line6);
		t.addSeparator();

//		ToolbarLine line7 = new ToolbarLine();
//		line7.add(TOOLBAR_OPTIONS.Styles);
//		t.add(line7);
//
//		ToolbarLine line8 = new ToolbarLine();
//		line8.add(TOOLBAR_OPTIONS.Format);
//		t.add(line8);
//		t.addSeparator();

		ToolbarLine line9 = new ToolbarLine();
		line9.add(TOOLBAR_OPTIONS.Font);
		t.add(line9);
		t.addSeparator();

		ToolbarLine line10 = new ToolbarLine();
		line10.add(TOOLBAR_OPTIONS.FontSize);
		t.add(line10);
		t.addSeparator();

		ToolbarLine line11 = new ToolbarLine();
		line11.add(TOOLBAR_OPTIONS.TextColor);
//		line11.add(TOOLBAR_OPTIONS.SpecialChar);
		line11.add(TOOLBAR_OPTIONS.Maximize);
		t.add(line11);
		t.addSeparator();
		
		// Set the toolbar to the config (replace the FULL preset toolbar)
		ckf.setToolbar(t);
		return ckf;
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
		if(trip.getPoster() != null) {
			lbPoster.setText(trip.getPoster().getUserName());
			lbPoster.setHref("/profile/"+ trip.getPoster().getUserID());
		}
		else
			lbPoster.setText("Tester");
		txbDepartureDate.setValue(trip.getDepartureDate());
		//
		editContent.clear();
		if(isEdit) {
			txbRichDescription = new CKEditor(getCKConfig());
			editContent.add(txbRichDescription);
			txbRichDescription.setData(trip.getDescription());
			isEdit = false;
		}
		else {
			isEdit = true;
			editContent.getElement().setInnerHTML(trip.getDescription());
		}
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
		trip.setDescription(txbRichDescription.getData());
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
				Window.alert(TripShare.ERROR_MESSAGE);
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
