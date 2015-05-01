package com.born2go.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BetaTripSample extends Composite {

	private static BetaTripUiBinder uiBinder = GWT
			.create(BetaTripUiBinder.class);
	@UiField Anchor link;

	interface BetaTripUiBinder extends UiBinder<Widget, BetaTripSample> {
	}
	
	public BetaTripSample() {
		initWidget(uiBinder.createAndBindUi(this));
		
		link.setHref("http://born2go-b.appspot.com/journey/5700305828184064");
	}

}
