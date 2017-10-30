<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		
		<form action="index">
			<input type="submit" value="Home">
		</form>
		<c:if test="${ sessionScope.user!=null }">
			<form  action="userPage">
				<input type="submit" value="User">
			</form>
		</c:if>
		<c:if test= "${ sessionScope.user==null }">
			<form  action="loginPage">
				<input type="submit" value="log in">
			</form>
		</c:if>
		Welcome, <c:out value="${sessionScope.user.username }">Guest</c:out>
		<c:if test="${ sessionScope.user!=null }">
			<form  action="Logout" method="post">
				<input type="submit" value="log out">
			</form>
		</c:if>
		
		<jsp:include page="search.jsp"></jsp:include>
	</body>
</html>