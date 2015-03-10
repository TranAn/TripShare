window.fbAsyncInit = function() {
	console.log('Init Facebook !.... ');
	FB.init({
		appId : '961318463908824',
		channelURL : 'http://localhost:8080/', // Channel File
		status : true,
		version : 'v2.2'
	});
	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
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

function loginFacebook() {
	console.log('Welcome!  Fetching your information.... ');
	FB.login(function(response) {
		if (response.authResponse) {
			getUserNameLogin(response);
		} else {
			console.log('User cancelled login or did not fully authorize.');

		}
	});
}
function getUserNameLogin(response) {
	FB.api('/me', function(response) {
		console.log('Good to see you, ' + response.name + '.');
		document.getElementById('signIn').innerHTML = response.name;
		console.log(JSON.stringify(response));
	});

}
function statusChangeCallback(response) {
	console.log('statusChangeCallback');
	if (response.status === 'connected') {
		// Logged into your app and Facebook.
		console.log('connected');
		getUserNameLogin(response);
		console.log('Access Token: ' + response.authResponse.accessToken);
		console.log('ExpiresIn:' + response.authResponse.expiresIn);

	} else if (response.status === 'not_authorized') {
		// The person is logged into Facebook, but not your app.
		console.log('Please log ' + 'into this app.');

	} else {
		// The person is not logged into Facebook, so we're not sure if
		// they are logged into this app or not.
		console.log('Please log ' + 'into this app.2');

	}
}
function logoutfacebook() {
	FB.logout(function(response) {

	});
}