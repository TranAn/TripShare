<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy MMM d hh:mm:ss");
%>
<script type="text/javascript">
	function closeBox(id) {
		var e = document.getElementById(id);
		e.style.display = 'none';
	}
</script>

<div class="mheader">
	<div class="header-action">
		<a href="/mhome/"> <i class="fa fa-home fa-3x"></i>
		</a>
	</div>
	<div class="seoHeader">
		<h1>
			Trip&nbsp;<strong>Share</strong>
		</h1>
	</div>
	<div class="logo">
		<img src="/resources/1427111517_palm_tree.png" width="40" height="40" />
	</div>
</div>
<div class="m-ad" id="m-ad">
	<span class="close-banner"
		onclick="closeBox('m-ad');
				return false;"><i
		class="fa fa-times"></i></span>
	<div class="ad-info">
		<a > <marquee>Trip share has app mobile,  install now</marquee></a>
	</div>
</div>