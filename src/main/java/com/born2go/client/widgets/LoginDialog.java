package com.born2go.client.widgets;

import com.born2go.client.TripShare;
import com.born2go.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
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
	
	static void saveNewFacebookUser(final String accessToken, final String userId, final String userName) {
		TripShare.loadBox.center();
		User facebookUser = new User();
		facebookUser.setId(userId);
		facebookUser.setUserName(userName);
		TripShare.dataService.insertUser(facebookUser, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				TripShare.loadBox.hide();
				TripShare.getAccessToken(accessToken, userId);
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
				var userImg = new Image(); 
				userImg.src = "https://graph.facebook.com/"+ userId+ "/picture?width=35&height=35";
				userImg.className = "profile_img";
				$wnd.document.getElementById('menubutton').appendChild(userImg);
				var profileHref =  "/profile/"+ userId;
				$wnd.document.getElementById('menubutton').setAttribute('href', profileHref);
			    // Call instance method saveNewFacebookUser() on this
				$wnd.FB.api('/me', function(response) {
					@com.born2go.client.widgets.LoginDialog::saveNewFacebookUser(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(accessToken,userId,response.name);
				});
			} else {
				 
			}
		}, {scope: 'public_profile,user_friends'});
	}-*/;

}
