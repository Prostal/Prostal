<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User page</title>
</head>
	<body>
			<jsp:include page="header.jsp"></jsp:include>
	<hr>
		
		<c:set var = "user" value="${sessionScope.user }"></c:set>
		<c:out value="${user.username }"></c:out>
		<c:out value="${user.email }"></c:out>
		<c:if test="${user.avatarUrl!=null }">
			<img id="avatar" src="getAvatar"  width="50" height= auto>
			<form action="avatarUpload" method="post" enctype="multipart/form-data">
				<input type="file" name="avatar"><br>
				<input type="submit" value="change avatar"><br>
			</form>
		</c:if>
		
		<c:if test="${user.avatarUrl==null }">
		<!-- update session -->
			<form action="avatarUpload" method="post" enctype="multipart/form-data">
				Avatar<input type="file" name="avatar"><br>
				<input type="submit" value="upload"><br>
			</form>
		</c:if>
		
		<hr>
		<hr>
		<c:if test="${user.admin }">
			<jsp:include page="admin.jsp"></jsp:include>
		</c:if>
		<jsp:include page="footer.jsp"></jsp:include>
	</body>
</html>