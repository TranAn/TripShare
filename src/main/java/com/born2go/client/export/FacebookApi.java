package com.born2go.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.born2go.client.TripShare;
import com.born2go.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

@ExportPackage("GWTExport")
@Export
public class FacebookApi implements Exportable {

	public void saveNewFacebookUser(String userId) {
		User facebookUser = new User();
		facebookUser.setId(userId);
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

}
