package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Destination_ToolBar extends Composite {

	private static Destination_ToolBarUiBinder uiBinder = GWT
			.create(Destination_ToolBarUiBinder.class);

	interface Destination_ToolBarUiBinder extends
			UiBinder<Widget, Destination_ToolBar> {
	}
			
	@UiField HorizontalPanel commonToolbar;
	@UiField HorizontalPanel editToolbar;
			
	@UiField Anchor btnTripHome;
	@UiField Anchor btnPreviousPost;
	@UiField Anchor btnNextPost;
	@UiField Anchor btnGallery;
	@UiField Anchor btnComment;
	@UiField Anchor btnEdit;
	@UiField Anchor btnUploadPhoto;
	@UiField Anchor btnSave;
	@UiField Anchor btnCancel;
	
	public interface Listener {
		void onEdit();
		void onUploadPhoto();
		
		void onSave();
		void onCancel();
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public Destination_ToolBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setNavigator(String tripLink, String prePostLink, String nextPostLink) {
		btnTripHome.setHref(tripLink);
		if(!prePostLink.equals("#"))
			btnPreviousPost.removeStyleName("DestinationToolBar_Obj5");
		if(!nextPostLink.equals("#"))
			btnNextPost.removeStyleName("DestinationToolBar_Obj5");
		btnPreviousPost.setHref(prePostLink);
		btnNextPost.setHref(nextPostLink);
	}
	
	public void openPosterToolbar() {
		btnEdit.removeStyleName("DestinationToolBar_Obj5");
		btnUploadPhoto.removeStyleName("DestinationToolBar_Obj5");
	}
	
	public void setCommonToolbar() {
		commonToolbar.setVisible(true);
		editToolbar.setVisible(false);
	}
	
	public void setEditToolbar() {
		commonToolbar.setVisible(false);
		editToolbar.setVisible(true);
	}
	
	@UiHandler("btnGallery")
	void onBtnGalleryClick(ClickEvent event) {
		if(DOM.getElementById("gallery") != null)
			Window.scrollTo(0, DOM.getElementById("gallery").getAbsoluteTop());
	}
	
	@UiHandler("btnComment")
	void onBtnCommentClick(ClickEvent event) {
		if(DOM.getElementById("facebook") != null)
			Window.scrollTo(0, DOM.getElementById("facebook").getAbsoluteTop());
	}
	
	@UiHandler("btnEdit")
	void onBtnEditClick(ClickEvent event) {
		if(listener != null)
			listener.onEdit();
	}
	
	@UiHandler("btnUploadPhoto")
	void onBtnUploadPhotoClick(ClickEvent event) {
		if(listener != null)
			listener.onUploadPhoto();
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		if(listener != null)
			listener.onSave();
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		if(listener != null)
			listener.onCancel();
	}

}
