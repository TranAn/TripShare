<%@page import="com.born2go.shared.Path"%>
<%@ page import="com.born2go.shared.Trip"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.born2go.server.DataServiceImpl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	if (request.getPathInfo() == null || request.getPathInfo().length() <= 1)  
			redirectHomeUrl(response);
		 
		else {
			String tripId = request.getPathInfo().replaceAll("/", "");
			
			try{
				Long id = Long.valueOf(tripId);
				DataServiceImpl service = new DataServiceImpl();
				Trip trip = service.findTrip(id);
				if (trip == null)  
						redirectHomeUrl(response);
			    else {		
			    	SimpleDateFormat dateFormat1 = new SimpleDateFormat(
		                					"yyyy MMM d hh:mm:ss");
			    	 
					String urlFacebook = request.getRequestURL().toString();
					String dateCreate = "Ngày tạo " + dateFormat1.format( trip.getCreateDate());
					String poster = "Người tạo " + trip.getPoster().getUserName();
					String departureDate = dateFormat1.format( trip.getDepartureDate());
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
<title>Journey</title>

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
	<%@include file="mheader.jsp"%>
	<!-- Add Content here -->
	<div class="mcontent">
		<div>
			<div class="mnametrip">
				<h1><%=trip.getName()%></h1>
			</div>
			<div class="mitalictext">
				<%=dateCreate%></div>
			<div class="mitalictext">
				<%=poster%></div>
			<div class="mpadB10"></div>
			<div class="mtripmap" id="mtripmap"></div>
			<div class="mitinerary">
				<h3>Hành trình:</h3>
			</div>
			<div class="mtrip-destinations">
				<img src="/resources/red-spotlight.png" /> <span><%=trip.getJourney().getLocates().get(0)
							.getAddressName()%></span>
			</div>
			<%
				for (int i = 1; i < trip.getJourney().getLocates().size(); i++) 
																																												     					{
			%>
			<div class="mtrip-destinations">
				<img src="/resources/green-spotlight.png" /> <span> <%=trip.getJourney().getLocates().get(i)
								.getAddressName()%>
				</span>
			</div>
			<%
				}
			%>
			<div class="mitinerary">
				<h3>Ngày khởi hành:</h3>
			</div>
			<div class="mdepartureDate">
				<%=departureDate%>
			</div>
			<div class="mitinerary">
				<h3>Giới thiệu về chuyến đi:</h3>
			</div>
			<p class="mparagraptext">
				<%=trip.getDescription()%>
			</p>
			<div class="mListPath">
				<%
					List<Long> idsPath = trip.getDestination();
					if(idsPath != null && !idsPath.isEmpty()){
						List<Path> listPaths = service.listOfPath(idsPath);
						if(listPaths != null && !listPaths.isEmpty()){										
				%>
				<div class="mlistpath">
					<h3>Những chặng dừng chân:</h3>
				</div>

				<%
					for(int i = 0; i< listPaths.size(); i++)
																																													{
																																														Path path = listPaths.get(i);
				%>
				<div class="mblock">
					<h2><%=path.getLocate().getAddressName()%></h2>
					<table cellpadding="0" cellspacing="0" border="0"
						style="line-height: 17px">
						<tr>
							<td style="padding-right: 5px">
								<div class="mitalictext">Date post</div>
							</td>
							<td>
								<div class="mitalictext"><%=dateFormat.format(path.getCreateDate())%></div>
							</td>
						</tr>
						<tr>
							<td style="padding-right: 5px"><div class="mitalictext">Create
									by</div></td>
							<td><div class="mitalictext"><%=(trip.getPoster() != null ? trip.getPoster()
							.getUserName() : "Tester")%></div></td>
						</tr>
					</table>
					<p class="mparagraptext">
						<%=path.getDescription()%>

					</p>

				</div>
				<%
					}
				%>

				<%
					}																																																								}
				%>
			</div>
			<%
				}
			}
			 catch (Exception e){
					redirectHomeUrl(response);
 										}
		}															
			%>
		</div>

		<!-- End of jsp content -->
	</div>
	<div id = "sidebar"></div>



	<br />

	<%@include file="mfooter.jsp"%>

</body>
</html>
