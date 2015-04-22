<%@page import="com.born2go.shared.User"%>
<%@ page import="com.google.gwt.user.client.Window"%>
<%@ page import="com.born2go.shared.Path"%>
<%@ page import="com.born2go.shared.Picture"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>

<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.born2go.shared.Picture"%>
<%@ page import="com.born2go.shared.Journey"%>
<%@ page import="com.born2go.shared.Locate"%>
<%@ page import="com.born2go.shared.User"%>
<%@ page import="com.born2go.server.DataServiceImpl"%>
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
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

<!-- Specify the shortcut icon. -->
<link rel="shortcut icon" href="../favicon.ico" />

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Profile</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript" src="../tripshare/tripshare.nocache.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>
<script type="text/javascript" src="/myjs/facebookConnect.js"></script>
<script src="http://connect.facebook.net/en_US/all.js"></script>

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
					color="#fffbf8">finding your trip</font></span> <span
					style="line-height: 1.1em; font: normal normal normal 55px/1.1em Play, sans-serif; color: #fffbf8">Trip&nbsp;<strong>Share</strong></span>
				<img src="/resources/1427111517_palm_tree.png" height="42"
					width="42" /> <span
					style="margin-left: 20px; letter-spacing: 0.3em; font: normal normal normal 14px/1.3em 'Open Sans', sans-serif"><font
					color="#fffbf8">share with your friend</font></span>
			</center>
		</div>

		<div id="menu">
			<div style="margin: auto; display: -webkit-box;">
				<a class="menubutton" href="/">Home</a>
				<a class="menubutton" href="/create/">Plan your trip</a>
				<a id="menubutton" class="menubutton-actived" href="">Profile</a>
			</div>
		</div>
	</div>

	<!-- Add Content here -->
	<div id="content">
		<div style="position: relative; margin: auto; width: 75%; min-width: 900px; height: auto !important;">
			<div>
				<%!public void redirectHomeUrl(HttpServletResponse response) {
					String site = new String("/");
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", site);
				}%>

				<%
					if (request.getPathInfo() == null
							|| request.getPathInfo().length() == 0) {
						redirectHomeUrl(response);
					} else {
						String userId = request.getPathInfo().replaceAll("/", "");
						DataServiceImpl service = new DataServiceImpl();
						User user = service.findUser(userId);
						if (user == null) {
							redirectHomeUrl(response);
						} else {
				%>
				<%
					java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy MMM d");
				%>
				<table cellspacing="10">
					<tr>
						<td valign="top" style="min-width: 240px;">
							<div class="profile_form" style=" min-height: 250px;">
								<img src="https://graph.facebook.com/<%=userId%>/picture?type=normal" style="width: 50%; height: auto; background: #fff; border: 1px solid #ccc; display: block; margin: auto;" />
								<div class="font_4" style="margin-top: 20px; text-align: center; font-size: 17px;"><%=user.getUserName()%></div>
								<table style="width: 100%;" cellspacing='5'>
									<tr>
										<td style="width: 50%;" class="font-blackTitleNormal">Join Date:</td>
										<% if(user.getJoinDate() != null) { %>
										<td><%=df.format(user.getJoinDate())%></td>
										<%;} else { %>
										<td>from Stone Age</td>
										<%;} %>
									</tr>
									<tr>
										<td class="font-blackTitleNormal">Journeys:</td>
										<td><%=user.getMyTrips().size()%></td>
									</tr>
								</table>
							</div>
						</td>
						<td valign="top" style="width: 100%;">
							<div class="profile_form" id="profileContent"></div>
						</td>
					</tr>
				</table>
				<%
					}
					}
				%>
				<!-- End of jsp content -->
			</div>
		</div>
	</div>
	
	<br />

	<div id="footer" style="width: 100%">
		<center>© Copyright 2015, Born2Go.</center>
	</div>
</body>
</html>
