package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class LoadingBox extends DialogBox {

	private static LoadingBoxUiBinder uiBinder = GWT
			.create(LoadingBoxUiBinder.class);

	interface LoadingBoxUiBinder extends UiBinder<Widget, LoadingBox> {
	}

	public LoadingBox() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoadingBox-Obj1");
		setGlassEnabled(true);
	}

}
