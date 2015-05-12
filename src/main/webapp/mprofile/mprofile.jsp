<%@page import="com.born2go.shared.User"%>
<%@ page import="com.born2go.shared.Trip"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.born2go.server.DataServiceImpl"%>
<%@ page import="java.util.ArrayList"%>
<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	if (request.getPathInfo() == null
		|| request.getPathInfo().length() <= 1) {
	redirectHomeUrl(response);
		} else {
	String userId = request.getPathInfo().replaceAll("/", "");
	try {
		Long id = Long.valueOf(userId);
		DataServiceImpl service = new DataServiceImpl();
		User user = service.findUser(id.toString());
		if (user == null)
	redirectHomeUrl(response);
		else {
	String headerText = "Những chuyến đi của " + user.getUserName();
%>
<!doctype html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<!-- <link type="text/css" rel="stylesheet" href=""> 			   -->
<link type="text/css" rel="stylesheet" href="../mdevice.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

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
<script type="text/javascript" language="javascript"
	src="../tripshare/tripshare.nocache.js"></script>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script type="text/javascript" src="/myjs/facebookConnect.js"></script>
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>

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
	<div id="fb-root"></div>
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
	<%@include file="../mcommon/mheader.jsp"%>
	<!-- Add Content here -->
	<div class="mcontent">
			<div class="mitinerary">
				<h2><%=headerText%></h2>
			</div>
			<ul>
				<%
					List<Trip> trips = new ArrayList<Trip>();
					if(user.getMyTrips() != null && !user.getMyTrips().isEmpty())
					{
						trips = service.listOfTrip(user.getMyTrips());
						if(trips != null && !trips.isEmpty()){
							for(int i = 0; i < trips.size(); i++){
							Trip trip = trips.get(i);
							String des = trip.getDescription();
							String textShow = null;
							 
							String hrefShow = "/journey/"+ trip.getId().toString();
							if(des != null && des.length()!= 0 ){
								if(des.length()< 200)
									textShow = des;
								else
									textShow = des.substring(0, 200) + "...";
							}
							String dateCreate = "Ngày tạo " + dateFormat.format( trip.getCreateDate());						
				%>
				<li>
					<div class="mnametrip">
						<h1>
							<a href=<%=hrefShow%>><%=trip.getName()%></a>
						</h1>
					</div>
					<div class="mitalictext">
						<%=dateCreate%></div>
					<div class="imgdefault">
						<a><img alt="Du lịch thế giới"
							src="/resources/Travel tips2.jpg"
							style="color: rgb(56, 119, 127);"></a>
						<p class="mparagraptext">
							<%=textShow%>
						</p>
					</div>
				</li>
			</ul>
			<%
				}
									}
								}
							}
						} catch (NumberFormatException e) {
						 
							System.out.println(e.getMessage());
						}
							}
			%>
		<!-- End of jsp content -->
	</div>
	<br />
	<%@include file="../mcommon/mfooter.jsp"%>
</body>
</html>
