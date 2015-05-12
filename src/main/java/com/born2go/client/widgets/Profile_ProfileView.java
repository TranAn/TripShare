package com.born2go.client.widgets;

import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Profile_ProfileView extends Composite {

	private static ProfileViewUiBinder uiBinder = GWT
			.create(ProfileViewUiBinder.class);
	
	@UiField
	VerticalPanel verDetails;
	@UiField
	ListBox lstbViewOption;
	@UiField
	Label lbNoJourneyFound;

	interface ProfileViewUiBinder extends UiBinder<Widget, Profile_ProfileView> {
	}
	
	private String profileId;
	private List<Trip> listTrips;

	public Profile_ProfileView() {
		initWidget(uiBinder.createAndBindUi(this));
		lstbViewOption.addItem("All");
		lstbViewOption.addItem("Owned Trip");
		lstbViewOption.addItem("Joined Trip");
		
		lstbViewOption.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				verDetails.clear();
				int countTripDisplay = 0;
				if(lstbViewOption.getSelectedIndex() == 0) {
					for(int i = listTrips.size()-1; i >=0; i--) {
						Profile_JourneyInfoView journeyInfoView = new Profile_JourneyInfoView("/resources/Travel tips2_resize.jpg", listTrips.get(i));
						verDetails.add(journeyInfoView);
						countTripDisplay++;
					}
				} 
				else if (lstbViewOption.getSelectedIndex() == 1) {
					for(int i = listTrips.size()-1; i >=0; i--) {
						if(listTrips.get(i).getPoster().getUserID().toString().equals(profileId)) {
							Profile_JourneyInfoView journeyInfoView = new Profile_JourneyInfoView("/resources/Travel tips2_resize.jpg", listTrips.get(i));
							verDetails.add(journeyInfoView);
							countTripDisplay++;
						}
					}
				}
				else if (lstbViewOption.getSelectedIndex() == 2) {
					for(int i = listTrips.size()-1; i >=0; i--) {
						if(!listTrips.get(i).getPoster().getUserID().toString().equals(profileId)) {
							Profile_JourneyInfoView journeyInfoView = new Profile_JourneyInfoView("/resources/Travel tips2_resize.jpg", listTrips.get(i));
							verDetails.add(journeyInfoView);
							countTripDisplay++;
						}
					}
				}
				if(countTripDisplay == 0) {
					verDetails.add(lbNoJourneyFound);
				}
			}
		});
	}
	
	void getJourneysOfUser(User user) {
		TripShare.dataService.listOfTrip(user.getMyTrips(), new AsyncCallback<List<Trip>>() {
			@Override
			public void onSuccess(List<Trip> result) {
				listTrips = result;
				if(!result.isEmpty())
					verDetails.remove(lbNoJourneyFound);
				for(int i = result.size()-1; i >=0; i--) {
					Profile_JourneyInfoView journeyInfoView = new Profile_JourneyInfoView("/resources/Travel tips2_resize.jpg", result.get(i));
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
		profileId = userID;
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
	}

}
