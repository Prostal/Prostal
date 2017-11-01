<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sportal.bg- Home</title>
<link type="text/css" href="css/main.css" rel="stylesheet">
</head>
<body>

	<!-- page header with logo, search, login, user etc.  -->
	<jsp:include page="header.jsp"></jsp:include>


	<nav> <!-- side menu at the left page side --> 
	<jsp:include
		page="sidemenu.jsp"></jsp:include> </nav>


	<div id="right_body"> <!-- all the rest page content between header and footer-->

	
		<a href="Top5?sort=impressions"><button class="button_most">most
				viewed</button></a> <a href="Top5?sort=impressions"><button
				class="button_most">most commented</button></a> <a
			href="Top5?sort=impressions"><button class="button_most">leading</button></a>
	
	</div>


	<!-- footer with copy rights and cotacts -->
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>