package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class Destination_PhotoOption extends DialogBox {

	private static Destination_PhotoOptionUiBinder uiBinder = GWT
			.create(Destination_PhotoOptionUiBinder.class);

	interface Destination_PhotoOptionUiBinder extends
			UiBinder<Widget, Destination_PhotoOption> {
	}
			
	@UiField Image imgUploading;
			
	public interface Listener {
		void onViewClick();
		void onSetFeaturedPhotoClick();
		void onDeleteClick();
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public Destination_PhotoOption() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoginDialog-Obj1");
		setGlassEnabled(true);
		setAutoHideEnabled(true);
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
	
	public void startLoading() {
		imgUploading.setVisible(true);
	}
	
	public void endLoading() {
		imgUploading.setVisible(false);
	}
	
	@UiHandler("btnView")
	void onBtnViewClick(ClickEvent event) {
		if(listener != null) {
			listener.onViewClick();
		}
	}
	
	@UiHandler("btnSetFeaturedPhoto")
	void onBtnSetFeaturedPhotoClick(ClickEvent event) {
		if(listener != null) {
			listener.onSetFeaturedPhotoClick();
		}
	}
	
	@UiHandler("btnDelete")
	void onBtnDeleteClick(ClickEvent event) {
		if(listener != null) {
			listener.onDeleteClick();
		}
	}

}
