package com.itpro.tripshare.client.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.itpro.tripshare.client.rpc.DataServiceAsync;

public class UploadPicture extends Composite {
	FormPanel uploadForm;
	final DataServiceAsync blobService = GWT.create(DataServiceAsync.class);

	public UploadPicture() {
		
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

		uploadForm.add(hori);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		upload.setName("uploadFile");

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
}
