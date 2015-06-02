package com.born2go.client.widgets;

import java.util.Date;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Locate;
import com.born2go.shared.Path;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class Journey_PathCreate extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);
	
	@UiField HTMLPanel htmlPathCreate;
	@UiField TextBox txbTitle;
	@UiField DateBox txbTimeline;
	@UiField Anchor btnPost;
	@UiField StretchyTextArea txbDescription;
	@UiField Anchor btnCancel;
	@UiField HTMLPanel htmlPathPhotos;
	@UiField ScrollPanel scrollPathPhotos;
	@UiField Label lbCountPhotos;
	@UiField HTMLPanel editTextBox;
	@UiField ListBox lbPostTo;
	@UiField Anchor btnRichTextEdit;
	@UiField Anchor btnFindYourLocation;
	@UiField Label lbTitle;
	@UiField HTMLPanel container;
	@UiField Anchor pick_files;
	
	Long tripId;
	Long pathID;
	Locate locate;
	
	boolean isRichTextEdit = false;
	
	interface Binder extends UiBinder<Widget, Journey_PathCreate> {
	}

	public interface Listener {
		void onClose();
		
		void createPathSuccess(Long newPathId);
	}
	
	private Listener listener;
	private Path updatePath;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	TextArea txbRichDescription;
	
