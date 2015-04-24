/*window.fbAsyncInit = function() {
    FB.init({
      appId      : '386540048195283',
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
	FB.login(function(response) {
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
	});
}

function logoutfacebook() {
	FB.logout(function(response) {
		/*document.cookie = "access_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";*/
	});
}

function setUserInfo(userId, userName) {
	var menuProfile = document.getElementById('menubutton');
	menuProfile.innerHTML = "Profile";
	menuProfile.removeEventListener("click", loginFacebook);
	var profileHref =  "/profile/"+ userId;
	menuProfile.setAttribute('href', profileHref);
}

function addSharingMeta(title, imgUrl, link, description) {
	var meta_Type = document.createElement('meta');
	meta_Type.setAttribute("property","og:type");
	meta_Type.content = "article";
	document.getElementsByTagName('head')[0].appendChild(meta_Type);
	
	var meta_Title = document.createElement('meta');
	meta_Title.setAttribute("property","og:title");
	meta_Title.content = title;
	document.getElementsByTagName('head')[0].appendChild(meta_Title);
	document.title = title;
	
	var meta_ImgUrl = document.createElement('meta');
	meta_ImgUrl.setAttribute("property","og:image");
	meta_ImgUrl.content = imgUrl;
	document.getElementsByTagName('head')[0].appendChild(meta_ImgUrl);
	
	var meta_Link = document.createElement('meta');
	meta_Link.setAttribute("property","og:url");
	meta_Link.content = link;
	document.getElementsByTagName('head')[0].appendChild(meta_Link);
	
	var meta_Description = document.createElement('meta');
	meta_Description.setAttribute("property","og:description");
	meta_Description.content = description;
	document.getElementsByTagName('head')[0].appendChild(meta_Description);
}

