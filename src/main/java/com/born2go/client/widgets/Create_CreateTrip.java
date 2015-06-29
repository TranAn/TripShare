package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.born2go.client.TripShare;
import com.born2go.client.widgets.Create_HandlerJsonListFriends.ListFriends;
import com.born2go.shared.embedclass.Journey;
import com.born2go.shared.embedclass.Locate;
import com.born2go.shared.embedclass.Poster;
import com.born2go.shared.embedclass.Journey.Point;
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
import com.google.gwt.user.client.DOM;
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

public class Create_CreateTrip extends Composite {

	private static CreateTripUiBinder uiBinder = GWT
			.create(CreateTripUiBinder.class);
	
	@UiField HTMLPanel mapContainer;
	@UiField Anchor btnCreateTrip;
	@UiField Anchor btnAddPart;
	@UiField Anchor btnRichTextEdit;
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
	@UiField HTMLPanel editTextBox;
	@UiField Anchor btnAddFriend;

	interface CreateTripUiBinder extends UiBinder<Widget, Create_CreateTrip> {
	}
	
	Locate originPoint;
	Locate destinationPoint;
	List<Locate> listWayPoint = new ArrayList<Locate>();
	
	@SuppressWarnings("unchecked")
	JsArray<DirectionsWaypoint> waypoints = (JsArray<DirectionsWaypoint>) JsArray.createArray();
	DirectionsRoute directionRouter;
	
	CKEditor txbRichDescription;
	boolean isRichTextEdit = false;
	
