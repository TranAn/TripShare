window.fbAsyncInit = function() {
    FB.init({
      appId      : '1636504239911870',
      xfbml      : true,
      version    : 'v2.3'
    });
    checkUserLogin();
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

  
function checkUserLogin() {
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
//			console.log(response.authResponse);
			var userId = response.authResponse.userID;
		    var accessToken = response.authResponse.accessToken;
		    var facebookApi = new GWTExport.FacebookApi();
		    facebookApi.getAccessToken(accessToken, userId);
			FB.api('/me', function(response) {
				setUserInfo(response.name);
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


function saveCookie(accessToken) {
	var path = window.location.pathname;
	if(path == "/")
		document.cookie="access_token=" + accessToken;
}

function loginFacebook() {
	FB.login(function(response) {
		if (response.authResponse) {
			var userId = response.authResponse.userID;
			var accessToken = response.authResponse.accessToken;
		    //rpc call save new user
			var facebookApi = new GWTExport.FacebookApi();
			facebookApi.getAccessToken(accessToken, userId);
			facebookApi.saveNewFacebookUser(userId);
			//set user info to view
			FB.api('/me', function(response) {
				setUserInfo(response.name);
			});
		} else {
			 var facebookApi = new GWTExport.FacebookApi();
			 facebookApi.getAccessToken("","");
		}
	});
}

function logoutfacebook() {
	FB.logout(function(response) {
//		document.cookie = "access_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	});
}

function setUserInfo(userName) {
	document.getElementById('menubutton').innerHTML = "Profile";
}