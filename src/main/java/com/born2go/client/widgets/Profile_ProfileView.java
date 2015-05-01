package com.born2go.client.widgets;

import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Profile_ProfileView extends Composite {

	private static ProfileViewUiBinder uiBinder = GWT
			.create(ProfileViewUiBinder.class);
	
	@UiField
	VerticalPanel verDetails;

	interface ProfileViewUiBinder extends UiBinder<Widget, Profile_ProfileView> {
	}

	public Profile_ProfileView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	void getJourneysOfUser(User user) {
		TripShare.dataService.listOfTrip(user.getMyTrips(), new AsyncCallback<List<Trip>>() {
			@Override
			public void onSuccess(List<Trip> result) {
				if(!result.isEmpty())
					verDetails.clear();
				for(Trip trip: result) {
					Profile_JourneyInfoView journeyInfoView = new Profile_JourneyInfoView("/resources/Travel tips2_resize.jpg", trip);
					verDetails.add(journeyInfoView);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(TripShare.ERROR_MESSAGE);
			}
		});
	}

	public void showProfileView(final String userID) {
		TripShare.dataService.findUser(userID, new AsyncCallback<User>() {
			@Override
			public void onSuccess(User result) {
				if(result != null)
					getJourneysOfUser(result);
			}	
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
//		userID = "userId";
//		TripShare.dataService.findTrip_Picture(userID,
//				new AsyncCallback<Map<String, Trip>>() {
//
//					@Override
//					public void onSuccess(Map<String, Trip> result) {
//						for (Map.Entry<String, Trip> entry : result.entrySet()) {
//							String urlPicture = entry.getKey();
//							if (urlPicture.contains(emtryPicture))
//								urlPicture = "/resources/1427111517_palm_tree.png";
//							Trip trip = entry.getValue();
//							journeyInfoView = new Profile_JourneyInfoView(urlPicture,
//									trip);
//							verDetails.add(journeyInfoView);
//
//						}
//					}
//
//					@Override
//					public void onFailure(Throwable caught) {
//
//					}
//				});
//		TripShare.dataService.findUser(userID, new AsyncCallback<User>() {	
//			@Override
//			public void onSuccess(User result) {
//				if(result == null)
//					Window.Location.assign("/");
//				else {
//					iconProfile.setUrl("https://graph.facebook.com/"+userID+"/picture?type=normal");
//					getFacebookUserInfo(userID);
//				}
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//			}
//		});
	}

}
