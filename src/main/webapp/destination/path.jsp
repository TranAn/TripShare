<%@ page import="com.google.gwt.user.client.Window"%>
<%@ page import="com.born2go.shared.Path"%>
<%@ page import="com.born2go.shared.Picture"%>
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
<title>Destination</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript"src="../script/script.nocache.js"></script>
<!-- <script src="http://connect.facebook.net/en_US/all.js"></script> -->
<script type="text/javascript" src="/js/facebookConnect.js"></script>
<script type="text/javascript"src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false;key=AIzaSyCwX2qpyTev25qwNaBxFXBbgIhbPtFeLHw"></script>
<script type="text/javascript"src="http://slideshow.triptracker.net/slide.js"></script>

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
				<li><a class="menubutton-actived" href="/">Home</a></li>
				<li><a class="menubutton" href="/create/">Plan your trip</a></li>
				<li><a id="menubutton" class="menubutton" href="/">Profile</a></li>
				<!-- <li><a id="signInMenu" onclick="loginFacebook()">Sign In</a></li> -->
			</div>
		</div>
	</div>

	<!-- Add Content here -->
	<div id="content">
		<div class="pathInfo">
			<div>
				<%!public void redirectHomeUrl(HttpServletResponse response) {
					String site = new String("/");
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", site);
				}%>

				<%
					if (request.getPathInfo() == null || request.getPathInfo().length() <= 1) {
						redirectHomeUrl(response);
					} else {
						String pathId = request.getPathInfo().replaceAll("/", "");
						DataServiceImpl service = new DataServiceImpl();
						Path path = service.findPart(Long.valueOf(pathId));
						if (path == null) {
							redirectHomeUrl(response);
						} else {
				%>
				<table>
					<tr>
						<td valign="top" style="padding-right: 10px;">
							<div class="left_rightPath">
								<div class="font_4 ">
								<%if(path.getTitle() != null) { %>
									<%=path.getTitle()%>
								<%} else { %>
									<%=path.getLocate().getAddressName()%>
								<% } %>
								</div>
								<div style="">
									<%
										java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy MMM d hh:mm:ss");
									%>
									<p class="font_9">
										Post by: <a href=""><%=path.getPoster().getUserName()%></a>
									</p>
									<p class="font_9">
										Date post:
										<%=df.format(path.getCreateDate())%></p>
								</div>
								<div style="min-height: 10px;">
									<%
										List<Long> gallery = path.getGallery();
																																																				
										if(gallery != null && !gallery.isEmpty()){
											List<Picture> listPicture = service.listPicture(gallery);
											if(listPicture.size() > 0) {
											 	Picture randomP = listPicture.get(0);																																													/* out.println("Results for:<b> " + totalRow + size + "</b>"); */
									%>
									<img src="<%=randomP.getServeUrl()%>" class="one_imageView">
									<%
										}
											}
									%>
								</div>

								<div class="font-blackTitleLarge" style="margin-top: 30px; color: gray;">Mô tả:</div>
								<p
									style="font-size: 15px; line-height: 1.6em; white-space: pre-line;"><%=path.getDescription()%></p>
								<div 
									class="font-blackTitleLarge" style="margin-top: 30px; color: gray;">Gallery:</div>
								<div
									style="with:100%; border: 1px solid #ccc;   margin: 15px 0px" id="gallery" class="gallery"></div>
									
								<div id="facebook">
									<div class="fb-like"
										data-href="http://born2go-b.appspot.com/destination/<%= pathId %>"
										data-layout="standard" data-action="like"
										data-show-faces="true" data-share="true"></div>
									<div class="fb-comments"
										data-href="http://born2go-b.appspot.com/destination/<%= pathId %>"
										data-numposts="5" data-colorscheme="light" data-order-by="reverse_time"></div>
								</div>

							</div>
						</td>
						
						<td valign="top" style="width: 240px;">
							<div class="left_rightPath">
								<div>
									<div style="margin-top: 20px;" class="font_6">ONTRIP
										POSTS:</div>
									<ul class="a">
										<li class="font_4" style="font-size: 14px"><a>Path 1
												Home - Office</a></li>
										<li class="font_4" style="font-size: 14px"><a>Path 2
												Home - Office</a></li>
										<li class="font_4" style="font-size: 14px"><a>Path 3
												Home - Office</a></li>
									</ul>
								</div>
								<hr style="margin-bottom: 20px;" />

								<div>
									<div style="margin-top: 20px;" class="font_6">FOLLOW ME:</div>
									<div class="image"
										style="width: 131px; height: 146px; margin: auto; padding-top: 20px;">
										<img src="/resources/Travel tips2_resize.jpg"
											style="margin-top: 0px; margin-left: 0px; width: 131px; height: 146px;" />
									</div>
									<div style="margin-top: 20px">
										<div align="left">FaceBook</div>
										<div align="center" style="margin-top: -18px;">Twinter</div>
										<div align="right" style="margin-top: -18px;">Google +</div>
									</div>
								</div>
								<hr style="margin-bottom: 20px;" />

								<div>
									<div style="margin-top: 20px;" class="font_6">RELATIVE
										POST:</div>
									<ul class="a">
										<li class="font_4" style="font-size: 14px"><a>Path 1
												Home - Office</a></li>
										<li class="font_4" style="font-size: 14px"><a>Path 2
												Home - Office</a></li>
										<li class="font_4" style="font-size: 14px"><a>Path 3
												Home - Office</a></li>
									</ul>
								</div>
								<%-- <div class="font-blackTitleLarge" style="margin-top: 30px;">Mô
									tả:</div>
								<div
									style="font-size: 15px; line-height: 1.8em; white-space: pre-line;"><%=path.getDescription()%></div> --%>
							</div>
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
