package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ThemePickTable extends DialogBox {

	private static ThemePickTableUiBinder uiBinder = GWT
			.create(ThemePickTableUiBinder.class);

	interface ThemePickTableUiBinder extends UiBinder<Widget, ThemePickTable> {
	}
	
	@UiField CheckBox ck_default;
	@UiField CheckBox ck_beach_dawn;
	@UiField CheckBox ck_beach_morning;
	@UiField CheckBox ck_beach_sunset;
	@UiField CheckBox ck_city;
	@UiField CheckBox ck_country;
	@UiField CheckBox ck_forest;
	@UiField CheckBox ck_forest_waterfall;
	@UiField CheckBox ck_mountain_spring;
	@UiField CheckBox ck_mountain_winter;
	@UiField Image previewImg;
	
	Trip trip;

	public ThemePickTable(Trip trip) {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoginDialog-Obj1");
		setGlassEnabled(true);
		setAutoHideEnabled(true);
		
		this.trip = trip;
		
		if(trip.getTheme() == null) {
			ck_default.setValue(true);
			previewImg.setUrl("/resources/themes/Default.jpg");
		} else {
			switch (trip.getTheme()) {
			case DEFAULT:
				ck_default.setValue(true);
				previewImg.setUrl("/resources/themes/Default.jpg");
				break;
			case BEACH_DAWN:
				ck_beach_dawn.setValue(true);
				previewImg.setUrl("/resources/themes/Beach-Dawn.jpg");
				break;
			case BEACH_MORNING:
				ck_beach_morning.setValue(true);
				previewImg.setUrl("/resources/themes/Beach-Morning.jpg");
				break;
			case BEACH_SUNSET:
				ck_beach_sunset.setValue(true);
				previewImg.setUrl("/resources/themes/Beach-Sunset.jpg");
				break;
			case CITY:
				ck_city.setValue(true);
				previewImg.setUrl("/resources/themes/City.jpg");
				break;
			case COUNTRY:
				ck_country.setValue(true);
				previewImg.setUrl("/resources/themes/Country.jpg");
				break;
			case FOREST:
				ck_forest.setValue(true);
				previewImg.setUrl("/resources/themes/Forest.jpg");
				break;
			case FOREST_WATERFALL:
				ck_forest_waterfall.setValue(true);
				previewImg.setUrl("/resources/themes/Forest-Waterfall.jpg");
				break;
			case MOUNTAIN_SPRING:
				ck_mountain_spring.setValue(true);
				previewImg.setUrl("/resources/themes/Mountain-Spring.jpg");
				break;
			case MOUNTAIN_WINTER:
				ck_mountain_winter.setValue(true);
				previewImg.setUrl("/resources/themes/Mountain-Winter.jpg");
				break;
			}
		}
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				hide();
			}
			break;
		}
	}
	
	void clearCheck() {
		ck_default.setValue(false);
		ck_beach_dawn.setValue(false);
		ck_beach_morning.setValue(false);
		ck_beach_sunset.setValue(false);
		ck_city.setValue(false);
		ck_country.setValue(false);
		ck_forest.setValue(false);
		ck_forest_waterfall.setValue(false);
		ck_mountain_spring.setValue(false);
		ck_mountain_winter.setValue(false);
	}
	
	@UiHandler("ck_default")
	void onCKDefaultClick(ClickEvent event) {
		clearCheck();
		ck_default.setValue(true);
		previewImg.setUrl("/resources/themes/Default.jpg");
		trip.setTheme(Trip.Theme.DEFAULT);
	}
	
	@UiHandler("ck_beach_dawn")
	void onCKBeachDawnClick(ClickEvent event) {
		clearCheck();
		ck_beach_dawn.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Dawn.jpg");
		trip.setTheme(Trip.Theme.BEACH_DAWN);
	}
	
	@UiHandler("ck_beach_morning")
	void onCKBeachMorningClick(ClickEvent event) {
		clearCheck();
		ck_beach_morning.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Morning.jpg");
		trip.setTheme(Trip.Theme.BEACH_MORNING);
	}
	
	@UiHandler("ck_beach_sunset")
	void onCKBeachSunsetClick(ClickEvent event) {
		clearCheck();
		ck_beach_sunset.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Sunset.jpg");
		trip.setTheme(Trip.Theme.BEACH_SUNSET);
	}
	
	@UiHandler("ck_city")
	void onCKCityClick(ClickEvent event) {
		clearCheck();
		ck_city.setValue(true);
		previewImg.setUrl("/resources/themes/City.jpg");
		trip.setTheme(Trip.Theme.CITY);
	}
	
	@UiHandler("ck_country")
	void onCKCountryClick(ClickEvent event) {
		clearCheck();
		ck_country.setValue(true);
		previewImg.setUrl("/resources/themes/Country.jpg");
		trip.setTheme(Trip.Theme.COUNTRY);
	}
	
	@UiHandler("ck_forest")
	void onCKForestClick(ClickEvent event) {
		clearCheck();
		ck_forest.setValue(true);
		previewImg.setUrl("/resources/themes/Forest.jpg");
		trip.setTheme(Trip.Theme.FOREST);
	}
	
	@UiHandler("ck_forest_waterfall")
	void onCKForestWaterfallClick(ClickEvent event) {
		clearCheck();
		ck_forest_waterfall.setValue(true);
		previewImg.setUrl("/resources/themes/Forest-Waterfall.jpg");
		trip.setTheme(Trip.Theme.FOREST_WATERFALL);
	}
	
	@UiHandler("ck_mountain_spring")
	void onCKMountainSpringClick(ClickEvent event) {
		clearCheck();
		ck_mountain_spring.setValue(true);
		previewImg.setUrl("/resources/themes/Mountain-Spring.jpg");
		trip.setTheme(Trip.Theme.MOUNTAIN_SPRING);
	}
	
	@UiHandler("ck_mountain_winter")
	void onCKMountainWinterClick(ClickEvent event) {
		clearCheck();
		ck_mountain_winter.setValue(true);
		previewImg.setUrl("/resources/themes/Mountain-Winter.jpg");
		trip.setTheme(Trip.Theme.MOUNTAIN_WINTER);
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		hide();
		TripShare.loadBox.center();
		TripShare.dataService.updateTrip(trip, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				TripShare.loadBox.hide();
				switch (trip.getTheme()) {
				case DEFAULT:
					setBodyClass("journeypage_default_theme");
					break;
				case BEACH_DAWN:
					setBodyClass("journeypage_beachdawn_theme");
					break;
				case BEACH_MORNING:
					setBodyClass("journeypage_beachmorning_theme");
					break;
				case BEACH_SUNSET:
					setBodyClass("journeypage_beachsunset_theme");
					break;
				case CITY:
					setBodyClass("journeypage_city_theme");
					break;
				case COUNTRY:
					setBodyClass("journeypage_country_theme");
					break;
				case FOREST:
					setBodyClass("journeypage_forest_theme");
					break;
				case FOREST_WATERFALL:
					setBodyClass("journeypage_forestwaterfall_theme");
					break;
				case MOUNTAIN_SPRING:
					setBodyClass("journeypage_mountainspring_theme");
					break;
				case MOUNTAIN_WINTER:
					setBodyClass("journeypage_mountainwinter_theme");
					break;
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				TripShare.loadBox.hide();
				Window.alert(TripShare.ERROR_MESSAGE);
			}
		});
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		hide();
	}
	
	public static native void setBodyClass(String clazzName) /*-{
		var c = clazzName;
		$wnd.document.body.className = c;
	}-*/;

}
