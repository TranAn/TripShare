package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PathDetail extends Composite {

	private static PathDetailUiBinder uiBinder = GWT
			.create(PathDetailUiBinder.class);

	interface PathDetailUiBinder extends UiBinder<Widget, PathDetail> {
	}
	
	@UiField Label lbTitle;
	@UiField Label lbPoster;
	@UiField Image picture;
	@UiField ParagraphElement htmlContent;

	public PathDetail(String pictureUrl, String title, String postBy, String content) {
		initWidget(uiBinder.createAndBindUi(this));
		
		picture.setUrl(pictureUrl);
		lbTitle.setText(title);
		lbPoster.setText(postBy);
		String summaryContent;
		if(content.length() > 501)
			summaryContent = content.substring(0, 500) + "..." + " <a href=''>View more</a>";
		else
			summaryContent = content;
		htmlContent.setInnerHTML(summaryContent);
	}
	
	public void setDisplayPhoto(String src) {
		picture.setUrl(src);
	}

	
}
