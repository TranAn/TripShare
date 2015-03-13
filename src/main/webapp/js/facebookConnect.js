window.fbAsyncInit = function() {
	FB.init({
		appId : '386540048195283',
		channelURL : 'http://localhost:8080/', // Channel File
		status : true,
		version : 'v2.2'
	});
};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {
		return;
	}
	js = d.createElement(s);
	js.id = id;
	js.src = "//connect.facebook.net/en_US/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

window.onload = userInfoLoad;

function userInfoLoad() {
	if(location.pathname == "/create/")
		checkUserLogin();
	else
		getLoginStatus();
}

function checkUserLogin() {
	document.body.style.visibility = "hidden";
	if(typeof(Storage) !== "undefined") {
		 if (sessionStorage.userId) {
			 document.body.style.visibility = "visible";
			 setUserInfo(sessionStorage.userName);
	     } else {
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					document.body.style.visibility = "visible";
					setUserInfo(response.name);
					storeSession(response);
				} else if (response.status === 'not_authorized') {
					window.location = "/";
				} else {
					window.location = "/";
				}
			});
	     }
    } else {
    	FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				document.body.style.visibility = "visible";
				setUserInfo(response.name);
			} else if (response.status === 'not_authorized') {
				window.location = "/";
			} else {
				window.location = "/";
			}
		});
    }
}

function getLoginStatus() {
	if(typeof(Storage) !== "undefined") {
		 if (sessionStorage.userId) {
			 setUserInfo(sessionStorage.userName);
	     } else {
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					setUserInfo(response.name);
				} else if (response.status === 'not_authorized') {
				} else {
				}
			});
	     }
   } else {
   		FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				setUserInfo(response.name);
			} else if (response.status === 'not_authorized') {
			} else {
			}
		});
   }
}

function storeSession(response) {
	sessionStorage.userId = response.authResponse.userID;
	sessionStorage.userName = response.authResponse.userName;
}

function loginFacebook() {
	console.log('Log: Connect to facebook.... ');
	FB.login(function(response) {
		if (response.authResponse) {
			FB.api('/me', function(response) {
				var facebookApi = new GWTExport.FacebookApi();
				facebookApi.saveNewFacebookUser(response.id);
				if(typeof(Storage) !== "undefined") {
					sessionStorage.userId = response.id;
					sessionStorage.userName = response.name;
				}
				setUserInfo(response.name);
				console.log(JSON.stringify(response));
			});
		} else {
			console.log('Log: User cancelled login or did not fully authorize.');
		}
	});
}

function logoutfacebook() {
	FB.logout(function(response) {
		if(typeof(Storage) !== "undefined") {
			sessionStorage.clear();
		}
	});
}

function setUserInfo(userName) {
	document.getElementById('signInMenu').innerHTML = userName;
	var li = document.createElement("LI");
	var a = document.createElement("A");
	a.innerHTML = "Log out";
	a.addEventListener("click", function() {
		FB.logout(function(response) {
			if(typeof(Storage) !== "undefined") {
				sessionStorage.clear();
			}
		});
	});
	li.appendChild(a);
	document.getElementById('menu').appendChild(li);
}