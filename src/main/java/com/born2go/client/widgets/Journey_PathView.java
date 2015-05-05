package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Poster;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.DirectionsResult;

public class Journey_PathView extends Composite {

	private static PathViewUiBinder uiBinder = GWT
			.create(PathViewUiBinder.class);

	interface PathViewUiBinder extends UiBinder<Widget, Journey_PathView> {
	}
	
	@UiField
	HTMLPanel pathViewForm;
	@UiField
	HTMLPanel dummyNode;
	@UiField
	HTMLPanel htmlPathToolbar;
	@UiField
	FlowPanel htmlPathTable;
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
	
	List<Path> listPaths = new ArrayList<Path>();
	List<Journey_PathDetail> listPathsDetail = new ArrayList<Journey_PathDetail>();
	
	private Journey_TripEdit tripInfo;
	private Journey_PathCreate pathCreate = new Journey_PathCreate();
	private PhotoUpload photoUpload = new PhotoUpload();
	private Trip theTrip;
	private boolean isPoster = false;
	private boolean isOpenPathCreate = false;

	public Journey_PathView() {
		initWidget(uiBinder.createAndBindUi(this));
		htmlPathCreate.add(pathCreate);
		listArrange.addItem("Newest");
		listArrange.addItem("Oldest");
		btnEdit.addStyleName("PathView-Obj14");
		btnPost.addStyleName("PathView-Obj14");
		btnUpload.addStyleName("PathView-Obj14");
		
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
				if(event.getScrollTop() >= pathViewForm.getElement().getAbsoluteTop()) {
					htmlPathToolbar.setStyleName("PathView-Obj4");
					if(!isOpenPathCreate)
						dummyNode.setHeight("95px");
					else {
						if(dummyNode.getOffsetHeight() == 0)
							dummyNode.setHeight("395px");
					}
				}
				else {	
					if(!pathViewForm.getElement().isOrHasChild(htmlPathToolbar.getElement()))
						pathViewForm.getElement().insertFirst(htmlPathToolbar.getElement());
					htmlPathToolbar.setStyleName("PathView-Obj3");
					dummyNode.setHeight("0px");
				}
				
				for(Journey_PathDetail pathDetail: listPathsDetail) {
					if(event.getScrollTop() + Window.getClientHeight() > pathDetail.getElement().getAbsoluteTop()) {
						pathDetail.setStyleName("PathDetail-Obj1 fadeIn");
					}
				}
			}
		});
		
		pathCreate.setListener(new Journey_PathCreate.Listener() {
			@Override
			public void onClose() {
				btnUpload.setVisible(true);
				isOpenPathCreate = false;
				if(dummyNode.getOffsetHeight() == 395)
					dummyNode.setHeight("95px");
			}
			@Override
			public void createPathSuccess(Long newPathId) {
				TripShare.loadBox.center();
				TripShare.dataService.findPart(newPathId, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(final Path result) {
						TripShare.loadBox.hide();
						if(listPaths.contains(result)) {
							Journey_PathDetail existPathDetail = listPathsDetail.get(listPaths.size() - listPaths.indexOf(result) - 1);
							String title = (result.getTitle())+ " - " + TripShare.dateFormat(result.getCreateDate());
							existPathDetail.updatePath(title, result.getDescription());
							getPathPhoto(result, existPathDetail);
							listPaths.set(listPaths.indexOf(result), result);
							pathCreate.getListPaths(listPaths);
						} else {
							String title = (result.getTitle())+ " - " + TripShare.dateFormat(result.getCreateDate());
							Poster poster = new Poster();
							if(result.getPoster() != null)
								poster = result.getPoster();
							Journey_PathDetail pathDetail = new Journey_PathDetail(result.getId(), "/resources/Travel tips2_resize.jpg", title, poster.getUserName(), poster.getUserID().toString(), result.getDescription());
							pathDetail.addPostControl();
							pathDetail.setListener(new Journey_PathDetail.Listener() {
								@Override
								public void onDeletePost(Journey_PathDetail pathDetail) {
									htmlPathTable.remove(pathDetail);
									listPathsDetail.remove(pathDetail);
									listPaths.remove(result);
									pathCreate.getListPaths(listPaths);
								}
							});
							if(listPathsDetail.isEmpty())
								htmlPathTable.add(pathDetail);
							else
								htmlPathTable.insert(pathDetail, 0);
							getPathPhoto(result, pathDetail);
							pathDetail.setStyleName("PathDetail-Obj1 fadeIn");
							//---
							listPathsDetail.add(0, pathDetail);
							listPaths.add(result);
							pathCreate.getListPaths(listPaths);
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert(TripShare.ERROR_MESSAGE);
					}
				});
			}
		});
	}
	
	public void checkPermission() {
		if(TripShare.user_id == null || theTrip == null)
			isPoster = false;
		else {
			if(theTrip.getPoster().getUserID().toString().equals(TripShare.user_id)) {
				isPoster = true;
				btnEdit.removeStyleName("PathView-Obj14");
				btnPost.removeStyleName("PathView-Obj14");
				btnUpload.removeStyleName("PathView-Obj14");
				for(Journey_PathDetail pd: listPathsDetail)
					pd.addPostControl();
			}
			else
				isPoster = false;
		}
	}
	
	public void setDirectionResult(DirectionsResult directionResult) {
		tripInfo.setDirectionResult(directionResult);
	}
	
	public void getTrip(final Long tripId) {
		Journey_PathView.tripId = tripId;
		TripShare.dataService.findTrip(tripId, new AsyncCallback<Trip>() {
			@Override
			public void onSuccess(Trip result) {
				if(result != null) {
					theTrip = result;
					TripShare.tripMap.drawTheJourney(result.getJourney().getDirections(), result.getJourney().getLocates());
					getThePaths(result.getDestination());
					checkPermission();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	void getThePaths(List<Long> idsPath) {
		if(!idsPath.isEmpty())
			TripShare.dataService.listOfPath(idsPath, new AsyncCallback<List<Path>>() {
				@Override
				public void onSuccess(List<Path> result) {
					listPaths.addAll(result);
					pathCreate.getListPaths(listPaths);
					for(int i=result.size()-1; i>=0; i--) {
						final Path path = result.get(i);
						String title = "";
						if(path.getTitle() != null)
							title = path.getTitle()+ " - " + TripShare.dateFormat(path.getCreateDate()); 
						else if(path.getLocate() != null)
							title = path.getLocate().getAddressName()+ " - " + TripShare.dateFormat(path.getCreateDate());
						Poster poster = new Poster();
						if(path.getPoster() != null)
							poster = path.getPoster();
						Journey_PathDetail pathDetail = new Journey_PathDetail(path.getId(), "/resources/Travel tips2_resize.jpg", title, poster.getUserName(), poster.getUserID().toString(), path.getDescription());
						pathDetail.setListener(new Journey_PathDetail.Listener() {
							@Override
							public void onDeletePost(Journey_PathDetail pathDetail) {
								htmlPathTable.remove(pathDetail);
								listPathsDetail.remove(pathDetail);
								listPaths.remove(path);
								pathCreate.getListPaths(listPaths);
							}
						});
						htmlPathTable.add(pathDetail);
						getPathPhoto(path, pathDetail);
						if(theTrip.getPoster().getUserID().toString().equals(TripShare.user_id)) 
							pathDetail.addPostControl();
						//---
						listPathsDetail.add(pathDetail);
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
	}
	
	void getPathPhoto(Path path, final Journey_PathDetail pathDetail) {
		if(path.getGallery().isEmpty()) {}
		else {
			Long displayPhotoId = null;
			if(path.getFeaturedPhoto() != null)
				displayPhotoId = path.getGallery().get(path.getFeaturedPhoto());
			else
				if(path.getGallery() != null)
					displayPhotoId = path.getGallery().get(0);
			if(displayPhotoId != null) {
				TripShare.dataService.findPicture(displayPhotoId, new AsyncCallback<Picture>() {
					@Override
					public void onSuccess(Picture result) {
						if(result != null)
							pathDetail.setDisplayPhoto(result.getServeUrl());
					}				
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}
				});
			}
		}
	}

	@UiHandler("btnEdit")
	void onBtnEditClick(ClickEvent event) {
		if(isPoster) {
			Window.scrollTo(0, 0);
			htmlPathToolbar.addStyleName("PathView-Obj13");
			htmlPathTable.setVisible(false);
			htmlPathCreate.setVisible(false);
			toolbar.setVisible(false);
			editToolbar.setVisible(true);
			DOM.getElementById("commentBox").setAttribute("style", "display:none");
			DOM.getElementById("tripInfo").setInnerHTML("");
			tripInfo = new Journey_TripEdit();
			RootPanel.get("tripInfo").add(tripInfo);
			tripInfo.setTrip(theTrip);
			tripInfo.setListener(new Journey_TripEdit.Listener() {			
				@Override
				public void onUpdateTrip(Trip updateTrip) {
					theTrip = updateTrip;
					cancelEdit();
				}
			});
		}
	}

	void cancelEdit() {
		Window.scrollTo(0, 0);
		htmlPathTable.setVisible(true);
		htmlPathCreate.setVisible(true);
		toolbar.setVisible(true);
		editToolbar.setVisible(false);
		DOM.getElementById("commentBox").setAttribute("style", "");
		htmlPathToolbar.removeStyleName("PathView-Obj13");
		for(Journey_PathDetail pathDetail: listPathsDetail) {
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
		if(isPoster) {
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
			isOpenPathCreate = true;
		}
	}

	@UiHandler("btnUpload")
	void onBtnUploadClick(ClickEvent event) {
		if(isPoster) {
			photoUpload.center();
			photoUpload.uploadFor(tripId, null);
			photoUpload.handlerUploadEvent();
		}
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
