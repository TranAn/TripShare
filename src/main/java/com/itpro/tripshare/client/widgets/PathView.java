package com.itpro.tripshare.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.itpro.tripshare.client.TripShare;
import com.itpro.tripshare.shared.Path;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.client.widgets.PathDetail;

public class PathView extends Composite {

	private static PathViewUiBinder uiBinder = GWT
			.create(PathViewUiBinder.class);

	interface PathViewUiBinder extends UiBinder<Widget, PathView> {
	}

	@UiField
	HTMLPanel htmlPathTable;
	@UiField
	HTMLPanel htmlPathCreate;
	@UiField
	Anchor btnEdit;
	@UiField
	Anchor btnPost;
	@UiField
	Anchor btnGallery;
	@UiField
	Anchor btnUpload;
	
	Long tripId;
	PathCreate pathCreate = new PathCreate();
	PhotoUpload photoUpload = new PhotoUpload();

	public PathView() {
		initWidget(uiBinder.createAndBindUi(this));
		htmlPathCreate.add(pathCreate);
	}
	
	public void getTrip(Long tripId) {
		this.tripId = tripId;
		TripShare.dataService.findTrip(tripId, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				if(result != null) {
					TripShare.tripMap.drawTheJourney(result.getJourney().getDirections());
					getThePaths(result.getDestination());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
//				Window.alert("com.itpro.tripshare.client.widgets.TripView log: server response error!");
			}
		});
	}
	
	void getThePaths(List<Long> idsPath) {
		if(!idsPath.isEmpty())
			TripShare.dataService.listOfPath(idsPath, new AsyncCallback<List<Path>>() {
				
				@Override
				public void onSuccess(List<Path> result) {
					for(Path path: result) {
						String title = path.getLocate().getAddressName() + " - " + path.getCreateDate();
						PathDetail pathDetail = new PathDetail("/resources/loaderPincode.gif", title, path.getDescription());
						htmlPathTable.add(pathDetail);
						getPathPhoto(path, pathDetail);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
	}
	
	void getPathPhoto(Path path, final PathDetail pathDetail) {
		if(path.getGallery().isEmpty()) {
			
		}
		else {
			int index = (int) (Math.random() * path.getGallery().size()) + 0;
			Long displayPhotoId = path.getGallery().get(index);
			TripShare.dataService.findPicture(displayPhotoId, new AsyncCallback<Picture>() {
				
				@Override
				public void onSuccess(Picture result) {
					pathDetail.setDisplayPhoto(result.getServeUrl());
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	@UiHandler("btnEdit")
	void onBtnEditClick(ClickEvent event) {
	}

	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		pathCreate.setVisible(true);
		pathCreate.setTripId(tripId);
		pathCreate.handlerUploadEvent();
	}

	@UiHandler("btnUpload")
	void onBtnUploadClick(ClickEvent event) {
		photoUpload.center();
		photoUpload.handlerUploadEvent();
	}

	@UiHandler("btnGallery")
	void onBtnGalleryClick(ClickEvent event) {
		openGallery();
	}
	
	public static native void openGallery() /*-{
	  	var viewer = new $wnd.PhotoViewer();
  		viewer.add('http://s13.postimg.org/5t4xm5ruf/1379562560_beautiful_nature_wallpaper_hd.jpg/resources/1.jpg');
  		viewer.add('http://s13.postimg.org/7alsjz77b/alone_in_the_universe_wallpaper_1366x768.jpg');
  		viewer.add('http://s13.postimg.org/k39wjwit3/Desktop_Wallpaper_Background_Desktop_Background.jpg');
  		viewer.add('http://s13.postimg.org/se4wj7t4n/Gaming_3_D_Earth_Sky_94.jpg');
  		viewer.add('http://s13.postimg.org/6p1f1m1iv/spiral_galaxy_2880x1800.jpg');
  		viewer.show(0);
	}-*/;
	
}
