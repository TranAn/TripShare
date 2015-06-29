package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Journey_PathDetail extends Composite {

	private static PathDetailUiBinder uiBinder = GWT
			.create(PathDetailUiBinder.class);

	interface PathDetailUiBinder extends UiBinder<Widget, Journey_PathDetail> {
	}
	
	@UiField Anchor lbTitle;
	@UiField Anchor lbPoster;
	@UiField HTMLPanel pictureTable;
//	@UiField Image picture;
	@UiField ParagraphElement htmlContent;
	@UiField Anchor btnDeletePost;
	@UiField HTMLPanel postControl;
	@UiField Image imgPoster;
	@UiField Label lbDatePost;
	@UiField Anchor btnExpandACollapse;
	@UiField Anchor indexCatalog;
	@UiField Label txbLocation;
	@UiField Label txbPhotos;

	Path path;
	Long pathId;
	
	boolean isAddDeletePost = false;
	boolean isViewDetail = false;
	
	public interface Listener {
		void onDeletePost(Journey_PathDetail pathDetail);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public Journey_PathDetail(final Path path, final Long pathId, String pictureUrl, String title, String postBy, String posterId, String content) {
		
		initWidget(uiBinder.createAndBindUi(this));
		postControl.remove(btnDeletePost);
		
//		Event.setEventListener(btnExpandACollapse.getElement(), new EventListener() {
//	            @Override
//	            public void onBrowserEvent(Event event) {
//	                String string = event.getType();
//	                if(string.equalsIgnoreCase("click")) {
//	                	
//	                }
//	            }
//	        });
//	    Event.sinkEvents(btnExpandACollapse.getElement(), Event.ONCLICK);
		
		indexCatalog.getElement().setAttribute("name", "index_"+ path.getId());
		this.path = path;
		this.pathId = pathId;
//		picture.setUrl(pictureUrl);
		lbTitle.setText(title);
		lbTitle.setTarget("_blank");
		String id = String.valueOf(pathId);
		lbTitle.setHref("/destination/"+ id);
		lbPoster.setText(postBy);
		lbPoster.setHref("/profile/"+ posterId);
		imgPoster.setUrl("https://graph.facebook.com/"+ posterId+ "/picture?width=25&height=25");
		if(path.getCreateDate() != null)
			lbDatePost.setText(TripShare.dateFormat(path.getCreateDate()));
		
		if(path.getLocate() != null) {
			txbLocation.setText("at: "+ path.getLocate().getAddressName());
			txbLocation.setVisible(true);
		}
		else {
			txbLocation.setText("- None location added");
			txbLocation.setVisible(true);
		}
		
		if(path.getShortDescription() != null)
			htmlContent.setInnerHTML(truncateText(path.getShortDescription().replaceAll("br2n", "<br/>")));
		else
			htmlContent.setInnerHTML(truncateText(path.getDescription()));
		
		btnDeletePost.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm("!: Are you sure you want to delete this post?")) {
					TripShare.loadBox.center();
					TripShare.dataService.removePart(pathId, new AsyncCallback<Void>() {			
						@Override
						public void onSuccess(Void result) {
							TripShare.loadBox.hide();
							removeThisPost();
						}						
						@Override
						public void onFailure(Throwable caught) {
							TripShare.loadBox.hide();
							Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
						}
					});
				}
			}
		});
		
//		btnExpandACollapse.setTarget("_blank");
		btnExpandACollapse.setHref("/destination/"+ id);
//		btnExpandACollapse.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(!isViewDetail) {
////					picture.setVisible(false);
//					htmlContent.setInnerHTML(path.getDescription());
//					btnExpandACollapse.setTitle("Collapse");
//					btnExpandACollapse.getElement().setInnerHTML("<i style='position: relative; top: 2px;' class='boxbutton'>Collapse</i>");
//				}
//				else {
////					picture.setVisible(true);
//					htmlContent.setInnerHTML(truncateText(path.getShortDescription().replaceAll("br2n", "<br/>")));
//					btnExpandACollapse.setTitle("Read more");
//					btnExpandACollapse.getElement().setInnerHTML("<i style='position: relative; top: 2px;' class='boxbutton'>Read more</i>");
////					Window.scrollTo(0, picture.getAbsoluteTop()-50);
//				}
//				isViewDetail = !isViewDetail;
//			}
//		});
		
		getPathPhotos();
	}
	
	public static native String truncateText(String content) /*-{
		if(content.trim().length > 301) {
			var shortText = content    // get the text within the div
			    .trim()    // remove leading and trailing spaces
			    .substring(0, 300)    // get first 500 characters
			    .split(" ") // separate characters into an array of words
			    .slice(0, -1)    // remove the last full or partial word
			    .join(" ") + "..."; // combine into a single string and append "..."
			return shortText;
		} else 
			return content;
	}-*/;
	
	public void addPostControl() {
		if(!isAddDeletePost)
			postControl.add(btnDeletePost);
		isAddDeletePost = true;
		btnExpandACollapse.addStyleName("PathDetail-Obj13");
	}
	
	public void setDisplayPhoto(String src) {
//		picture.setUrl(src);
	}
	
	public void removeThisPost() {
		if(listener != null)
			listener.onDeletePost(this);
	}
	
	public void updatePath(String title, String content) {
		lbTitle.setText(title);
		htmlContent.setInnerHTML(truncateText(content));
	}
	
	public Path getPath() {
		return path;
	}
	
	public void getPathPhotos() {
		final int displayPhotoNumber;
		if(path.getGallery().isEmpty()) {}
		else {
			pictureTable.setVisible(true);
			if(path.getGallery().size() <= 4) {
				displayPhotoNumber = path.getGallery().size();
				txbPhotos.setVisible(false);
			}
			else {
				displayPhotoNumber = (int) (Math.random() * 3) + 2;
				txbPhotos.setVisible(true);
				txbPhotos.setText("+"+ (path.getGallery().size()-displayPhotoNumber)+ " Photos");
			}
			for(int i=0; i<=displayPhotoNumber-1; i++) {
				final int index = i;
				TripShare.dataService.findPicture(path.getGallery().get(i), new AsyncCallback<Picture>() {
					
					@Override
					public void onSuccess(Picture arg0) {				
						Image photo = new Image();
						photo.setUrl(arg0.getServeUrl());
						photo.setStyleName("PathDetail_Obj15");
						int photoMaxWidth = 700 / displayPhotoNumber - 28;
						int rotate1 = (int) (Math.random() * 4) - 2;
						int rotate2 = (int) (Math.random() * 4) - 2;
						if(index == 0) 
							photo.getElement().setAttribute("style", "max-width:"+ photoMaxWidth+ "px");
						else 
							photo.getElement().setAttribute("style", "max-width:"+ photoMaxWidth+ "px; transform:rotate("+ rotate1+ "deg) translate3d( 0, 0, 0); -webkit-transform:rotate("+ rotate2+ "deg) translate3d( 0, 0, 0)");
						pictureTable.add(photo);
					}
					
					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}
	

}
