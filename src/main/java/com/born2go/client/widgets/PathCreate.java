package com.born2go.client.widgets;

import java.util.Date;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.born2go.client.TripShare;
import com.born2go.shared.Locate;
import com.born2go.shared.Path;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class PathCreate extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);
	
	@UiField HTMLPanel container;
	@UiField TextBox txbLocation;
	@UiField DateBox txbTimeline;
	@UiField Anchor btnPost;
	@UiField StretchyTextArea txbDescription;
	@UiField Anchor btnCancel;
	@UiField FormPanel formUpload;
	@UiField FileUpload pathPhotoUpload;
	@UiField HTMLPanel htmlPathPhotos;
	@UiField LongBox boxTripId;
	@UiField LongBox boxPathId;
	@UiField ScrollPanel scrollPathPhotos;
	@UiField Label lbCountPhotos;
	@UiField HTMLPanel editTextBox;
	@UiField ListBox lbPostTo;
	@UiField Anchor btnRichTextEdit;
	
	Long tripId;
	Locate locate;
	boolean isHandlerUploadEvent = false;
	boolean isRichTextEdit = false;
	
	interface Binder extends UiBinder<Widget, PathCreate> {
	}

	public interface Listener {
		void onClose();
		
		void createPathSuccess(Long newPathId);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	CKEditor txbRichDescription;
	
	public CKConfig getCKConfig() {
		// Creates a new config, with FULL preset toolbar as default
		CKConfig ckf = new CKConfig();
		
		// Setting background color
		ckf.setUiColor("#f6f7f8");
		
		// Setting size
		ckf.setWidth("");
		ckf.setHeight("180px");
		ckf.setResizeMaxHeight(250);

		// Creating personalized toolbar
		ToolbarLine line1 = new ToolbarLine();
		line1.add(TOOLBAR_OPTIONS.Source);
		line1.add(TOOLBAR_OPTIONS._);
		line1.add(TOOLBAR_OPTIONS.Undo);
		line1.add(TOOLBAR_OPTIONS.Redo);

		// Creates the toolbar
		Toolbar t = new Toolbar();
		t.add(line1);
		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t2 = { TOOLBAR_OPTIONS.Bold, TOOLBAR_OPTIONS.Italic,
				TOOLBAR_OPTIONS.Underline, TOOLBAR_OPTIONS._,
				TOOLBAR_OPTIONS.RemoveFormat, TOOLBAR_OPTIONS._ };
		ToolbarLine line2 = new ToolbarLine();
		line2.addAll(t2);
		t.add(line2);
		t.addSeparator();

		// Define the third line
		TOOLBAR_OPTIONS[] t3 = { TOOLBAR_OPTIONS.Find, TOOLBAR_OPTIONS.Replace, };
		ToolbarLine line3 = new ToolbarLine();
		line3.addAll(t3);
		t.add(line3);
		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t4 = {
		TOOLBAR_OPTIONS.JustifyLeft, TOOLBAR_OPTIONS.JustifyCenter,
				TOOLBAR_OPTIONS.JustifyRight, TOOLBAR_OPTIONS.JustifyBlock };
		
		ToolbarLine line4 = new ToolbarLine();
		line4.addAll(t4);
		t.add(line4);
		t.addSeparator();

		ToolbarLine line5 = new ToolbarLine();
		line5.add(TOOLBAR_OPTIONS.Link);
		line5.add(TOOLBAR_OPTIONS.Unlink);
		t.add(line5);
		t.addSeparator();

		ToolbarLine line6 = new ToolbarLine();
		line6.add(TOOLBAR_OPTIONS.Image);
		line6.add(TOOLBAR_OPTIONS.Smiley);
		t.add(line6);
		t.addSeparator();

		ToolbarLine line7 = new ToolbarLine();
		line7.add(TOOLBAR_OPTIONS.Styles);
		t.add(line7);

		ToolbarLine line8 = new ToolbarLine();
		line8.add(TOOLBAR_OPTIONS.Format);
		t.add(line8);
		t.addSeparator();

		ToolbarLine line9 = new ToolbarLine();
		line9.add(TOOLBAR_OPTIONS.Font);
		t.add(line9);
		t.addSeparator();

		ToolbarLine line10 = new ToolbarLine();
		line10.add(TOOLBAR_OPTIONS.FontSize);
		t.add(line10);
		t.addSeparator();

		ToolbarLine line11 = new ToolbarLine();
		line11.add(TOOLBAR_OPTIONS.TextColor);
//		line11.add(TOOLBAR_OPTIONS.SpecialChar);
		line11.add(TOOLBAR_OPTIONS.Maximize);
		t.add(line11);
		t.addSeparator();
		
		// Set the toolbar to the config (replace the FULL preset toolbar)
		ckf.setToolbar(t);
		return ckf;
	}
	
//	public void reAddCKEditor() {
//		String saveText = txbRichDescription.getData();
//		txbRichDescription.removeFromParent();
//		txbRichDescription = null;
//		txbRichDescription = new CKEditor(getCKConfig());
//		txbRichDescription.setData(saveText);
//		editTextBox.add(txbRichDescription);
//		if(!isRichTextEdit)
//			txbRichDescription.setVisible(false);
//	}

	public PathCreate() {
		initWidget(uiBinder.createAndBindUi(this));
//		this.setVisible(false);
		lbPostTo.addItem("new");
//		txbRichDescription = new CKEditor(getCKConfig());
//		editTextBox.add(txbRichDescription);
//		txbRichDescription.setVisible(false);
		
		formUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
		formUpload.setMethod(FormPanel.METHOD_POST);
		
		pathPhotoUpload.setName("filesUpload");
		boxTripId.setName("tripId");
		boxPathId.setName("pathId");
		
		formUpload.getElement().setAttribute("id", "formUpload");
		pathPhotoUpload.getElement().setAttribute("id", "pathPhotoUpload");
		htmlPathPhotos.getElement().setAttribute("id", "htmlPathPhotos");
		scrollPathPhotos.getElement().setAttribute("id", "scrollPathPhotos");
		lbCountPhotos.getElement().setAttribute("id", "lbCountPhotos");
		
		txbDescription.getElement().setPropertyString("placeholder", "Write your feeling now, or about the story of your best memory on the journey!");
		
		DOM.setElementProperty(pathPhotoUpload.getElement(), "multiple", "multiple"); 
		
		formUpload.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				TripShare.loadBox.hide();
				if(listener != null)
					listener.createPathSuccess(boxPathId.getValue());
				cancelPost();
			}
		});
		
