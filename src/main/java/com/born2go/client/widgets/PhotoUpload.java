package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;

public class PhotoUpload extends DialogBox {

	private static Binder uiBinder = GWT.create(Binder.class);
	
	@UiField FileUpload fileUpload;
	@UiField HTMLPanel imageTable;
	@UiField FormPanel formUpload;
	@UiField LongBox boxTripId;
	@UiField LongBox boxPathId;
	@UiField Label lbPhotosCount;
	@UiField Image imgUploading;
	
	boolean isHandlerUploadEvent = false;

	interface Binder extends UiBinder<Widget, PhotoUpload> {
	}
	
	public PhotoUpload() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("PhotoUploadDialog");

		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		formUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
		formUpload.setMethod(FormPanel.METHOD_POST);
		
		fileUpload.setName("filesUpload");
		boxTripId.setName("tripId");
		boxPathId.setName("pathId");
	
		fileUpload.getElement().setAttribute("id", "fileUpload");
		imageTable.getElement().setAttribute("id", "imageTable");
		lbPhotosCount.getElement().setAttribute("id", "lbPhotosCount");
		
		DOM.setElementProperty(fileUpload.getElement(), "multiple", "multiple"); 
		
		formUpload.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				cancelUpload();
				if(Window.Location.getPath().contains("destination"))
					Window.Location.reload();
			}
		});
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
	
	public void handlerUploadEvent() {
		if(!isHandlerUploadEvent) {
			handleFileSelect();
			isHandlerUploadEvent = true;
		}
	}
	
	public static native void handleFileSelect() /*-{
  		function handleFileSelect(evt) {
  			var imageTable = $wnd.document.getElementById('imageTable');
  			imageTable.innerHTML = "";
  			
    		var files = evt.target.files; // FileList object
    		var countImg = 0;

		    // Loop through the FileList and render image files as thumbnails.
		    for (var i = 0, f; f = files[i]; i++) {
		
		      // Only process image files.
		      if (!f.type.match('image.*')) {
		        continue;
		      }
		
			  countImg = countImg + 1;
		      var reader = new FileReader();
		
		      // Closure to capture the file information.
		      reader.onload = (function(theFile) {
		        return function(e) {
		          // Render thumbnail.
		          var span = document.createElement('span');
		          span.innerHTML = ['<img class="PhotoUpload-Obj11" src="', e.target.result,
		                            '" title="', escape(theFile.name), '"/>'].join('');
		          $wnd.document.getElementById('imageTable').insertBefore(span, null);
		        };
		      })(f);
		
		      // Read in the image file as a data URL.
		      reader.readAsDataURL(f);
		    }
		    
		    var insertImg = document.createElement('label');
 			insertImg.className = "PhotoUpload-Obj10";
 			insertImg.style.height = "100px";
 			insertImg.style.width = "100px";
 			insertImg.setAttribute("for", "fileUpload");
 			insertImg.innerHTML = ['<i style="margin-top: 19px;margin-left: 24px;" class="fa fa-plus fa-5x"></i>'].join('');
 			imageTable.insertBefore(insertImg, null);
 			
 			$wnd.document.getElementById('lbPhotosCount').innerHTML = countImg + " / Photos";
		 }

		$wnd.document.getElementById('fileUpload').addEventListener('change', handleFileSelect, false);
			
	}-*/;
	
	public void uploadFor(Long tripId, Long pathId) {
		boxTripId.setValue(tripId);
		boxPathId.setValue(pathId);
	}
	
	void cancelUpload() {
		imgUploading.setVisible(false);
		formUpload.reset();
		imageTable.clear();
		imageTable.getElement().setInnerHTML("<label for='fileUpload' style='width:100px; height:100px' class='PhotoUpload-Obj10'><i style='margin-top: 19px;margin-left: 24px;' class='fa fa-plus fa-5x'></i></label>");
		lbPhotosCount.setText("0 / Photos");
		hide();
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		cancelUpload();
	}
	
	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		imgUploading.setVisible(true);
		TripShare.dataService.getUploadUrl(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				formUpload.setAction(result.toString());
				formUpload.submit();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