//	public CKConfig getCKConfig() {
//		// Creates a new config, with FULL preset toolbar as default
//		CKConfig ckf = new CKConfig();
//		
//		// Setting background color
//		ckf.setUiColor("#f6f7f8");
//		
//		// Setting size
//		ckf.setWidth("");
//		ckf.setHeight("180px");
//		ckf.setResizeMinHeight(180);
//		ckf.setResizeMaxHeight(250);
//
//		// Creating personalized toolbar
//		ToolbarLine line1 = new ToolbarLine();
//		line1.add(TOOLBAR_OPTIONS.Source);
//		line1.add(TOOLBAR_OPTIONS._);
//		line1.add(TOOLBAR_OPTIONS.Undo);
//		line1.add(TOOLBAR_OPTIONS.Redo);
//
//		// Creates the toolbar
//		Toolbar t = new Toolbar();
//		t.add(line1);
//		t.addSeparator();
//
//		// Define the second line
//		TOOLBAR_OPTIONS[] t2 = { TOOLBAR_OPTIONS.Bold, TOOLBAR_OPTIONS.Italic,
//				TOOLBAR_OPTIONS.Underline, TOOLBAR_OPTIONS._,
//				TOOLBAR_OPTIONS.RemoveFormat, TOOLBAR_OPTIONS._ };
//		ToolbarLine line2 = new ToolbarLine();
//		line2.addAll(t2);
//		t.add(line2);
//		t.addSeparator();
//
//		// Define the third line
//		TOOLBAR_OPTIONS[] t3 = { TOOLBAR_OPTIONS.Find, TOOLBAR_OPTIONS.Replace, };
//		ToolbarLine line3 = new ToolbarLine();
//		line3.addAll(t3);
//		t.add(line3);
//		t.addSeparator();
//
//		// Define the second line
//		TOOLBAR_OPTIONS[] t4 = {
//		TOOLBAR_OPTIONS.JustifyLeft, TOOLBAR_OPTIONS.JustifyCenter,
//				TOOLBAR_OPTIONS.JustifyRight, TOOLBAR_OPTIONS.JustifyBlock };
//		
//		ToolbarLine line4 = new ToolbarLine();
//		line4.addAll(t4);
//		t.add(line4);
//		t.addSeparator();
//
//		ToolbarLine line5 = new ToolbarLine();
//		line5.add(TOOLBAR_OPTIONS.Link);
//		line5.add(TOOLBAR_OPTIONS.Unlink);
//		t.add(line5);
//		t.addSeparator();
//
//		ToolbarLine line6 = new ToolbarLine();
//		line6.add(TOOLBAR_OPTIONS.Image);
//		line6.add(TOOLBAR_OPTIONS.Smiley);
//		t.add(line6);
//		t.addSeparator();
//
//		ToolbarLine line7 = new ToolbarLine();
//		line7.add(TOOLBAR_OPTIONS.Styles);
//		t.add(line7);
//
//		ToolbarLine line8 = new ToolbarLine();
//		line8.add(TOOLBAR_OPTIONS.Format);
//		t.add(line8);
//		t.addSeparator();
//
//		ToolbarLine line9 = new ToolbarLine();
//		line9.add(TOOLBAR_OPTIONS.Font);
//		t.add(line9);
//		t.addSeparator();
//
//		ToolbarLine line10 = new ToolbarLine();
//		line10.add(TOOLBAR_OPTIONS.FontSize);
//		t.add(line10);
//		t.addSeparator();
//
//		ToolbarLine line11 = new ToolbarLine();
//		line11.add(TOOLBAR_OPTIONS.TextColor);
////		line11.add(TOOLBAR_OPTIONS.SpecialChar);
//		line11.add(TOOLBAR_OPTIONS.Maximize);
//		t.add(line11);
//		t.addSeparator();
//		
//		// Set the toolbar to the config (replace the FULL preset toolbar)
//		ckf.setToolbar(t);
//		return ckf;
//	}

	public Journey_PathCreate() {
		initWidget(uiBinder.createAndBindUi(this));
		lbPostTo.addItem("new");

		htmlPathPhotos.getElement().setAttribute("id", "htmlPathPhotos");
		scrollPathPhotos.getElement().setAttribute("id", "scrollPathPhotos");
		lbCountPhotos.getElement().setAttribute("id", "lbCountPhotos");
		
		container.getElement().setAttribute("id", "post_container");
		pick_files.getElement().setAttribute("id", "post_pick_files");
		
		txbDescription.getElement().setPropertyString("placeholder", "Write your feeling now, or about the story of your best memory on the journey!");
		txbDescription.getElement().setAttribute("spellcheck", "false");
		
		btnFindYourLocation.addStyleName("PathView-Obj14");
		
//		final Autocomplete autocomplete = Autocomplete.create(
//				(InputElement) (Element) txbTitle.getElement());
//		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
//			public void handle() {
//				PlaceResult place = autocomplete.getPlace();
//				locate.setLatLng(place.getGeometry().getLocation());
//			}
//		});
	}
	
	public void getListPaths(final List<Path> list_paths) {
		lbPostTo.clear();
		lbPostTo.addItem("new");
		for(Path path: list_paths) {
			lbPostTo.addItem(path.getTitle());
		}
		lbPostTo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(lbPostTo.getSelectedIndex() == 0) {
					updatePath = null;
					txbTitle.setText("");
					txbTimeline.setEnabled(true);
				}
				else {
					updatePath = list_paths.get(lbPostTo.getSelectedIndex() - 1);
					txbTitle.setText(updatePath.getTitle());
					txbTimeline.setEnabled(false);
				}
			}
		});
	}
	
	public void handlerUploadEvent() {
		getPlupLoad(this);
	}
	
	public void setTripId(Long tripId) {
		this.tripId = tripId;
		txbTimeline.setValue(new Date());
		txbTitle.setFocus(true);
	}

	public void uploadPhoto(final Long tripId, final Long pathId) {
		TripShare.dataService.getUploadUrl(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				pathID = pathId;
				String trip_id = (tripId != null ? tripId.toString() : "");
				String path_id = (pathId != null ? pathId.toString() : "");
				updatePlupLoad(trip_id, path_id);
				startPlupLoad(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				TripShare.loadBox.hide();
				Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
			}
		});
	}
	
	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		if(VerifiedField()) {
			TripShare.loadBox.center();
			if(updatePath == null) {
				Path path = new Path();
				path.setTitle(txbTitle.getText());
	//			path.setLocate(locate);
				path.setCreateDate(txbTimeline.getValue());
				if(!isRichTextEdit) {
//					if(txbRichDescription != null) {
//						txbRichDescription.setText(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
//						path.setDescription(txbRichDescription.getText());
//					} else 
						path.setDescription("<p>"+ txbDescription.getText().replaceAll("(\r\n|\n)", "<br />")+ "</p>");
				}
				else
					path.setDescription(getDataCustomEditor());
				TripShare.dataService.insertPart(path, tripId, TripShare.access_token, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(Path result) {
						if(result != null)
							uploadPhoto(tripId, result.getId());
						else {
							TripShare.loadBox.hide();
							Window.alert(TripShare.ERROR_MESSAGE);
						}		
					}
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert(TripShare.ERROR_MESSAGE);
					}
				});
			} 
			else {
				updatePath.setTitle(txbTitle.getText());
				if(!isRichTextEdit) {
//					if(txbRichDescription != null) {
//						txbRichDescription.setText(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
//						updatePath.setDescription(updatePath.getDescription()+ "<p>"+ txbRichDescription.getText()+ "</p>");
//					} else 
						updatePath.setDescription(updatePath.getDescription()+ "<p>"+ txbDescription.getText().replaceAll("(\r\n|\n)", "<br />")+ "</p>");
				}
				else
					updatePath.setDescription(updatePath.getDescription()+ "<p>"+ getDataCustomEditor()+ "</p>");
				TripShare.dataService.updatePart(updatePath, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(Path result) {
						if(result != null)
							uploadPhoto(tripId, result.getId());
						else {
							TripShare.loadBox.hide();
							Window.alert(TripShare.ERROR_MESSAGE);
						}	
					}
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert(TripShare.ERROR_MESSAGE);
					}
				});
			}
		}
	}
	
	void handlerUploadComplete() {
		TripShare.loadBox.hide();
		cancelPost();
		if(listener != null)
			listener.createPathSuccess(pathID);
	}
	
	public void cancelPost() {
		this.setStyleName("PathCreate-Obj3");
		lbPostTo.setSelectedIndex(0);
		updatePath = null;
		txbTitle.setText("");
		lbTitle.setStyleName("font-blackTitleNormal");
		txbTimeline.setEnabled(true);
		txbTitle.setText("");
		txbTimeline.getElement().setInnerHTML("");
		btnRichTextEdit.removeStyleName("PathCreate-Obj16");
		txbDescription.setText("");
		txbDescription.setVisible(true);
		if(txbRichDescription != null) {
			if(DOM.getElementById("cke_txbRichDescription") != null)
				DOM.getElementById("cke_txbRichDescription").setAttribute("style", "display:none");
			destroyCustomEditor();
			txbRichDescription.setText("");
			txbRichDescription.setVisible(false);
		}
		isRichTextEdit = false;
		scrollPathPhotos.setHeight("0px");
		htmlPathPhotos.getElement().setInnerHTML("");
		lbCountPhotos.setText("0 / Photos");
		removePlupLoad();
		locate = null;
		if(listener != null)
			listener.onClose();
	}

	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		cancelPost();
	}
	
