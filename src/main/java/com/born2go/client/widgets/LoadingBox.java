package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoadingBox extends DialogBox {

	private static LoadingBoxUiBinder uiBinder = GWT
			.create(LoadingBoxUiBinder.class);

	interface LoadingBoxUiBinder extends UiBinder<Widget, LoadingBox> {
	}
	
	@UiField Label uploadPercent;

	public LoadingBox() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoadingBox-Obj1");
		setGlassEnabled(true);
		
		this.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> arg0) {
				uploadPercent.setVisible(false);
			}
		});
	}
	
	public void setUploadPercent(String percent) {
		uploadPercent.setVisible(true);
		uploadPercent.setText(percent);
	}

}
