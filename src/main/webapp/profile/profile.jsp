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
			 String userId = request.getPathInfo().replaceAll("/", "");
			 RequestDispatcher rd = getServletContext().getRequestDispatcher("/mprofile/"+userId);
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
public void redirectHomeUrl(HttpServletResponse response) {
	String site = new String("/");
	response.setStatus(response.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", site);
}
%>

<%
	User user = new User();
	java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy MMM d");
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() == 0) {
		redirectHomeUrl(response);
	} else {
		String userId = request.getPathInfo().replaceAll("/", "");
		DataServiceImpl service = new DataServiceImpl();
		user = service.findUser(userId);
		if (user == null) {
			redirectHomeUrl(response);
		} else {
		}
	}
%>
				

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
<script type="text/javascript" language="javascript" src="/tripshare/tripshare.nocache.js"></script>
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

	<!-- <script type="text/javascript">
		window.onbeforeunload = function() {
			window.scrollTo(0, 0);
		}
	</script> -->

	<div id="header">
		<div id="title">
			<center>
			<span style="margin-right:20px ;letter-spacing:0.3em; font:normal normal normal 14px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">Plan your trips</font></span>
			<span style="line-height: 1.1em; font:normal normal normal 55px/1.1em Play,sans-serif; color:#fffbf8">Trip&nbsp;<strong>Share</strong></span>
			<img src="/resources/1427111517_palm_tree.png" height="42" width="42" /> 
			<span style="margin-left:20px ;letter-spacing:0.3em; font:normal normal normal 14px/1.3em 'Open Sans',sans-serif"><font color="#fffbf8">Share the moments</font></span>
			</center>
		</div>

		<div id="menu">
			<div style="margin: auto; display: -webkit-box; width: -moz-fit-content;">
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
				<table cellspacing="10">
					<tr>
						<td valign="top" style="min-width: 240px;">
							<div class="profile_form" style=" min-height: 250px;">
								<img src="https://graph.facebook.com/<%=user.getId()%>/picture?type=normal" style="width: 50%; height: auto; background: #fff; border: 1px solid #ccc; display: block; margin: auto;" />
								<div class="font_4" style="margin-top: 20px; text-align: center; font-size: 17px;"><%=user.getUserName()%></div>
								<table style="width: 100%;" cellspacing='5'>
									<tr>
										<td style="width: 50%;" class="font-blackTitleNormal">Join Date:</td>
										<% if(user.getJoinDate() != null) { %>
										<td><%=df.format(user.getJoinDate())%></td>
										<%;} else { %>
										<td>Stone Age</td>
										<%;} %>
									</tr>
									<tr>
										<td class="font-blackTitleNormal">Journeys:</td>
										<td><%=user.getMyTrips().size()%></td>
									</tr>
								</table>
								<div style="margin-top: 10px; margin-bottom:5px; text-align: center; font-size: 14px; padding: 6px 15px;" class="greenbutton" onclick="logoutfacebook()">Log Out</span>
							</div>
						</td>
						<td valign="top" style="width: 100%;">
							<div class="profile_form" id="profileContent"></div>
						</td>
					</tr>
				</table>
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
