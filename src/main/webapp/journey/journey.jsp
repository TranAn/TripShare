<%@page import="java.io.IOException"%>
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

<%
	//Check if we serve mobile or not?
	String ua=request.getHeader("User-Agent").toLowerCase();
	if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
		//response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		//response.setHeader("Location", request.getRequestURI().replaceFirst("journey", "mobile"));
		 if (request.getPathInfo() == null || request.getPathInfo().length() <= 1) {
				redirectHomeUrl(response);
		 }
		 else{
			 String tripId = request.getPathInfo().replaceAll("/", "");
			 RequestDispatcher rd = getServletContext().getRequestDispatcher("/mtrip/"+ tripId);
				if (rd != null){
					rd.forward(request, response);
				}
				else {
					response.setStatus(response.SC_OK);
					redirectHomeUrl(response);
				}
		 } 
	}
%>

<%!
	//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");	
		try {
			response.getWriter().print("<h1>NOT_FOUND</h1>");
		} catch(IOException e) {}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setHeader("Location", site);
	}
%>

<%
	//Global variable
	Trip trip = new Trip();
	String tripId = "";
	String tripAvatar = "";
	java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy MMM d - hh:mm:ss");
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() < 1) {
		redirectHomeUrl(response);
		return;
	} else {
		tripId = request.getPathInfo().replaceAll("/", "");
		DataServiceImpl service = new DataServiceImpl();
		Long trip_id = 1L;
		try{
			trip_id = Long.valueOf(tripId);
		} catch (NumberFormatException e) {
			redirectHomeUrl(response);
			return;
		}
		trip = service.findTrip(trip_id);
		if (trip == null) {
			redirectHomeUrl(response);
			return;
		} else {
		}
	}
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

<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<!-- <link type="text/css" rel="stylesheet" href=""> 			   -->
<link type="text/css" rel="stylesheet" href="../main.css">
<link rel="stylesheet" href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