//		final Autocomplete autocomplete = Autocomplete.create(
//				(InputElement) (Element) txbLocation.getElement());
//		autocomplete.addPlaceChangedListener(new PlaceChangedHandler() {
//			public void handle() {
//				PlaceResult place = autocomplete.getPlace();
//				locate.setLatLng(place.getGeometry().getLocation());
//			}
//		});
		
//		txbDescription.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//				if(txbDescription.getElement().getClientHeight() >= 360) {
//					txbDescription.addStyleName("PathCreate-Obj6ScrollAble");
//				}
//				else {
//					txbDescription.removeStyleName("PathCreate-Obj6ScrollAble");
//				}
//			}
//		});
	}
	
	public void handlerUploadEvent() {
		if(!isHandlerUploadEvent) {
			handleFileSelect();
			isHandlerUploadEvent = true;
		}
	}
	
	public static native void handleFileSelect() /*-{
		function cancelPhotosUpload() {
			$wnd.document.getElementById('htmlPathPhotos').innerHTML = "";
			$wnd.document.getElementById('scrollPathPhotos').style.height = "0px";
			$wnd.document.getElementById('lbCountPhotos').innerHTML = "0 / Photos";
			$wnd.document.getElementById('formUpload').reset();
		}
		
		function handleFileSelect(evt) {
			var htmlPathPhotos = $wnd.document.getElementById('htmlPathPhotos');
			htmlPathPhotos.innerHTML = "";
			
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
		          span.innerHTML = ['<img class="PhotoUpload-Obj3" src="', e.target.result,
		                            '" title="', escape(theFile.name), '"/>'].join('');
		          htmlPathPhotos.insertBefore(span, null);
		        };
		      })(f);
		
		      // Read in the image file as a data URL.
		      reader.readAsDataURL(f);
		    }
 
 			var cancelSpan = document.createElement('span');
 			cancelSpan.className = "PathCreate-Obj12";
 			cancelSpan.style.height = "76px";
 			cancelSpan.style.width = "75px";
 			cancelSpan.innerHTML = ['<i style="margin-top: 5px;margin-left: 10px;" class="fa fa-ban fa-5x"></i>'].join('');
 			htmlPathPhotos.insertBefore(cancelSpan, null);
 			cancelSpan.addEventListener('click', cancelPhotosUpload);
		    
		    var scrollPathPhotos = $wnd.document.getElementById('scrollPathPhotos');
		    if(countImg > 0) {
		    	scrollPathPhotos.style.height = "100px";
		    }
		    else {
		    	scrollPathPhotos.style.height = "0px";
		    } 	
		    $wnd.document.getElementById('lbCountPhotos').innerHTML = countImg + " / Photos";
	 	}
		
		$wnd.document.getElementById('pathPhotoUpload').addEventListener("change", handleFileSelect, false);
	}-*/;
	
	public void setTripId(Long tripId) {
		this.tripId = tripId;
		txbTimeline.setValue(new Date());
		txbLocation.setFocus(true);
	}

	public void uploadPhoto(final Long tripId, final Long pathId) {
		TripShare.dataService.getUploadUrl(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				formUpload.setAction(result.toString());
				htmlPathPhotos.clear();
				boxTripId.setValue(tripId);
				boxPathId.setValue(pathId);
				formUpload.submit();
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
		TripShare.loadBox.center();
		Path path = new Path();
		path.setTitle(txbLocation.getText());
		path.setLocate(locate);
		path.setCreateDate(txbTimeline.getValue());
		if(!isRichTextEdit) {
			if(txbRichDescription != null) {
				txbRichDescription.setData(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
				path.setDescription(txbRichDescription.getData());
			} else 
				path.setDescription(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
		}
		else
			path.setDescription(txbRichDescription.getData());
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
	
	void cancelPost() {
		this.setStyleName("PathCreate-Obj3");
		txbLocation.setText("");
		txbTimeline.getElement().setInnerHTML("");
		btnRichTextEdit.removeStyleName("PathCreate-Obj16");
		txbDescription.setText("");
		txbDescription.setVisible(true);
		if(txbRichDescription != null) {
			txbRichDescription.setData("");
			txbRichDescription.setVisible(false);
		}
		isRichTextEdit = false;
		scrollPathPhotos.setHeight("0px");
		htmlPathPhotos.getElement().setInnerHTML("");
		lbCountPhotos.setText("0 / Photos");
		formUpload.reset();
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
//							txbLocation.setText(address);
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
			if(txbRichDescription.getHTML().length() != 0) {
				if(Window.confirm("!: Rich text can't convert to normal text, if you continue the text will be clear.")) {
					txbRichDescription.setVisible(false);
					txbDescription.setVisible(true);
					btnRichTextEdit.removeStyleName("PathCreate-Obj16");
					txbDescription.setText("");
					isRichTextEdit = !isRichTextEdit;
				}
			} else {
				txbRichDescription.setVisible(false);
				txbDescription.setVisible(true);
				btnRichTextEdit.removeStyleName("PathCreate-Obj16");
				isRichTextEdit = !isRichTextEdit;
			}
		}
		else {
			if(txbRichDescription == null) {
				txbRichDescription = new CKEditor(getCKConfig());
				editTextBox.add(txbRichDescription);
			}
			txbRichDescription.setVisible(true);
			txbDescription.setVisible(false);
			btnRichTextEdit.addStyleName("PathCreate-Obj16");
			txbRichDescription.setData(txbDescription.getText().replaceAll("(\r\n|\n)", "<br />"));
			isRichTextEdit = !isRichTextEdit;
		}
	}
	
}
