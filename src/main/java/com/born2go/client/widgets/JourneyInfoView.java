package com.born2go.client.widgets;

import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Locate;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class JourneyInfoView extends Composite {

	private static JourneyInfoViewUiBinder uiBinder = GWT
			.create(JourneyInfoViewUiBinder.class);

	interface JourneyInfoViewUiBinder extends UiBinder<Widget, JourneyInfoView> {
	}

	@UiField
	Anchor lbTitle;
	@UiField
	Label lbPoster;
	@UiField
	Image picture;
	@UiField
	HTML htmlContent;
	@UiField
	Label noteDes;
	@UiField
	VerticalPanel verLocate;
	@UiField
	Label btnViewDetail;

	public JourneyInfoView(String urlPicture, Trip trip) {
		initWidget(uiBinder.createAndBindUi(this));
		picture.setUrl(urlPicture);
		lbTitle.setText(trip.getName());
		lbTitle.setHref("/journey/"+trip.getId());
		lbPoster.setText(TripShare.dateFormat(trip.getCreateDate()));
		// set des for trip
		setLocate(trip);
		String content = trip.getDescription();
//		String summaryContent;
//		if (content.length() > 401)
//			summaryContent = content.substring(0, 400) + "...";
//		else
//			summaryContent = content;
		htmlContent.setHTML(content);
		htmlContent.setVisible(false);
		btnViewDetail.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnViewDetail.setVisible(false);
				htmlContent.setVisible(true);
			}
		});
		getDisplayPhoto(trip);
	}

	public void getDisplayPhoto(Trip trip) {
		if(trip.getGallery().isEmpty()) {}
		else {
		int index = (int) (Math.random() * trip.getGallery().size()) + 0;
		Long displayPhotoId = trip.getGallery().get(index);
			TripShare.dataService.findPicture(displayPhotoId, new AsyncCallback<Picture>() {
				@Override
				public void onSuccess(Picture result) {
					if(result != null)
						picture.setUrl(result.getServeUrl());
				}
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	private void setLocate(Trip trip) {
		List<Locate> locates = trip.getJourney().getLocates();
		noteDes.setText(locates.get(0).getAddressName());
		for (int i = 1; i < locates.size(); i++) {
			HorizontalPanel horiLocate = new HorizontalPanel();
			Image image = new Image();
			image.setUrl("/resources/green-spotlight.png");
			image.setStyleName("JourneyInfoView-Obj8");
			horiLocate.add(image);
			Label lbAdd = new Label(locates.get(i).getAddressName());
			lbAdd.setStyleName("JourneyInfoView-Obj9");
			horiLocate.add(lbAdd);
			verLocate.add(horiLocate);
		}
	}

}