<!-- Specify the shortcut icon. -->
<link rel="shortcut icon" href="../favicon.ico" />

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title><%=trip.getName()%></title>
<meta property="og:title" content="<%=trip.getName().replaceAll("\"", "\'")%>" />
<meta property="og:type" content="article" />
<meta property="og:image" content="<%= tripAvatar %>" />
<meta property="og:url" content="http://born2go-b.appspot.com/journey/<%= tripId %>" />
<%if(trip.getDescription() != null) { %>
<meta property="og:description" content='<%=trip.getDescription().replaceAll("\'", "\"").replace("\n", "").replace("\r", "")%>' />
<%;} %>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript" src="/tripshare/tripshare.nocache.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>
<script type="text/javascript" src="/myjs/facebookConnect.js"></script>
<script type="text/javascript" src="http://slideshow.triptracker.net/slide.js"></script>
<script type="text/javascript" src="/resources/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="/resources/plupload/plupload.full.min.js"></script>
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
<body style="margin: 0px; background: none;" class="journeypage_theme">

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
		window.onbeforeunload = function(){
			window.scrollTo(0,0);
		}
	</script>
	
	<%
	if(trip.getTheme() == null) {
	%>
		<script type="text/javascript">
			document.body.className = "journeypage_default_theme";
		</script>
	<%
	}
	else {
		switch (trip.getTheme()) {
		case DEFAULT:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_default_theme";
			</script>
		<%
			break;
		case BEACH_DAWN:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_beachdawn_theme";
			</script>
		<%
			break;
		case BEACH_MORNING:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_beachmorning_theme";
			</script>
		<%
			break;
		case BEACH_SUNSET:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_beachsunset_theme";
			</script>
		<%
			break;
		case CITY:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_city_theme";
			</script>
		<%
			break;
		case COUNTRY:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_country_theme";
			</script>
		<%
			break;
		case FOREST:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_forest_theme";
			</script>
		<%
			break;
		case FOREST_WATERFALL:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_forestwaterfall_theme";
			</script>
		<%
			break;
		case MOUNTAIN_SPRING:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_mountainspring_theme";
			</script>
		<%
			break;
		case MOUNTAIN_WINTER:
		%>
			<script type="text/javascript">
				document.body.className = "journeypage_mountainwinter_theme";
			</script>
		<%
			break;
		} 
	}
	%>

	<div id="header" style="margin:0px; height: 130px;">
		<div style="width:86%; margin: auto;">
			<div id="title">
				<img style="float:left; height:76px; width:80px;" src="/resources/1427111517_palm_tree.png" />
				<div style="float:left;  margin-left: 10px;">
				<div style="margin-bottom: 5px;">
				<span style="line-height: 1.1em; font:normal normal normal 50px/1.1em Play,sans-serif; color:#fffbf8">Trip&nbsp;<strong>Share</strong></span>
				</div>
				<div>
				<span style="margin-right:20px ;letter-spacing:0.3em; font:normal normal normal 13px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">Plan your trips & Share the moments</font></span>
				</div>
				 
				<!-- <span style="margin-left:20px ;letter-spacing:0.3em; font:normal normal normal 14px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">Share the moments</font></span> -->
				</div>
				
			</div>
	
			<div id="menu">
				<div style="margin:auto; display: -webkit-box; width: -moz-fit-content; overflow: hidden;">
				<a class="menubutton-actived" href="/">Home</a>
				<a class="menubutton" href="/create/">Plan your trip</a>
				<a id="menubutton" class="menubutton" onclick="loginFacebook()">Sign in Facebook</a>
				</div>
			</div>
		</div>
	</div>

	<!-- Add Content here -->
	<div id="content">
		<div id="tripInfo" class="tripInfo">
			<div>	
				<img src="/resources/1411646988_note2.png" width="36px" height="36px" style="position: absolute; top: 4px; right: 2px; z-index: 1;"/>			
				<table>
				<tr>
				<td valign="top" style="width:100%">
					<div class="font-blackTitleLarge" style="font-size: 30px;font-family: museo-w01-700,serif; color:rgb(56, 119, 127)"><%=trip.getName()%></div>
					<div style="float: right; margin-top: 12px;">
						<div class="fb-like" data-href="http://born2go-b.appspot.com/journey/<%= tripId %>" data-layout="button_count" data-action="like" data-show-faces="true" data-share="true"></div>
					</div>
					
					<%if(trip.getPoster() != null) {%>
					<div style="display: inline-flex; display: -webkit-inline-box; display: -webkit-inline-flex; display: -ms-inline-flexbox; -webkit-align-self: auto;">
						<div class="font-blackTitleLarge" style="margin-top: 12px;margin-bottom: 20px;margin-right: 4px;font-size: 15px;font-family: museo-w01-700,serif; color:gray; font-style: italic;">Create by:</div>
						<a href="/profile/<%=trip.getPoster().getUserID()%>" class="font-blackTitleLarge link" style="margin-top: 12px;;margin-bottom: 20px;font-size: 15px;font-family: museo-w01-700,serif; color: cornflowerblue; font-style: italic;"><%=(trip.getPoster()!=null?trip.getPoster().getUserName():"Tester")%></a>
						<img style="border: 1px silver solid;border-radius: 20px;overflow: hidden;margin-left: 8px;margin-bottom: 15px;" src="https://graph.facebook.com/<%=trip.getPoster().getUserID()%>/picture?width=25&height=25" />					
					</div>
					<%} %>
					
					<%if(trip.getJourney() !=null) {%> 
					<div class="font-blackTitleLarge">Itinerary:</div>
					<div class="trip-destinations">
						<img src="/resources/red-spotlight.png" style="width:22px;height:30px;vertical-align: middle;"/>
						<span style="margin-left:5px"><%=trip.getJourney().getLocates().get(0).getAddressName()%></span>
					</div>
					<%for (int i = 1; i < trip.getJourney().getLocates().size(); i++) { %>
					<div class="trip-destinations"><img src="/resources/green-spotlight.png" style="width:22px;height:30px;vertical-align: middle;"/> <span style="margin-left:5px"> <%= trip.getJourney().getLocates().get(i).getAddressName() %> </span></div>
					<%} }%>
					
					<%if(trip.getDepartureDate() != null) {%>
					<div class="font-blackTitleLarge" style="margin-top:25px;">Departure date:</div>
					<div class="trip-destinations">
						<img src="/resources/1430221613_schedule.png" style="width:22px;height:30px;vertical-align: middle;"/> 
						<span style="margin-left:5px"><%= df.format(trip.getDepartureDate()) %></span>
					</div>
					<%} %>
					
					<%-- <%if(!trip.getCompanion().isEmpty()) { %> --%>
					<div class="font-blackTitleLarge" style="margin-top:30px;">Companion:</div>
					<div class="trip-destinations">
						<img src="/resources/1432155993_WLM.png" style="width:22px;height:28px;vertical-align: middle;float: left;margin-right: 6px;"/>
						<div id="companion_table"></div>	
					</div>	
					<%-- <%} %>	 --%>
				</td>
				
				<td valign="top" height="1">
					<div id="tripMap" class="tripMap" style="width: 500px; height: 100%; margin-left: 10px; min-height: 400px; max-height: 500px; border:1px solid gray;"></div>
				</td>
				</tr>
				
				<tr>
					<td colspan="2">
						<div class="tripInfoBorder"></div>
						<div class="font-blackTitleLarge" style="margin-top:30px;">Journey description:</div>
						<div style="font-size: 15px; line-height: 1.6em;"><%= trip.getDescription() %></div>
					</td>
				</tr>
				</table>
			</div>
		</div>
		
		<div id="tripcontent" class="tripContent"></div>
		
		<div id="commentBox" class="commentBox">
			<div class="fb-comments" data-width="100%" data-href="http://born2go-b.appspot.com/journey/<%= trip.getId() %>" data-numposts="5" data-colorscheme="light" data-order-by="reverse_time" data-version="v2.3"></div>
		</div>
	</div>
	
	<div id="footer" style="width: 100%; margin:0px; background-color: #313131;">
		<div style="color: white; font: 300 14px/14px 'Roboto', sans-serif;width: 336px;margin: auto;"> Born2Go © 2015 All Rights Reserved  |  Privacy policy </div>
	</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script type="text/javascript" src="http://arrow.scrolltotop.com/arrow86.js"></script>
	<noscript>Not seeing a <a href="http://www.scrolltotop.com/">Scroll to Top Button</a>? Go to our FAQ page for more info.</noscript>
	
</body>
</html>
