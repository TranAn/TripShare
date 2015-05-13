<%@ page import="com.google.gwt.user.client.Window"%>
<%@ page import="com.born2go.shared.Trip"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.born2go.shared.Picture"%>
<%@ page import="com.born2go.shared.Journey"%>
<%@ page import="com.born2go.shared.Locate"%>
<%@ page import="com.born2go.server.DataServiceImpl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<!doctype html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<!-- <link type="text/css" rel="stylesheet" href=""> 			   -->
<link type="text/css" rel="stylesheet" href="../main.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

<!-- Specify the shortcut icon. -->
<link rel="shortcut icon" href="../favicon.ico" />

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Journey</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript"
	src="/tripshare/tripshare.nocache.js"></script>
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>
<script type="text/javascript" src="/myjs/facebookConnect.js"></script>
<script type="text/javascript"
	src="http://slideshow.triptracker.net/slide.js"></script>
<script type="text/javascript" src="/resources/ckeditor/ckeditor.js"></script>
<script src="http://connect.facebook.net/en_US/all.js"></script>

<!-- <script>
	(function(i, s, o, g, r, a, m) {
		i['GoogleAnalyticsObject'] = r;
		i[r] = i[r] || function() {
			(i[r].q = i[r].q || []).push(arguments)
		}, i[r].l = 1 * new Date();
		a = s.createElement(o), m = s.getElementsByTagName(o)[0];
		a.async = 1;
		a.src = g;
		m.parentNode.insertBefore(a, m)
	})(window, document, 'script', '//www.google-analytics.com/analytics.js',
			'ga');

	ga('create', 'UA-60355111-1', 'auto');
	ga('send', 'pageview');
</script> -->
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body style="margin: 0px; background: none;">

	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>

	<script type="text/javascript">
		window.onbeforeunload = function() {
			window.scrollTo(0, 0);
		}
	</script>

	<div id="header">
		<div id="title">
			<center>
				<span
					style="margin-right: 20px; letter-spacing: 0.3em; font: normal normal normal 14px/1.3em 'Open Sans', sans-serif"><font
					color="#fffbf8">Plan your trips</font></span> <span
					style="line-height: 1.1em; font: normal normal normal 55px/1.1em Play, sans-serif; color: #fffbf8">Trip&nbsp;<strong>Share</strong></span>
				<img src="/resources/1427111517_palm_tree.png" height="42"
					width="42" /> <span
					style="margin-left: 20px; letter-spacing: 0.3em; font: normal normal normal 14px/1.3em 'Open Sans', sans-serif"><font
					color="#fffbf8">Share the moments</font></span>
			</center>
		</div>

		<div id="menu">
			<div
				style="margin: auto; display: -webkit-box; width: -moz-fit-content;">
				<a class="menubutton-actived" href="/">Home</a> <a
					class="menubutton" href="/create/">Plan your trip</a> <a
					id="menubutton" class="menubutton" onclick="loginFacebook()">Sign
					in Facebook</a>
			</div>
		</div>
	</div>

	<!-- Add Content here -->
	<div id="content">
		<div id="summary"></div>

	</div>

	<div id="footer" style="width: 100%">
		<center>© Copyright 2015, Born2Go.</center>
	</div>

	<script type="text/javascript"
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript"
		src="http://arrow.scrolltotop.com/arrow50.js"></script>
	<noscript>
		Not seeing a <a href="http://www.scrolltotop.com/">Scroll to Top
			Button</a>? Go to our FAQ page for more info.
	</noscript>

</body>
</html>
