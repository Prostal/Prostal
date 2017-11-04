<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
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
			Username<input type="text" name="username" required><br>
			Password<input type="password" name="password" required><br>
			Email<input type= "email" name="email" required><br>
			<!-- Avatar<input type="file" name="avatar"><br> -->
			<input type="submit" value="Register"><br>
		</form> 
		Already have an account?<a href = "loginPage">Please login here</a>
		<!-- "^@[A-Za-z0-9_]{1,15}$" -->
		<jsp:include page="footer.jsp"></jsp:include>
	</body>
</html>