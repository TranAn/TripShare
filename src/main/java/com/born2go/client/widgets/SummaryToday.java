package com.born2go.client.widgets;

import com.born2go.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SummaryToday extends Composite {

	private static SuBinder uiBinder = GWT.create(SuBinder.class);

	interface SuBinder extends UiBinder<Widget, SummaryToday> {
	}

	@UiField(provided = true)
	DataGrid<User> gridProjects;

	public SummaryToday() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
