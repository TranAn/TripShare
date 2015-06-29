package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.born2go.shared.embedclass.Locate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
import com.google.maps.gwt.client.places.PlaceResult;

public class LocationPicker extends DialogBox {

	private static LocationPickerUiBinder uiBinder = GWT
			.create(LocationPickerUiBinder.class);

	interface LocationPickerUiBinder extends UiBinder<Widget, LocationPicker> {
	}
	
	@UiField HTMLPanel mapTable;
	@UiField TextBox txbLocation;
	@UiField Anchor btnCancel;
	@UiField Anchor btnCurrentLocation;
	
	private Locate locate;
	
	public interface Listener {
		void getLocation(Locate locate);
	}
	
	private Listener listener;
	
	public void setListerner(Listener listener) {
		this.listener = listener;
	}

	public LocationPicker() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LocationPicker_Dialog");

		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		mapTable.add(TripShare.tripMap.getMapView());
		
		final Autocomplete autocomplete = Autocomplete
				.create((InputElement) (Element) txbLocation.getElement());
		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
			public void handle() {
				PlaceResult place = autocomplete.getPlace();
				LatLng position = place.getGeometry().getLocation();
				TripShare.tripMap.addMarker2(position, txbLocation.getText(), false);
				TripShare.tripMap.getMap().setCenter(position);
				locate = new Locate(txbLocation.getText(), position);	
			}
		});
		
		TripShare.tripMap.clickToAddMarker();
		TripShare.tripMap.setListener(new TravelMap.Listener() {
			
			@Override
			public void getLocation(String address, LatLng position) {
				txbLocation.setText(address);
				locate = new Locate(address, position);
			}
			
			@Override
			public void getDirectionResult(DirectionsResult directionResult) {}
		});
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				cancelAddLocation();
			}
			break;
		}
	}
	
	void cancelAddLocation() {
		TripShare.tripMap.removeClickToAddMarker();
		TripShare.tripMap.removeLocationMarker();
		hide();
	}
	
	public void setFocus() {
		txbLocation.setFocus(true);
	}
	
	public void setLocation(Locate locate) {
		this.locate = locate;
		if(locate != null) {
			txbLocation.setText(locate.getAddressName());
			TripShare.tripMap.addMarker2(locate.getLatLng(), locate.getAddressName(), false);
			TripShare.tripMap.getMap().setCenter(locate.getLatLng());
		}
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		cancelAddLocation();
	}
	
	@UiHandler("btnCurrentLocation")
	void onBtnCurrentLocationClick(ClickEvent event) {
		Geolocation geoLocation = Geolocation.getIfSupported();
		if (geoLocation == null) {
			Window.alert("!: Your old browser not support GeoLocation");
		} else {
			geoLocation.getCurrentPosition(new com.google.gwt.core.client.Callback<Position, PositionError>() {
				
				@Override
				public void onSuccess(Position result) {
					final LatLng l = LatLng.create(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
					GeocoderRequest geoRequest = GeocoderRequest.create();
					geoRequest.setLocation(l);
					Geocoder geoCode = Geocoder.create();
					geoCode.geocode(geoRequest, new Geocoder.Callback() {
						@Override
						public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
							String address = a.get(0).getFormattedAddress();
							txbLocation.setText(address);
							TripShare.tripMap.addMarker2(l, address, false);
							TripShare.tripMap.getMap().setCenter(l);
							locate = new Locate(txbLocation.getText(), l);	
						}
					});
				}
				
				@Override
				public void onFailure(PositionError reason) {
					Window.alert("!: Can't find your location");
				}
			});
		}
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		if(locate != null)
			locate.setAddressName(txbLocation.getText());
		cancelAddLocation();
		if(listener != null) 
			listener.getLocation(locate);
	}
	
	@UiHandler("btnClearLocation")
	void onBtnClearLocationClick(ClickEvent event) {
		locate = null;
		txbLocation.setText("");
		TripShare.tripMap.removeLocationMarker();
	}

}
