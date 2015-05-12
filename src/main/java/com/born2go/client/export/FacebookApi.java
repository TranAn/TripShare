package com.born2go.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.born2go.client.TripShare;
import com.born2go.client.widgets.LoginDialog;
import com.born2go.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

@ExportPackage("GWTExport")
@Export
public class FacebookApi implements Exportable {

	public void saveNewFacebookUser(String userId, String userName) {
		User facebookUser = new User();
		facebookUser.setId(userId);
		facebookUser.setUserName(userName);
		TripShare.dataService.insertUser(facebookUser, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub				
			}
		});
	}
	
	public void logIn() {
		if(TripShare.access_token == null || TripShare.access_token.isEmpty())
			LoginDialog.LoginFacebook();
		/*else
			Window.Location.assign("/profile/"+ TripShare.user_id);*/
	}
	
	public void getAccessToken(String accessToken, String userId) {
		TripShare.getAccessToken(accessToken, userId);
	}

}
