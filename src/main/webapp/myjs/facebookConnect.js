/*window.fbAsyncInit = function() {
    FB.init({
      appId      : '1636504239911870',
      cookie	 : true,
      xfbml      : true,
      version    : 'v2.3'
    });
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));*/

function checkUserStatus() {
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			/*console.log(response.authResponse);*/
			var userId = response.authResponse.userID;
		    var accessToken = response.authResponse.accessToken;
		    var facebookApi = new GWTExport.FacebookApi();
		    facebookApi.getAccessToken(accessToken, userId);
			FB.api('/me', function(response) {
				setUserInfo(userId, response.name);
			});
		} else if (response.status === 'not_authorized') {
			 var facebookApi = new GWTExport.FacebookApi();
			 facebookApi.getAccessToken("","");
		} else {
			 var facebookApi = new GWTExport.FacebookApi();
			 facebookApi.getAccessToken("","");
		}
	}, true);
}

function saveFacebookCookie(accessToken) {
	var path = window.location.pathname;
	if(path == "/")
		document.cookie="access_token=" + accessToken;
}

function loginFacebook() {
	 var facebookApi = new GWTExport.FacebookApi();
	 facebookApi.logIn();
	/*FB.login(function(response) {
		if (response.authResponse) {
			var userId = response.authResponse.userID;
			var accessToken = response.authResponse.accessToken;
		    //rpc call save new user
			var facebookApi = new GWTExport.FacebookApi();
			facebookApi.getAccessToken(accessToken, userId);
			//set user info to view
			FB.api('/me', function(response) {
				setUserInfo(userId, response.name);
				facebookApi.saveNewFacebookUser(userId, response.name);
			});
		} else {
			 var facebookApi = new GWTExport.FacebookApi();
			 facebookApi.getAccessToken("","");
		}
	}, {scope: 'public_profile,user_friends'});*/
}

function logoutfacebook() {
	FB.logout(function(response) {
		window.location = "/";
	});
}

function setUserInfo(userId, userName) {
	var menuProfile = document.getElementById('menubutton');
	menuProfile.innerHTML = "Profile";
	var profileHref =  "/profile/"+ userId;
	menuProfile.setAttribute('href', profileHref);
}

