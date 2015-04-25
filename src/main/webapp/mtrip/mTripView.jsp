<%@page import="com.born2go.shared.Path"%>
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
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>

<%
	boolean bMobile = false;
	//Check if we serve mobile or not?
	String ua = request.getHeader("User-Agent").toLowerCase();
	if (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")
	|| ua.substring(0, 4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
		//We are in mobile version, do nothing
		
	} 
	else {
		 // PC
		/*  String serveletPath = null;
		 serveletPath = request.getServletPath().toString();
		 if(serveletPath.equals("/mtrip")){
	 			String redirect = null;
	 			String idTrip = request.getPathInfo();
		        RequestDispatcher rd=request.getRequestDispatcher("/journey");  
		        redirect = "/journey" + idTrip;
		        out.println("redirect: " + redirect);
		        response.sendRedirect(redirect); 
		 } */
		 
		 
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
<link type="text/css" rel="stylesheet" href="../mobile.css">
<link rel="stylesheet" href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

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

	<div id="mheader">
		<div id="mtitle">
			<div style="display: inline-flex; width: 100%;">
				<div style="color: white; margin: 8px;">
					<i class="fa fa-home fa-5x"></i>
				</div>
				<div style="width: 80%; margin-left: 10px;; text-align: left;">
					<span
						style="line-height: 1.1em; font: normal normal normal 65px/1.1em Play, sans-serif; color: #fffbf8">Trip&nbsp;<strong>Share</strong></span>
					<img src="/resources/1427111517_palm_tree.png"
						style="height: 65px; width: 65px" />
				</div>

			</div>

		</div>

	</div>

	<!-- Add Content here -->
	<div id="content">
		<div id="mtripInfo">
			<div>

				<%
					if (request.getPathInfo() == null || request.getPathInfo().length() <= 1) {
																	redirectHomeUrl(response);
														}
														else {
															String tripId = request.getPathInfo().replaceAll("/", "");
															
															try{
																Long id = Long.valueOf(tripId);
																DataServiceImpl service = new DataServiceImpl();
																Trip trip = service.findTrip(id);
																if (trip == null)  
																		redirectHomeUrl(response);
															    else {		
															    	SimpleDateFormat dateFormat = new SimpleDateFormat(
												                					"yyyy MMM d hh:mm:ss");
															    	 
																	String urlFacebook = request.getRequestURL().toString();
				%>

				<h1
					style="margin-bottom: 20px; margin-top: 10px; color: rgb(56, 119, 127); font-family: museo-w01-700, serif;"><%=trip.getName()%></h1>

				<div style="display: inline-flex;">
					<div class="mtextItalic"
						style="margin-bottom: 20px; margin-right: 4px;">Create by</div>
					<div class="mtextItalic" style="margin-bottom: 20px;"><%=(trip.getPoster() != null ? trip.getPoster()
							.getUserName() : "Tester")%></div>
				</div>
				<div id="mtripMap" class="mtripMap"></div>
				<div class="font-blackTitleLarge" style="margin-top:20px;">Hành trình:</div>
				<div class="trip-destinations">
					<img src="/resources/red-spotlight.png"
						style="width: 22px; height: 30px; vertical-align: middle;" /> <span
						style="margin-left: 5px"><%=trip.getJourney().getLocates().get(0)
							.getAddressName()%></span>
				</div>
				<%
					for (int i = 1; i < trip.getJourney().getLocates().size(); i++) 
																														     					{
				%>
				<div class="trip-destinations">
					<img src="/resources/green-spotlight.png"
						style="width: 22px; height: 30px; vertical-align: middle;" /> <span
						style="margin-left: 5px"> <%=trip.getJourney().getLocates().get(i)
								.getAddressName()%>
					</span>
				</div>
				<%
					}
				%>

				 
					<div  >
						<div class="font-blackTitleLarge" style="margin-top: 25px;">Ngày
							khởi hành:</div>

						<div style="font-size: 15px;"><%=dateFormat.format(trip.getDepartureDate())%></div>
					</div>
					 

				 

				
				<div class="font-blackTitleLarge" style="margin-top: 30px;">Giới
					thiệu về chuyến đi:</div>
				<div
					style="font-size: 15px; line-height: 1.8em; white-space: pre-line;"><%=trip.getDescription()%></div>

				<div id="mListPath" class="mListPath"
					style="border-top-style: dotted solid;">
					<%
						List<Long> idsPath = trip.getDestination();
																			if(idsPath != null && !idsPath.isEmpty()){
																				List<Path> listPaths = service.listOfPath(idsPath);
																				if(listPaths != null && !listPaths.isEmpty()){
					%>
					<div class="font-blackTitleLarge"
						style="margin-top: 30px; padding-bottom: 20px;">Những chặng
						dừng chân</div>
					<%
						for(int i = 0; i< listPaths.size(); i++)
																			{
																				Path path = listPaths.get(i);
					%>
					<div
						style="border-top: 5px dotted rgba(161, 161, 161, 1); padding-bottom: 20px;">
						<h2
							style="margin-bottom: 20px; margin-top: 10px; color: rgb(56, 119, 127); font-family: museo-w01-700, serif; margin-top: 20px;"><%=path.getLocate().getAddressName()%></h2>
						<div style="display: -webkit-box;">
							<div class="mtextItalic" style="margin-right: 4px; font-size:40px;">Date
								post</div>
							<div class="mtextItalic" style="font-size:40px;"><%=dateFormat.format(path.getCreateDate())%></div>
						</div>
						<div style="display: inline-flex; margin-bottom: 15px;">
							<div class="mtextItalic"
								style="margin-right: 4px; margin-left: 5px;font-size:40px;">Create by</div>
							<div class="mtextItalic" style="font-size:40px;"><%=(trip.getPoster() != null ? trip.getPoster()
							.getUserName() : "Tester")%></div>
						</div>
						<p
							style="white-space: pre-line; font-size: 15px; line-height: 1.6em;"><%=path.getDescription()%></p>


					</div>
					<%
						}
					%>

					<%
						}
																																																															}
					%>
				</div>


				 



				<%
					}
					}
					 catch (Exception e){
													            /* out.println("An exception occurred: " + e.getMessage()); */
					redirectHomeUrl(response);
  }
		}
				%>
			</div>

			<!-- End of jsp content -->
		</div>


	</div>

	<br />

	<div id="mfooter" style="width: 100%">
		<center>© Copyright 2015, Born2Go.</center>
	</div>

</body>
</html>
