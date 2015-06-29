package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PhotoUpload extends DialogBox {

	private static Binder uiBinder = GWT.create(Binder.class);
	
	@UiField HTMLPanel container;
	@UiField FormPanel formUpload;
	@UiField Label lbPhotosCount;
	@UiField Image imgUploading;
	@UiField Anchor pick_files;
	@UiField Label lbUploadProgress;
	@UiField HorizontalPanel uploadingForm;
	@UiField HTMLPanel imageTable;
//	@UiField HTMLPanel dropzone;
	
//	boolean isHandlerUploadEvent = false;

	interface Binder extends UiBinder<Widget, PhotoUpload> {
	}
	
	public PhotoUpload() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("PhotoUploadDialog");

		setGlassEnabled(true);
		setAnimationEnabled(true);
	
		container.getElement().setAttribute("id", "container");
		lbPhotosCount.getElement().setAttribute("id", "lbPhotosCount");
		pick_files.getElement().setAttribute("id", "pick_files");
		lbUploadProgress.getElement().setAttribute("id", "lbUploadProgress");
		imageTable.getElement().setAttribute("id", "imageTable");
//		dropzone.getElement().setAttribute("id", "dropzone");
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
	
	public void handlerUploadEvent(Long tripId, Long pathId) {
		HTML not_support_flash_warning = new HTML("Your browser need Flash to upload <a target='_blank' href='https://get.adobe.com/flashplayer/'>Download Flash here!</a>");
		not_support_flash_warning.getElement().setAttribute("style", "margin-top:15px");
		imageTable.add(not_support_flash_warning);
		getPlupLoad(this);
		String trip_id = (tripId != null ? tripId.toString() : "");
		String path_id = (pathId != null ? pathId.toString() : "");
		updatePlupLoad(trip_id, path_id);
	}
	
	void cancelUpload() {
		uploadingForm.setVisible(false);
		removePlupLoad();
		imageTable.getElement().setInnerHTML("");
		lbPhotosCount.setText("0 / Photos");
		lbUploadProgress.setText("0%");
//		hide();
		Window.Location.reload();
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		uploadingForm.setVisible(false);
		removePlupLoad();
		imageTable.getElement().setInnerHTML("");
		lbPhotosCount.setText("0 / Photos");
		lbUploadProgress.setText("0%");
		hide();
	}
	
	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		uploadingForm.setVisible(true);
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
		if($wnd.uploader != null) {
			$wnd.uploader.settings.url = upload_url;
			$wnd.uploader.start();
		}
	}-*/;
	
	public static native void updatePlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		if($wnd.uploader != null) {
			$wnd.uploader.settings.url = upload_url;
		}
	}-*/;
	
	public static native void updatePlupLoad(String par1, String par2) /*-{
		var trip_id = par1;
		var path_id = par2;
		if($wnd.uploader != null) {
			$wnd.uploader.settings.multipart_params.tripId = trip_id;
			$wnd.uploader.settings.multipart_params.pathId = path_id;
		}
	}-*/;
	
	public static native void removePlupLoad() /*-{
		if($wnd.uploader != null) {
			$wnd.uploader.destroy();
		}
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
	
	public static native void getPlupLoad(PhotoUpload instance) /*-{
		var files_remaining;          
	  	$wnd.uploader = new $wnd.plupload.Uploader({
		    runtimes : 'flash',
		    container: $wnd.document.getElementById('container'), // ... or DOM Element itself
		    browse_button : 'pick_files', // you can pass in id...
//		    drop_element: 'dropzone',
		    url : '/',
		    use_query_string: false,
		   	dragdrop: true,
		    multipart : true,
		    
		    //Enable resize
		    resize: {
    			width: 1366,
    			height: 768
  			},
		    
		    //Enable params
		    multipart_params : {
    			tripId: '',
    			pathId: ''
    		},
		     
		    //Enable filter files
		    filters : {
		        max_file_size : '5mb',
		        mime_types: [
		            {title : "Image files", extensions : "jpg,png"}  
		        ]
		    },
		 
		    // Flash settings
		    flash_swf_url : '/resources/plupload/Moxie.swf',
		    
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
		            $wnd.document.getElementById('imageTable').innerHTML = '';
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
	                        var span = document.createElement('div');
	                        span.id = this.uid;	                      
	                        span.className = "PhotoUpload-Obj11";                       
	                        $wnd.document.getElementById('imageTable').insertBefore(span, null);
	                        	                     	                  
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
	                            $wnd.document.getElementById(img.uid).style[prefix + 'transform'] = 'rotate('+ (Math.floor(Math.random() * 4) - 2) + 'deg)';
	                        });
 							// add remove image button
	                        var removeImg = document.createElement("a");
	                        var span = 	$wnd.document.getElementById(this.uid);
	                        span.appendChild(removeImg);
	                        removeImg.className = "greenbutton PhotoUpload-Obj15";
	                        removeImg.innerHTML = "<i class='fa fa-times'></i>";
	                        removeImg.onclick = function(index) {
	                        	span.parentNode.removeChild(span);
	                        	$wnd.uploader.removeFile(file);
	                        };
	                    };
	                    
	                    img.load(file.getSource());
				  });
		        },		      	
		 
		        UploadProgress: function(up, file) {
		        	var total_files =  $wnd.uploader.files.length;
		        	var files_uploaded = total_files - files_remaining;
		        	var total_percent = ((file.percent/100 * 1/total_files)*100) + ((files_uploaded * 1/total_files)*100);
		            $wnd.document.getElementById('lbUploadProgress').innerHTML = '<span>' + Math.ceil(total_percent) + "%</span>";
		        },
		        
		        FileUploaded: function(up, file, info) {
	                files_remaining--;
	            },
	            
	            UploadComplete: function(up, files) {          	
             		instance.@com.born2go.client.widgets.PhotoUpload::cancelUpload()();	
	            },
		 
		        Error: function(up, err) {
		            $wnd.document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
		        }
		    }
		});
		 
		$wnd.uploader.init();
	}-*/;
}
