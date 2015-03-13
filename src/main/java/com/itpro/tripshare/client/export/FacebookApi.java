package com.itpro.tripshare.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.client.TripShare;
import com.itpro.tripshare.shared.User;

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
