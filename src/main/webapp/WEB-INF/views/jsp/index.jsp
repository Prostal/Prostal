<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sportal.bg- Home</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<hr>
		<a href="Top5?sort=impressions"><button>most viewed</button></a>
		<a href="Top5?sort=impressions"><button>most commented</button></a>
		<a href="Top5?sort=impressions"><button>leading</button></a>
	<hr>
	<jsp:include page="sidemenu.jsp"></jsp:include>
	
	
	
</body>
</html>