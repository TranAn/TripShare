package com.itpro.tripshare.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class Menu extends Composite {

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);
	
	@UiField
	Anchor menuPlanTrip;
	@UiField
	Anchor menuHome;

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}

	public Menu() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("menuHome")
	void onMenuHomeClick(ClickEvent event) {
		Window.Location.assign("/");
	}

	@UiHandler("menuPlanTrip")
	void onMenuPlanTripClick(ClickEvent event) {
		Window.Location.assign("/create/");
	}
}
