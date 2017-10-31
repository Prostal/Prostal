<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<link type="text/css" href="css/main.css" rel="stylesheet">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>register</title>
	</head>
	<body>
	<jsp:include page="header.jsp"></jsp:include>
	<hr>
		<c:if test="${ requestScope.error == null }">
			<h3>Welcome, please register!</h3>
		</c:if>
		<c:if test="${ requestScope.error != null }">
			<h1 style="color: red">Sorry, registration unsuccessfull. Reason: ${requestScope.error }</h1>
		</c:if>
		<form action="register" method="post" >
			Username<input type="text" name="username"><br>
			Password<input type="password" name="password"><br>
			Email<input type= "email" name="email"><br>
			<!-- Avatar<input type="file" name="avatar"><br> -->
			<input type="submit" value="Register"><br>
		</form> 
		Already have an account?<a href = "loginPage">Please login here</a>
		
		<jsp:include page="footer.jsp"></jsp:include>
	</body>
</html>