<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<hr>
	<c:forEach items="${sessionScope.articles}" var="article">
				<h1><a href="pickArticle?articleId=${article.id}" > ${article.title } </a></h1>
				<!--<c:out value="${article.title }"></c:out>  <br> -->
				<c:out value="${article.textContent }"></c:out> <br>
				<c:out value="${article.impressions }"></c:out> <br>
				<c:out value="${article.created }"></c:out> <br>
				
				<c:forEach items="${article.mediaFiles}" var="media">
					<c:if test="${!media.isVideo }">
						<img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="320" height= auto><br>
					</c:if>
				</c:forEach>
				<hr>
	 </c:forEach>
	 <hr>
	<jsp:include page="sidemenu.jsp"></jsp:include>

</body>
</html>