	public CKConfig getCKConfig() {
		// Creates a new config, with FULL preset toolbar as default
		CKConfig ckf = new CKConfig();
		
		// Setting background color
		ckf.setUiColor("#f6f7f8");
		
		// Setting size
		ckf.setWidth("100%");
		ckf.setHeight("");
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

	public Create_CreateTrip() {
		initWidget(uiBinder.createAndBindUi(this));
		mapContainer.add(TripShare.tripMap.getMapView());
		
		txbName.getElement().setPropertyString("placeholder", "Name of your journey");
		txbDescription.getElement().setPropertyString("placeholder", "Let's talk about the plan for your trip!");
		txbDescription.getElement().setAttribute("spellcheck", "false");
		txbDeparture.setValue(new Date());
		btnAddFriend.getElement().setAttribute("id", "btnAddFriend");
		
		final Autocomplete autocomplete = Autocomplete
				.create((InputElement) (Element) txbOrigin.getElement());
		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete.getPlace();
				String address = txbOrigin.getText();
				originPoint = new Locate(address, place.getGeometry().getLocation());
				if(destinationPoint == null) {
					TripShare.tripMap.addMarker(place.getGeometry().getLocation(), address, true, true, true);
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
		final Label lb = new Label("Destination point:");
		final TextBox txb = new TextBox();
		final Anchor btn1 = new Anchor();
		final Anchor btn = new Anchor();
		
		lb.setStyleName("font-blackTitleNormal");
		txb.setStyleName("gwt-TextBox CreateTrip-Obj7");
		btn1.setStyleName("greenbutton CreateTrip-Obj13 CreateTrip-Obj20");
		btn.setStyleName("greenbutton CreateTrip-Obj12");
		btn1.getElement().setInnerHTML("<i class='fa fa-picture-o fa-lg'></i>");
		btn.getElement().setInnerHTML("<i class='fa fa-times fa-lg'></i>");
		btn1.setTitle("Find Tourist Place");
		btn.setTitle("Remove Destination Point");
		
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
		if(TripShare.access_token.equals("")) {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.center();
			loginDialog.addStyleName("fadeIn");
		}
		else {
			if(VerifiedField()) {
				TripShare.loadBox.center();
				Trip trip = new Trip();
				trip.setName(txbName.getText());
				trip.setDepartureDate(txbDeparture.getValue());
				trip.setCompanion(listCompanion);
		
				if(!isRichTextEdit) {
					if(txbRichDescription != null) {
						txbRichDescription.setData(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
						trip.setDescription(txbRichDescription.getData());
					} else 
						trip.setDescription("<p>"+ txbDescription.getText().replaceAll("(\r\n|\n)", "<br />")+ "</p>");
				}
				else
					trip.setDescription(txbRichDescription.getData());
				
				trip.setJourney(getJourney());
				
				TripShare.dataService.insertTrip(trip, TripShare.access_token, new AsyncCallback<Trip>() {
					@Override
					public void onSuccess(Trip result) {
						TripShare.loadBox.hide();
						if(result != null)
							Window.Location.assign("/journey/"+ result.getId());
						else
							Window.alert(TripShare.ERROR_MESSAGE);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert(TripShare.ERROR_MESSAGE);
					}
				});
			} 
			else {
				Window.scrollTo(0, 20);
			}
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
	
	@UiHandler("btnRichTextEdit") 
	void onBtnRichTextEditClick(ClickEvent event) {
		if(isRichTextEdit) {
			if(txbRichDescription.getHTML().length() != 0) {
				if(Window.confirm("!: Rich text can't convert to normal text, if you continue the text will be clear.")) {
					txbRichDescription.setVisible(false);
					txbDescription.setVisible(true);
					btnRichTextEdit.removeStyleName("PathCreate-Obj16");
					txbDescription.setText("");
					isRichTextEdit = !isRichTextEdit;
				}
			} else {
				txbRichDescription.setVisible(false);
				txbDescription.setVisible(true);
				btnRichTextEdit.removeStyleName("PathCreate-Obj16");
				isRichTextEdit = !isRichTextEdit;
			}
		}
		else {
			if(txbRichDescription == null) {
				txbRichDescription = new CKEditor(getCKConfig());
				editTextBox.add(txbRichDescription);
			}
			txbRichDescription.setVisible(true);
			txbDescription.setVisible(false);
			btnRichTextEdit.addStyleName("PathCreate-Obj16");
			txbRichDescription.setData(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
			isRichTextEdit = !isRichTextEdit;
		}
	}
	
	@UiHandler("btnAddFriend")
	void onBtnAddFriendClick(ClickEvent event) {
		if(TripShare.access_token.equals("")) {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.center();
			loginDialog.addStyleName("fadeIn");
		}
		else {
			getFriendList();
		}
	}
	
	private static List<Poster> listCompanion = new ArrayList<Poster>();
	
	static void takeListFriends(String jsonListFriends) {
		jsonListFriends = "{\"listFriends\":" + jsonListFriends + "}";
		Create_HandlerJsonListFriends handlerJson = new Create_HandlerJsonListFriends();
		ListFriends LF = handlerJson.deserializeFromJson(jsonListFriends);
		CompanionPickTable ftable = new CompanionPickTable();
		ftable.setListener(new CompanionPickTable.Listener() {
			@Override
			public void onSaveClick(List<Poster> friendsId) {
				listCompanion.clear();
				listCompanion.addAll(friendsId);
				if(!friendsId.isEmpty()) 
					DOM.getElementById("btnAddFriend").addClassName("PathCreate-Obj16");
				else
					DOM.getElementById("btnAddFriend").removeClassName("PathCreate-Obj16");
			}
		});
		ftable.setListFriend(LF.getListFriends(), listCompanion);
		int minusTop = 0;
		minusTop = 20 * (LF.getListFriends().size());
		ftable.setPopupPosition(Window.getClientWidth()/2 - 200, Window.getScrollTop()+ 200 - minusTop);
		ftable.addStyleName("fadeIn");
		ftable.show();
	}
	
	static native String getFriendList() /*-{
		$wnd.FB.api("/me/friends", function (response) {
      		if (response && !response.error) {
        		var jsonString = $wnd.JSON.stringify(response.data);
        		@com.born2go.client.widgets.Create_CreateTrip::takeListFriends(Ljava/lang/String;)(jsonString);
      		}
		});
	}-*/;
	
}
