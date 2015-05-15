package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
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
import com.google.gwt.user.client.ui.Anchor;
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
	@UiField Anchor pick_files;
	@UiField Label lbUploadProgress;
	
	boolean isHandlerUploadEvent = false;

	interface Binder extends UiBinder<Widget, PhotoUpload> {
	}
	
	public PhotoUpload() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("PhotoUploadDialog");

		setGlassEnabled(true);
		setAnimationEnabled(true);
		
//		formUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
//		formUpload.setMethod(FormPanel.METHOD_POST);
		
		fileUpload.setName("filesUpload");
		boxTripId.setName("tripId");
		boxPathId.setName("pathId");
	
		fileUpload.getElement().setAttribute("id", "fileUpload");
		imageTable.getElement().setAttribute("id", "container");
		lbPhotosCount.getElement().setAttribute("id", "lbPhotosCount");
		pick_files.getElement().setAttribute("id", "pick_files");
		lbUploadProgress.getElement().setAttribute("id", "lbUploadProgress");
		
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
		getPlupLoad();
//		if(!isHandlerUploadEvent) {
//			handleFileSelect();
//			isHandlerUploadEvent = true;
//		}
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
		          span.innerHTML = ['<img class="PhotoUpload-Obj11" src="', e.target.result,'" title="', escape(theFile.name), '"/>'].join('');
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
				startPlupLoad(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static native void startPlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		$wnd.uploader.settings.url = upload_url;
		$wnd.uploader.start();
	}-*/;
	
	public static native void updatePlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		$wnd.uploader.settings.url = upload_url;
	}-*/;
	
	public static void updateUploaderUrl() {
		TripShare.dataService.getUploadUrl(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				updatePlupLoad(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("!: Failed to get blob service upload url.");
			}
		});
	};
	
	public static native void getPlupLoad() /*-{
		var files_remaining;          
	  	$wnd.uploader = new $wnd.plupload.Uploader({
		    runtimes : 'flash',
		    container: $wnd.document.getElementById('container'), // ... or DOM Element itself
		    browse_button : 'pick_files', // you can pass in id...
		    url : '/',
		    use_query_string: false,
		    dragdrop: true,
		     
		    filters : {
		        max_file_size : '5mb',
		        mime_types: [
		            {title : "Image files", extensions : "jpg,png"}  
		        ]
		    },
		 
		    // Flash settings
		    flash_swf_url : '/resources/plupload/Moxie.swf',
		 
		    // Silverlight settings
//		    silverlight_xap_url : '/plupload/js/Moxie.xap',
		    
		    // PreInit events, bound before any internal events
	        preinit : {
	            Init: function(up, info) {
	            },
	 
	            UploadFile: function(up, file) {
	            	if(files_remaining > 1)
			    		@com.born2go.client.widgets.PhotoUpload::updateUploaderUrl()();
	            }
	        },
		 
		    init: {
		        PostInit: function() {			        
		        },
		        
		        QueueChanged: function(up) {
	            	files_remaining = $wnd.uploader.files.length;
	            	$wnd.document.getElementById('lbPhotosCount').innerHTML = files_remaining + " / Photos";
	        	},
		 
		        FilesAdded: function(up, files) {
 				   $wnd.plupload.each(files, function(file) {
						var img = new $wnd.o.Image();
						                  
	                    img.onload = function() {
	                        // create a thumb placeholder
	                        var span = document.createElement('span');
	                        span.id = this.uid;	                      
	                        span.className = "PhotoUpload-Obj11";                       
	                        $wnd.document.getElementById('container').insertBefore(span, null);
	                     
	                        // embed the actual thumbnail
	                        var widthcrop = 300;
	                        var heightcrop = widthcrop / (this.width/this.height);
	                        this.embed(span.id, {	  
	                            width: widthcrop,
	                            height: heightcrop,
	                            crop: true
	                        });
	                    };
	                    
	                    // drop thumbnails at different angles (optional eye candy)
	                    img.onembedded = function() {
	                        $wnd.plupload.each(['', '-ms-', '-webkit-', '-o-', '-moz-'], function(prefix) {
	                            $wnd.document.getElementById(img.uid).style[prefix + 'transform'] = 'rotate('+ (Math.floor(Math.random() * 6) - 3) + 'deg)';
	                        });
	                    };
	                    
	                    img.load(file.getSource());
				  });
		        },		      	
		 
		        UploadProgress: function(up, file) {
		            $wnd.document.getElementById('lbUploadProgress').innerHTML = '<span>' + file.percent + "%</span>";
		        },
		        
		        FileUploaded: function(up, file, info) {
	                files_remaining--;
	            },
		 
		        Error: function(up, err) {
		            $wnd.document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
		        }
		    }
		});
		 
		$wnd.uploader.init();
	}-*/;
}
