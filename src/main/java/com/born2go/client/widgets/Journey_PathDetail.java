package com.born2go.client.widgets;

import com.born2go.client.TripShare;
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
import com.google.gwt.user.client.ui.Widget;

public class Journey_PathDetail extends Composite {

	private static PathDetailUiBinder uiBinder = GWT
			.create(PathDetailUiBinder.class);

	interface PathDetailUiBinder extends UiBinder<Widget, Journey_PathDetail> {
	}
	
	@UiField Anchor lbTitle;
	@UiField Anchor lbPoster;
	@UiField Image picture;
	@UiField ParagraphElement htmlContent;
	@UiField Anchor btnDeletePost;
	@UiField HTMLPanel postControl;
	
	Long pathId;
	
	boolean isAddDeletePost = false;
	
	public interface Listener {
		void onDeletePost(Journey_PathDetail pathDetail);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public Journey_PathDetail(final Long pathId, String pictureUrl, String title, String postBy, String posterId, String content) {
		initWidget(uiBinder.createAndBindUi(this));
		
		postControl.remove(btnDeletePost);
		
		this.pathId = pathId;
		picture.setUrl(pictureUrl);
		lbTitle.setText(title);
		lbTitle.setTarget("_blank");
		String id = String.valueOf(pathId);
		lbTitle.setHref("/destination/"+ id);
		lbPoster.setText(postBy);
		lbPoster.setHref("/profile/"+ posterId);
		htmlContent.setInnerHTML(truncateText(content));
		
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
	}
	
	public static native String truncateText(String content) /*-{
		if(content.trim().length > 901) {
			var shortText = content    // get the text within the div
			    .trim()    // remove leading and trailing spaces
			    .substring(0, 900)    // get first 600 characters
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
	}
	
	public void setDisplayPhoto(String src) {
		picture.setUrl(src);
	}
	
	public void removeThisPost() {
		if(listener != null)
			listener.onDeletePost(this);
	}
	
	public void updatePath(String title, String content) {
		lbTitle.setText(title);
		htmlContent.setInnerHTML(truncateText(content));
	}

}
