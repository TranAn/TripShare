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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PathDetail extends Composite {

	private static PathDetailUiBinder uiBinder = GWT
			.create(PathDetailUiBinder.class);

	interface PathDetailUiBinder extends UiBinder<Widget, PathDetail> {
	}
	
	@UiField Anchor lbTitle;
	@UiField Label lbPoster;
	@UiField Image picture;
	@UiField ParagraphElement htmlContent;
	@UiField Anchor btnDeletePost;
	
	Long pathId;
	
	public interface Listener {
		void onDeletePost(PathDetail pathDetail);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public PathDetail(final Long pathId, String pictureUrl, String title, String postBy, String content) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.pathId = pathId;
		picture.setUrl(pictureUrl);
		lbTitle.setText(title);
		lbTitle.setTarget("_blank");
		String id = String.valueOf(pathId);
		lbTitle.setHref("/destination/"+ id);
		lbPoster.setText(postBy);
		String summaryContent;
		if(content.length() > 751)
			summaryContent = content.substring(0, 750) + "<p>..." + " <a target='_blank' href='/destination/"+ pathId+ "'>View more</a></p>";
		else
			summaryContent = content;
		htmlContent.setInnerHTML(summaryContent);
		
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
	
	public void setDisplayPhoto(String src) {
		picture.setUrl(src);
	}
	
	public void removeThisPost() {
		if(listener != null)
			listener.onDeletePost(this);
	}

}
