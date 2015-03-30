package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PhotoUpload extends DialogBox {

	private static Binder uiBinder = GWT.create(Binder.class);
	
	@UiField FileUpload fileUpload;
	@UiField HTMLPanel imageTable;

	interface Binder extends UiBinder<Widget, PhotoUpload> {
	}
	
	public PhotoUpload() {
		setWidget(uiBinder.createAndBindUi(this));

		setGlassEnabled(true);
		setAnimationEnabled(true);
	
		fileUpload.getElement().setAttribute("id", "fileUpload");
		imageTable.getElement().setAttribute("id", "imageTable");
		
		DOM.setElementProperty(fileUpload.getElement(), "multiple", "multiple"); 
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
		handleFileSelect();
	}
	
	public static native void handleFileSelect() /*-{
  		function handleFileSelect(evt) {
    		var files = evt.target.files; // FileList object

		    // Loop through the FileList and render image files as thumbnails.
		    for (var i = 0, f; f = files[i]; i++) {
		
		      // Only process image files.
		      if (!f.type.match('image.*')) {
		        continue;
		      }
		
		      var reader = new FileReader();
		
		      // Closure to capture the file information.
		      reader.onload = (function(theFile) {
		        return function(e) {
		          // Render thumbnail.
		          var span = document.createElement('span');
		          span.innerHTML = ['<img class="PhotoUpload-Obj3" src="', e.target.result,
		                            '" title="', escape(theFile.name), '"/>'].join('');
		          $wnd.document.getElementById('imageTable').insertBefore(span, null);
		        };
		      })(f);
		
		      // Read in the image file as a data URL.
		      reader.readAsDataURL(f);
		    }
		 }

		$wnd.document.getElementById('fileUpload').addEventListener('change', handleFileSelect, false);
			
	}-*/;
	
}
