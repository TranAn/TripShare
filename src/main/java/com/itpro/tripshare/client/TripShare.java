package com.itpro.tripshare.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.itpro.tripshare.client.rpc.PictureService;
import com.itpro.tripshare.client.rpc.PictureServiceAsync;

public class TripShare implements EntryPoint {
	FormPanel uploadForm;
	final PictureServiceAsync blobService = GWT.create(PictureService.class);

	@Override
	public void onModuleLoad() {
		uploadForm = new FormPanel();
		HorizontalPanel hori = new HorizontalPanel();
		Label lb = new Label("Them anh moi");
		hori.add(lb);

		TextBox onTrip = new TextBox();
		hori.add(onTrip);
		onTrip.setName("onTrip");

		FileUpload upload = new FileUpload();
		hori.add(upload);

		Button btSubmit = new Button("Submit");
		hori.add(btSubmit);

		uploadForm.setWidget(hori);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		upload.setName("uploadFile");
		RootPanel.get().add(uploadForm);
		startNewBlobstoreSession();
		btSubmit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadForm.submit();
				

			}
		});
		 
		uploadForm
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {

						uploadForm.reset();
					
						 
						System.out.println("submit complete"
								+ event.getResults());
					}
				});

	}

	private void startNewBlobstoreSession() {

		blobService.getURLUpload(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				uploadForm.setAction(result.toString());
				uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
				uploadForm.setMethod(FormPanel.METHOD_POST);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void onSubmitComplete(SubmitCompleteEvent event) {
		uploadForm.reset();
		startNewBlobstoreSession();

		// This is what gets the result back - the content-type *must* be
		// text-html
		String imageUrl = event.getResults();
		Image image = new Image();
		image.setUrl(imageUrl);

		final PopupPanel imagePopup = new PopupPanel(true);
		imagePopup.setWidget(image);

		// Add some effects
		imagePopup.setAnimationEnabled(true); // animate opening the image
		imagePopup.setGlassEnabled(true); // darken everything under the image
		imagePopup.setAutoHideEnabled(true); // close image when the user clicks
												// outside it
		imagePopup.center(); // center the image

	}

}
