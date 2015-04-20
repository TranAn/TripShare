package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.born2go.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox {

	private static LoginDialogUiBinder uiBinder = GWT
			.create(LoginDialogUiBinder.class);

	interface LoginDialogUiBinder extends UiBinder<Widget, LoginDialog> {
	}

	public LoginDialog() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoginDialog-Obj1");
		setGlassEnabled(true);
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				hide();
			}
			break;
		}
	}
	
	@UiHandler("btnExit")
	void onBtnExitClick(ClickEvent event) {
		hide();
	}
	
	@UiHandler("btnLogin")
	void onBtnLoginClick(ClickEvent event) {
		LoginFacebook();
		hide();
	}
	
	static void saveNewFacebookUser(final String accessToken, final String userId) {
		TripShare.loadBox.center();
		User facebookUser = new User();
		facebookUser.setId(userId);
		TripShare.dataService.insertUser(facebookUser, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				TripShare.loadBox.hide();
				TripShare.access_token = accessToken;
				TripShare.user_id = userId;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				TripShare.loadBox.hide();
				Window.alert(TripShare.ERROR_MESSAGE);
			}
		});
	}
	
	public static native void LoginFacebook() /*-{
	  	$wnd.FB.login(function(response) {
			if (response.authResponse) {
				var userId = response.authResponse.userID;
				var accessToken = response.authResponse.accessToken;
				$wnd.document.getElementById('menubutton').innerHTML = "Profile";
			    // Call instance method saveNewFacebookUser() on this
				@com.born2go.client.widgets.LoginDialog::saveNewFacebookUser(Ljava/lang/String;Ljava/lang/String;)(accessToken,userId);
			} else {
				 
			}
		});
	}-*/;

}
