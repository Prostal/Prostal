<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>register</title>
	</head>
	<body>
		<c:if test="${ requestScope.error == null }">
			<h3>Welcome, please register!</h3>
		</c:if>
		<c:if test="${ requestScope.error != null }">
			<h1 style="color: red">Sorry, registration unsuccessfull. Reason: ${requestScope.error }</h1>
		</c:if>

	<form:form commandName="user" > <!-- it is post -->
 		Username<form:input path="username"/><br> <!-- cssClass here -->
 		<%-- <form:errors path="username" /><!-- cssClass="error" -->  --%>
 		Password:<form:password path="password"/><br>
 		Email:<form:input path="email"/><br>
 		<input type="submit" value="Register">			
 	</form:form>
 	

		
		Already have an account?<a href = "loginPage">Please login here</a>
	</body>
</html>