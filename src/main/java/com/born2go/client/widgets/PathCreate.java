package com.born2go.client.widgets;

import java.util.Date;

import com.born2go.client.TripShare;
import com.born2go.shared.Locate;
import com.born2go.shared.Path;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
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
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.PlaceResult;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;

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
	
	Long tripId;
	Locate locate = new Locate();
	boolean isHandlerUploadEvent = false;
	
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

	public PathCreate() {
		initWidget(uiBinder.createAndBindUi(this));
//		this.setVisible(false);
		
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
		
		txbDescription.getElement().setPropertyString("placeholder", "Hãy viết lại nhật ký, hay cảm xúc của bạn về chuyến đi!");
		
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
		locate.setAddressName(txbLocation.getText());
		path.setLocate(locate);
		path.setCreateDate(txbTimeline.getValue());
		path.setDescription(txbDescription.getValue());
		TripShare.dataService.insertPart(path, tripId, TripShare.access_token, new AsyncCallback<Path>() {
			
			@Override
			public void onSuccess(Path result) {
				uploadPhoto(tripId, result.getId());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				TripShare.loadBox.hide();
				Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
			}
		});
	}
	
	void cancelPost() {
		this.setStyleName("PathCreate-Obj3");
		txbLocation.setText("");
		txbTimeline.getElement().setInnerHTML("");
		txbDescription.setText("");
		scrollPathPhotos.setHeight("0px");
		htmlPathPhotos.getElement().setInnerHTML("");
		lbCountPhotos.setText("0 / Photos");
		formUpload.reset();
		locate = new Locate();
		if(listener != null)
			listener.onClose();
	}

	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		cancelPost();
	}
	
	@UiHandler("btnFindYourLocation") 
	void onBtnFindYourLocationClick(ClickEvent event) {
		Geolocation geoLocation = Geolocation.getIfSupported();
		if (geoLocation == null) {
			Window.alert("!: Your old browser not support GeoLocation");
		} else {
			geoLocation.getCurrentPosition(new com.google.gwt.core.client.Callback<Position, PositionError>() {
				
				@Override
				public void onSuccess(Position result) {
					final LatLng l = LatLng.create(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
					GeocoderRequest geoRequest = GeocoderRequest.create();
					geoRequest.setLocation(l);
					Geocoder geoCode = Geocoder.create();
					geoCode.geocode(geoRequest, new Geocoder.Callback() {
						@Override
						public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
							String address = a.get(0).getFormattedAddress();
							txbLocation.setText(address);
							locate.setLatLng(l);
						}
					});
				}
				
				@Override
				public void onFailure(PositionError reason) {
					Window.alert("!: Can't find your location");
				}
			});
		}
	}
	
}
