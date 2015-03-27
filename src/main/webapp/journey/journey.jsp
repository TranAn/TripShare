<%@ page import="com.google.gwt.user.client.Window"%>
<%@ page import="com.itpro.tripshare.shared.Trip"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.itpro.tripshare.shared.Picture"%>
<%@ page import="com.itpro.tripshare.shared.Journey"%>
<%@ page import="com.itpro.tripshare.shared.Locate"%>
<%@ page import="com.itpro.tripshare.server.DataServiceImpl"%>
<%@ page import="java.util.ArrayList"%>

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

<!-- Specify the shortcut icon. -->
<link rel="shortcut icon" href="../favicon.ico" />

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Trip</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" src="http://slideshow.triptracker.net/slide.js"></script>
<script type="text/javascript" language="javascript" src="../script/script.nocache.js"></script>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script type="text/javascript" src="/js/facebookConnect.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>

<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

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

	<div id="header">
		<div id="title">
			<center>
			<span style="margin-right:20px ;letter-spacing:0.3em; font:normal normal normal 14px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">finding your trip</font></span>
			<span style="line-height: 1.1em; font:normal normal normal 55px/1.1em Play,sans-serif; color:#fffbf8">Trip&nbsp;<strong>Share</strong></span>
			<img src="/resources/1427111517_palm_tree.png" height="42" width="42" /> 
			<span style="margin-left:20px ;letter-spacing:0.3em; font:normal normal normal 14px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">share with your friend</font></span>
			</center>
		</div>

		<div id="menu">
			<div style="margin:auto; display: -webkit-box;">
			<li><a class="menubutton-actived" href="/">Home</a></li>
			<li><a class="menubutton" href="/create">Plan your trip</a></li>
			<li><a class="menubutton" href="/">Profile</a></li>
			<!-- <li><a id="signInMenu" onclick="loginFacebook()">Sign In</a></li> -->
			</div>
		</div>
	</div>
	
	<!-- Add Content here -->
	<div id="content">
		<div class="tripInfo">
			<div>
				<%!public void redirectHomeUrl(HttpServletResponse response) {
					String site = new String("/");
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", site);
				}%>
				
				<%
					if (request.getPathInfo() == null
							|| request.getPathInfo().length() <= 1) {
						redirectHomeUrl(response);
					} else {
						String tripId = request.getPathInfo().replaceAll("/", "");
						DataServiceImpl service = new DataServiceImpl();
						Trip trip = service.findTrip(Long.valueOf(tripId));
						if (trip == null) {
							redirectHomeUrl(response);
		
						} else {
							%>							
								<table>
								<tr>
								<td valign="top" style="width:100%">
									<div class="font-blackTitleLarge" style="margin-bottom: 20px;font-size: 30px;font-family: museo-w01-700,serif; color:rgb(56, 119, 127)"><%=trip.getName()%></div>
									<div class="font-blackTitleLarge">Hành trình:</div>
									<div class="trip-destinations">
									<img src="/resources/red-spotlight.png" style="width:22px;height:30px;vertical-align: middle;"/> 
									<span style="margin-left:5px"><%=trip.getJourney().getLocates().get(0).getAddressName()%></span>
									</div>
								<%
									for (int i = 1; i < trip.getJourney().getLocates().size(); i++) {
								%>
										<div class="trip-destinations"><img src="/resources/green-spotlight.png" style="width:22px;height:30px;vertical-align: middle;"/> <span style="margin-left:5px"> <%= trip.getJourney().getLocates().get(i).getAddressName() %> </span></div>
								<%
									}
								%>
									<div class="font-blackTitleLarge" style="margin-top:25px;">Ngày khởi hành:</div>
									<div style="font-size:15px;"><%= trip.getDepartureDate() %></div>
									<div class="font-blackTitleLarge" style="margin-top:30px;">Giới thiệu về chuyến đi:</div>
									<div style="font-size:15px; line-height: 1.8em;"><%= trip.getDescription() %></div>
								</td>
								<td height="1">
									<div id="tripMap" class="tripMap" style="width: 500px; height: 100%; margin-left: 10px; min-height: 450px; border:1px solid gray;"></div>
								</td>
								</tr>
								</table>
							<% 
						}
					}
				%>
			</div>
	
		<!-- End of jsp content -->
		</div>
		
		<div id="tripcontent" class="tripContent"></div>
	</div>
	
	<br />
	
	<div id="footer" style="width: 100%">
		<center> © Copyright 2015, ITPRO. </center>
	</div>

</body>
</html>
