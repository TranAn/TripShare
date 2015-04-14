package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
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
	HTMLPanel facebookComments;
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
	
	static Long tripId;
	
	TripInfo tripInfo;
	PathCreate pathCreate = new PathCreate();
	PhotoUpload photoUpload = new PhotoUpload();
	
	List<Path> listPaths = new ArrayList<Path>();
	List<PathDetail> listPathsDetail = new ArrayList<PathDetail>();
	
	private Trip theTrip;

	public PathView() {
		initWidget(uiBinder.createAndBindUi(this));
		htmlPathCreate.add(pathCreate);
		listArrange.addItem("Newest");
		listArrange.addItem("Oldest");
		
		listArrange.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				htmlPathTable.clear();
				if(listArrange.getSelectedIndex() == 0) {
					for(int i=0; i<=listPathsDetail.size()-1; i++) {
						listPathsDetail.get(i).setStyleName("PathDetail-Obj1");
						htmlPathTable.add(listPathsDetail.get(i));
						listPathsDetail.get(i).setStyleName("PathDetail-Obj1 fadeIn");
					}
				}
				else {
					for(int i=listPathsDetail.size()-1; i>=0; i--) {
						listPathsDetail.get(i).setStyleName("PathDetail-Obj1");
						htmlPathTable.add(listPathsDetail.get(i));
						listPathsDetail.get(i).setStyleName("PathDetail-Obj1 fadeIn");
					}
				}
			}
		});
		
		Window.addWindowScrollHandler(new ScrollHandler() {
			@Override
			public void onWindowScroll(ScrollEvent event) {
				if(event.getScrollTop() >= htmlPathToolbar.getElement().getAbsoluteTop()) {
					if(htmlPathToolbar.getElement().getChildCount() >= 4) {
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
				btnUpload.setVisible(true);
			}

			@Override
			public void createPathSuccess(Long newPathId) {
				TripShare.loadBox.center();
				TripShare.dataService.findPart(newPathId, new AsyncCallback<Path>() {
					
					@Override
					public void onSuccess(Path result) {
						TripShare.loadBox.hide();
						String title = result.getLocate().getAddressName()+ " - " + TripShare.dateFormat(result.getCreateDate());
						String poster = "Tester";
						if(result.getPoster() != null)
							poster = result.getPoster().getUserName();
						PathDetail pathDetail = new PathDetail(result.getId(), "/resources/Travel tips2_resize.jpg", title, poster, result.getDescription());
						if(listPathsDetail.isEmpty())
							htmlPathTable.add(pathDetail);
						else
							htmlPathTable.getElement().insertBefore(pathDetail.getElement(), listPathsDetail.get(0).getElement());
						getPathPhoto(result, pathDetail);
						listPathsDetail.add(0, pathDetail);
						pathDetail.setStyleName("PathDetail-Obj1 fadeIn");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
					}
				});
			}
		});
	}
	
	public void setDirectionResult(DirectionsResult directionResult) {
		tripInfo.setDirectionResult(directionResult);
	}
	
	public void getTrip(final Long tripId) {
		this.tripId = tripId;
		TripShare.dataService.findTrip(tripId, new AsyncCallback<Trip>() {
			
			@Override
			public void onSuccess(Trip result) {
				if(result != null) {
					theTrip = result;
					TripShare.tripMap.drawTheJourney(result.getJourney().getDirections(), result.getJourney().getLocates());
					getThePaths(result.getDestination());
//					facebookComments.getElement().setInnerHTML("<div class='fb-comments' data-href='http://localhost:8080/journey/" + tripId + "' data-numposts='5' data-colorscheme='light'></div>");
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
					listPaths.addAll(result);
					for(int i=result.size()-1; i>=0; i--) {
						Path path = result.get(i);
						String title = path.getLocate().getAddressName()+ " - " + TripShare.dateFormat(path.getCreateDate());
						String poster = "Tester";
						if(path.getPoster() != null)
							poster = path.getPoster().getUserName();
						PathDetail pathDetail = new PathDetail(path.getId(), "/resources/Travel tips2_resize.jpg", title, poster, path.getDescription());
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
		DOM.getElementById("commentBox").setAttribute("style", "display:none");
		DOM.getElementById("tripInfo").setInnerHTML("");
		tripInfo = new TripInfo();
		RootPanel.get("tripInfo").add(tripInfo);
		tripInfo.setTrip(theTrip);
		tripInfo.setListener(new TripInfo.Listener() {
			
			@Override
			public void onUpdateTrip(Trip updateTrip) {
				theTrip = updateTrip;
				cancelEdit();
			}
		});
	}

	void cancelEdit() {
		Window.scrollTo(0, 0);
		htmlPathTable.setVisible(true);
		htmlPathCreate.setVisible(true);
		toolbar.setVisible(true);
		editToolbar.setVisible(false);
		DOM.getElementById("commentBox").setAttribute("style", "");
		htmlPathToolbar.removeStyleName("PathView-Obj13");
		for(PathDetail pathDetail: listPathsDetail) {
			pathDetail.setStyleName("PathDetail-Obj1");
		}
		tripInfo.setTrip(theTrip);
		tripInfo.setDisable();
		TripShare.tripMap.drawTheJourney(theTrip.getJourney().getDirections(), theTrip.getJourney().getLocates());
	}

	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		tripInfo.updateTrip();
	}

	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		cancelEdit();
	}

	@UiHandler("btnPost")
	void onBtnPostClick(ClickEvent event) {
		pathCreate.setStyleName("PathCreate-Obj3 PathCreate-Obj3Open");
		btnUpload.setVisible(false);
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
		List<Long> photos = new ArrayList<Long>();
		photos.addAll(theTrip.getGallery());
		for(Path path: listPaths)
			photos.addAll(path.getGallery());
		TripShare.dataService.listPicture(photos, new AsyncCallback<List<Picture>>() {
			
			@Override
			public void onSuccess(List<Picture> result) {
				String photosUri = "";
				for(Picture p: result)
					photosUri = photosUri + p.getServeUrl() + ";";
				openGallery(photosUri);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static native void openGallery(String photosUri) /*-{
	  	var viewer = new $wnd.PhotoViewer();
	  	var URIs = photosUri.split(";");
	  	for(var i = 0; i < URIs.length-1; i++) {
	  		viewer.add(URIs[i]);
	  	}
		viewer.show(0);
	}-*/;

	@UiHandler("btnComment")
	void onBtnCommentClick(ClickEvent event) {
		Window.scrollTo(0, DOM.getElementById("commentBox").getAbsoluteTop());
	}
}