//	@UiHandler("btnFindYourLocation") 
//	void onBtnFindYourLocationClick(ClickEvent event) {
//		Geolocation geoLocation = Geolocation.getIfSupported();
//		if (geoLocation == null) {
//			Window.alert("!: Your old browser not support GeoLocation");
//		} else {
//			geoLocation.getCurrentPosition(new com.google.gwt.core.client.Callback<Position, PositionError>() {
//				
//				@Override
//				public void onSuccess(Position result) {
//					final LatLng l = LatLng.create(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
//					GeocoderRequest geoRequest = GeocoderRequest.create();
//					geoRequest.setLocation(l);
//					Geocoder geoCode = Geocoder.create();
//					geoCode.geocode(geoRequest, new Geocoder.Callback() {
//						@Override
//						public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
//							String address = a.get(0).getFormattedAddress();
//							txbTitle.setText(address);
//							locate.setLatLng(l);
//						}
//					});
//				}
//				
//				@Override
//				public void onFailure(PositionError reason) {
//					Window.alert("!: Can't find your location");
//				}
//			});
//		}
//	}
	
	@UiHandler("btnRichTextEdit") 
	void onBtnRichTextEditClick(ClickEvent event) {
		if(isRichTextEdit) {
			if(getDataCustomEditor().length() != 0) {
				if(Window.confirm("!: Rich text can't convert to normal text, if you continue the text will be clear.")) {
					txbRichDescription.setVisible(false);
					if(DOM.getElementById("cke_txbRichDescription") != null)
						DOM.getElementById("cke_txbRichDescription").setAttribute("style", "display:none");
					txbDescription.setVisible(true);
					btnRichTextEdit.removeStyleName("PathCreate-Obj16");
					txbDescription.setText("");
					isRichTextEdit = !isRichTextEdit;
				}
			} else {
				txbRichDescription.setVisible(false);
				if(DOM.getElementById("cke_txbRichDescription") != null)
					DOM.getElementById("cke_txbRichDescription").setAttribute("style", "display:none");
				txbDescription.setVisible(true);
				btnRichTextEdit.removeStyleName("PathCreate-Obj16");
				isRichTextEdit = !isRichTextEdit;
			}
		}
		else {
			if(txbRichDescription == null) {
				txbRichDescription = new TextArea();
				txbRichDescription.getElement().setAttribute("id", "txbRichDescription");
				editTextBox.add(txbRichDescription);	
			}
			else {
				if(DOM.getElementById("cke_txbRichDescription") != null)
					DOM.getElementById("cke_txbRichDescription").setAttribute("style", "display:");
			}
			addCustomEditor();
//			txbRichDescription.setVisible(true);
			txbDescription.setVisible(false);
			btnRichTextEdit.addStyleName("PathCreate-Obj16");
			String transferText = txbDescription.getText().replaceAll("(\r\n|\n)", "<br />");
			setDataCustomEditor(transferText);
			isRichTextEdit = !isRichTextEdit;
		}
	}
	
	public static native void addCustomEditor() /*-{
		var cke = $wnd.document.getElementById("cke_txbRichDescription");
		if (cke == null) {
			$wnd.CKEDITOR.replace( 'txbRichDescription', {
	    		customConfig: '/resources/ckeditor/custom_config.js'
			});
		}
	}-*/;
	
	public static native void setDataCustomEditor(String data) /*-{
		var cke = $wnd.document.getElementById("cke_txbRichDescription");
		if(cke != null) {
			var d = data;
			$wnd.CKEDITOR.instances.txbRichDescription.setData(d);
		}
	}-*/;
	
	public static native String getDataCustomEditor() /*-{
		var cke = $wnd.document.getElementById("cke_txbRichDescription");
		if(cke != null) {
			var data = $wnd.CKEDITOR.instances.txbRichDescription.getData();
			return data;
		}
		else
			return "";
	}-*/;
	
	public static native void destroyCustomEditor() /*-{
		var cke = $wnd.document.getElementById("cke_txbRichDescription");
		if(cke != null) {
			$wnd.CKEDITOR.instances.txbRichDescription.destroy();
		}
	}-*/;
	
	public static native void startPlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		$wnd.uploader.settings.url = upload_url;
		$wnd.uploader.start();
	}-*/;
	
	public static native void updatePlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		$wnd.uploader.settings.url = upload_url;
	}-*/;
	
	public static native void updatePlupLoad(String par1, String par2) /*-{
		var trip_id = par1;
		var path_id = par2;
		$wnd.uploader.settings.multipart_params.tripId = trip_id;
		$wnd.uploader.settings.multipart_params.pathId = path_id;
	}-*/;
	
	public static native void removePlupLoad() /*-{
		if($wnd.uploader != null)
			$wnd.uploader.destroy();
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
	
	public static native void getPlupLoad(Journey_PathCreate instance) /*-{
		var files_remaining;          
	  	$wnd.uploader = new $wnd.plupload.Uploader({
		    runtimes : 'flash',
		    container: $wnd.document.getElementById('post_container'), // ... or DOM Element itself
		    browse_button : 'post_pick_files', // you can pass in id...
	//	    drop_element: 'dropzone',
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
			    		@com.born2go.client.widgets.Journey_PathCreate::updateUploaderUrl()();
	            }
	        },
		 
		    init: {
		        PostInit: function() {			        
		        },
		        
		        QueueChanged: function(up) {
	            	files_remaining = $wnd.uploader.files.length;
	            	$wnd.document.getElementById('lbCountPhotos').innerHTML = files_remaining + " / Photos";
	            	var scrollPathPhotos = $wnd.document.getElementById('scrollPathPhotos');
		    		if(files_remaining > 0) {
		    			scrollPathPhotos.style.height = "100px";
		    		}
				    else {
				    	scrollPathPhotos.style.height = "0px";
				    } 	
	        	},
		 
		        FilesAdded: function(up, files) {
					   $wnd.plupload.each(files, function(file) {
						var img = new $wnd.o.Image();
						                  
	                    img.onload = function() {
	                        // create a thumb placeholder
	                        var span = document.createElement('div');
	                        span.id = this.uid;	                      
	                        span.className = "PathCreate-Obj19";                       
	                        $wnd.document.getElementById('htmlPathPhotos').insertBefore(span, null);
	                        	                     	                  
	                        // embed the actual thumbnail
	                        var heightcrop = 80;
	                        var widthcrop = heightcrop * (this.width/this.height);
	                        this.embed(span.id, {	  
	                            width: widthcrop,
	                            height: heightcrop,
	                            crop: true
	                        });	                                               	                       	                  
	                    };
	                    
	                    // drop thumbnails at different angles (optional eye candy)
	                    img.onembedded = function() {
//                        	$wnd.plupload.each(['', '-ms-', '-webkit-', '-o-', '-moz-'], function(prefix) {
//                            	$wnd.document.getElementById(img.uid).style[prefix + 'transform'] = 'rotate('+ (Math.floor(Math.random() * 6) - 3) + 'deg)';
//                        	});
							// add remove image button
	                        var removeImg = document.createElement("a");
	                        var span = 	$wnd.document.getElementById(this.uid);
	                        span.appendChild(removeImg);
	                        removeImg.className = "greenbutton PathCreate-Obj20";
	                        removeImg.innerHTML = "<i class='fa fa-times'></i>";
	                        removeImg.onclick = function(index) {
	                        	span.parentNode.removeChild(span);
	                        	$wnd.uploader.removeFile(file);
	                        };
	                    };
	                    
	                    img.load(file.getSource());
				  });
		        },		      	
		 
//		        UploadProgress: function(up, file) {
//		        	var total_files =  $wnd.uploader.files.length;
//		        	var files_uploaded = total_files - files_remaining;
//		        	var total_percent = ((file.percent/100 * 1/total_files)*100) + ((files_uploaded * 1/total_files)*100);
//		            $wnd.document.getElementById('lbUploadProgress').innerHTML = '<span>' + Math.ceil(total_percent) + "%</span>";
//		        },
		        
		        FileUploaded: function(up, file, info) {
	                files_remaining--;
	            },
	            
	            UploadComplete: function(up, files) {          	
	         		instance.@com.born2go.client.widgets.Journey_PathCreate::handlerUploadComplete()();	
	            },
		 
		        Error: function(up, err) {
		            $wnd.document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
		        }
		    }
		});
		 
		$wnd.uploader.init();
	}-*/;
	
	boolean VerifiedField() {
		boolean isFieldComplete = true;
//		if(txbTitle.getText().isEmpty()) {
//			lbTitle.setStyleName("font-redTitleNormal");
//			txbTitle.setFocus(true);
//			isFieldComplete = false;
//		} 
		return isFieldComplete;
	}
	
}
