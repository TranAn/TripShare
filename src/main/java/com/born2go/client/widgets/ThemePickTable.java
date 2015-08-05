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
	
		switch (trip.getTheme()) {
		case Trip.DEFAULT_THEME:
			ck_default.setValue(true);
			previewImg.setUrl("/resources/themes/Default-thumbnail.jpg");
			break;
		case Trip.BEACH_DAWN_THEME:
			ck_beach_dawn.setValue(true);
			previewImg.setUrl("/resources/themes/Beach-Dawn-thumbnail.jpg");
			break;
		case Trip.BEACH_MORNING_THEME:
			ck_beach_morning.setValue(true);
			previewImg.setUrl("/resources/themes/Beach-Morning-thumbnail.jpg");
			break;
		case Trip.BEACH_SUNSET_THEME:
			ck_beach_sunset.setValue(true);
			previewImg.setUrl("/resources/themes/Beach-Sunset-thumbnail.jpg");
			break;
		case Trip.CITY_THEME:
			ck_city.setValue(true);
			previewImg.setUrl("/resources/themes/City-thumbnail.jpg");
			break;
		case Trip.COUNTRY_THEME:
			ck_country.setValue(true);
			previewImg.setUrl("/resources/themes/Country-thumbnail.jpg");
			break;
		case Trip.FOREST_THEME:
			ck_forest.setValue(true);
			previewImg.setUrl("/resources/themes/Forest-thumbnail.jpg");
			break;
		case Trip.FOREST_WATERFALL_THEME:
			ck_forest_waterfall.setValue(true);
			previewImg.setUrl("/resources/themes/Forest-Waterfall-thumbnail.jpg");
			break;
		case Trip.MOUNTAIN_SPRING_THEME:
			ck_mountain_spring.setValue(true);
			previewImg.setUrl("/resources/themes/Mountain-Spring-thumbnail.jpg");
			break;
		case Trip.MOUNTAIN_WINTER_THEME:
			ck_mountain_winter.setValue(true);
			previewImg.setUrl("/resources/themes/Mountain-Winter-thumbnail.jpg");
			break;
		default:
			ck_default.setValue(true);
			previewImg.setUrl("/resources/themes/Default-thumbnail.jpg");
			break;
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
		previewImg.setUrl("/resources/themes/Default-thumbnail.jpg");
		trip.setTheme(Trip.DEFAULT_THEME);
	}
	
	@UiHandler("ck_beach_dawn")
	void onCKBeachDawnClick(ClickEvent event) {
		clearCheck();
		ck_beach_dawn.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Dawn-thumbnail.jpg");
		trip.setTheme(Trip.BEACH_DAWN_THEME);
	}
	
	@UiHandler("ck_beach_morning")
	void onCKBeachMorningClick(ClickEvent event) {
		clearCheck();
		ck_beach_morning.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Morning-thumbnail.jpg");
		trip.setTheme(Trip.BEACH_MORNING_THEME);
	}
	
	@UiHandler("ck_beach_sunset")
	void onCKBeachSunsetClick(ClickEvent event) {
		clearCheck();
		ck_beach_sunset.setValue(true);
		previewImg.setUrl("/resources/themes/Beach-Sunset-thumbnail.jpg");
		trip.setTheme(Trip.BEACH_SUNSET_THEME);
	}
	
	@UiHandler("ck_city")
	void onCKCityClick(ClickEvent event) {
		clearCheck();
		ck_city.setValue(true);
		previewImg.setUrl("/resources/themes/City-thumbnail.jpg");
		trip.setTheme(Trip.CITY_THEME);
	}
	
	@UiHandler("ck_country")
	void onCKCountryClick(ClickEvent event) {
		clearCheck();
		ck_country.setValue(true);
		previewImg.setUrl("/resources/themes/Country-thumbnail.jpg");
		trip.setTheme(Trip.COUNTRY_THEME);
	}
	
	@UiHandler("ck_forest")
	void onCKForestClick(ClickEvent event) {
		clearCheck();
		ck_forest.setValue(true);
		previewImg.setUrl("/resources/themes/Forest-thumbnail.jpg");
		trip.setTheme(Trip.FOREST_THEME);
	}
	
	@UiHandler("ck_forest_waterfall")
	void onCKForestWaterfallClick(ClickEvent event) {
		clearCheck();
		ck_forest_waterfall.setValue(true);
		previewImg.setUrl("/resources/themes/Forest-Waterfall-thumbnail.jpg");
		trip.setTheme(Trip.FOREST_WATERFALL_THEME);
	}
	
	@UiHandler("ck_mountain_spring")
	void onCKMountainSpringClick(ClickEvent event) {
		clearCheck();
		ck_mountain_spring.setValue(true);
		previewImg.setUrl("/resources/themes/Mountain-Spring-thumbnail.jpg");
		trip.setTheme(Trip.MOUNTAIN_SPRING_THEME);
	}
	
	@UiHandler("ck_mountain_winter")
	void onCKMountainWinterClick(ClickEvent event) {
		clearCheck();
		ck_mountain_winter.setValue(true);
		previewImg.setUrl("/resources/themes/Mountain-Winter-thumbnail.jpg");
		trip.setTheme(Trip.MOUNTAIN_WINTER_THEME);
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		hide();
		TripShare.loadBox.center();
		TripShare.dataService.updateTrip(trip, TripShare.access_token, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				TripShare.loadBox.hide();
				switch (trip.getTheme()) {
				case Trip.DEFAULT_THEME:
					setBodyClass("journeypage_default_theme");
					break;
				case Trip.BEACH_DAWN_THEME:
					setBodyClass("journeypage_beachdawn_theme");
					break;
				case Trip.BEACH_MORNING_THEME:
					setBodyClass("journeypage_beachmorning_theme");
					break;
				case Trip.BEACH_SUNSET_THEME:
					setBodyClass("journeypage_beachsunset_theme");
					break;
				case Trip.CITY_THEME:
					setBodyClass("journeypage_city_theme");
					break;
				case Trip.COUNTRY_THEME:
					setBodyClass("journeypage_country_theme");
					break;
				case Trip.FOREST_THEME:
					setBodyClass("journeypage_forest_theme");
					break;
				case Trip.FOREST_WATERFALL_THEME:
					setBodyClass("journeypage_forestwaterfall_theme");
					break;
				case Trip.MOUNTAIN_SPRING_THEME:
					setBodyClass("journeypage_mountainspring_theme");
					break;
				case Trip.MOUNTAIN_WINTER_THEME:
					setBodyClass("journeypage_mountainwinter_theme");
					break;
				default:
					setBodyClass("journeypage_default_theme");
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
