package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.maps.gwt.client.DirectionsResult;

public class PathView extends Composite {

	private static PathViewUiBinder uiBinder = GWT
			.create(PathViewUiBinder.class);

	interface PathViewUiBinder extends UiBinder<Widget, PathView> {
	}
	
	@UiField
	HTMLPanel htmlPathToolbarFixed;
	@UiField
	HTMLPanel htmlPathToolbar;
	@UiField
	HTMLPanel htmlPathTable;
	@UiField
	HTMLPanel htmlPathCreate;
	@UiField
	HTMLPanel toolbar;
	@UiField
	Anchor btnEdit;
	@UiField
	Anchor btnPost;
	@UiField
	Anchor btnGallery;
	@UiField
	Anchor btnUpload;
	@UiField
	Anchor btnComment;
	@UiField 
	ListBox listArrange;
	@UiField 
	HTMLPanel editToolbar;
	@UiField 
	Anchor btnSave;
	@UiField 
	Anchor btnCancel;
	
	Long tripId;
	TripInfo tripInfo;
	PathCreate pathCreate = new PathCreate();
	PhotoUpload photoUpload = new PhotoUpload();
	List<PathDetail> listPathsDetail = new ArrayList<PathDetail>();
	
	private Trip theTrip;

	public PathView() {
		initWidget(uiBinder.createAndBindUi(this));
		htmlPathCreate.add(pathCreate);
		listArrange.addItem("Date descending");
		listArrange.addItem("Date ascending");
		
		Window.addWindowScrollHandler(new ScrollHandler() {
			@Override
			public void onWindowScroll(ScrollEvent event) {
				if(event.getScrollTop() >= htmlPathToolbar.getElement().getAbsoluteTop()) {
					if(htmlPathToolbar.getElement().getChildCount() >= 2) {
						htmlPathToolbar.setHeight(toolbar.getOffsetHeight()+ htmlPathCreate.getOffsetHeight()+ "px");
						htmlPathToolbar.remove(toolbar);
						htmlPathToolbar.remove(htmlPathCreate);
						htmlPathToolbarFixed.add(toolbar);
						htmlPathToolbarFixed.add(htmlPathCreate);				
					}
				}
				else {
					if(htmlPathToolbarFixed.getElement().hasChildNodes()) {
						htmlPathToolbar.setHeight("");
						htmlPathToolbar.add(toolbar);
						htmlPathToolbar.add(htmlPathCreate);
						htmlPathToolbarFixed.remove(toolbar);
						htmlPathToolbarFixed.remove(htmlPathCreate);			
					}
				}
				
				for(PathDetail pathDetail: listPathsDetail) {
					if(event.getScrollTop() + Window.getClientHeight() > pathDetail.getElement().getAbsoluteTop()) {
						pathDetail.setStyleName("PathDetail-Obj1 fadeIn");
//						listPathsDetail.remove(pathDetail);
					}
				}
			}
		});
		
		pathCreate.setListener(new PathCreate.Listener() {
			
			@Override
			public void onClose() {
				if(htmlPathToolbarFixed.getElement().hasChildNodes()) {
					htmlPathToolbar.setHeight(toolbar.getOffsetHeight()+ "px");
				}
			}
		});
	}
	
	public void setDirectionResult(DirectionsResult directionResult) {
		tripInfo.setDirectionResult(directionResult);
	}
	
	public void getTrip(Long tripId) {
		this.tripId = tripId;
		TripShare.dataService.findTrip(tripId, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				if(result != null) {
					theTrip = result;
					TripShare.tripMap.drawTheJourney(result.getJourney().getDirections(), result.getJourney().getLocates());
					getThePaths(result.getDestination());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
//				Window.alert("com.born2go.client.widgets.TripView log: server response error!");
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
						listPathsDetail.add(pathDetail);
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
		Window.scrollTo(0, 0);
		htmlPathToolbar.addStyleName("PathView-Obj13");
		htmlPathTable.setVisible(false);
		htmlPathCreate.setVisible(false);
		toolbar.setVisible(false);
		editToolbar.setVisible(true);
		DOM.getElementById("tripInfo").setInnerHTML("");
		tripInfo = new TripInfo();
		RootPanel.get("tripInfo").add(tripInfo);
		tripInfo.setTrip(theTrip);
	}

	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		pathCreate.setStyleName("PathCreate-Obj3 PathCreate-Obj3Open");
		pathCreate.setTripId(tripId);
		pathCreate.handlerUploadEvent();
		Timer timer = new Timer () {
			@Override
			public void run() {
				pathCreate.setStyleName("PathCreate-Obj3 PathCreate-Obj3WideOpen");
			}
		};timer.schedule(200);
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
	
	@UiHandler("btnComment")
	void onBtnCommentClick(ClickEvent event) {
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
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		tripInfo.updateTrip();
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		Window.scrollTo(0, 0);
		htmlPathTable.setVisible(true);
		htmlPathCreate.setVisible(true);
		toolbar.setVisible(true);
		editToolbar.setVisible(false);
		htmlPathToolbar.removeStyleName("PathView-Obj13");
		for(PathDetail pathDetail: listPathsDetail) {
			pathDetail.setStyleName("PathDetail-Obj1");
		}
		tripInfo.setTrip(theTrip);
		tripInfo.setDisable();
	}
}
