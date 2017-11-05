<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Top 5</title>
</head>
<body>

		<jsp:include page="header.jsp"></jsp:include>
		
		
	<nav> <!-- side menu at the left page side --> 
	<jsp:include
		page="sidemenu.jsp"></jsp:include> </nav>


	<div id="center_body"> <!-- all the rest page content between header and footer-->
		
		<a class="link_most" href="Top5?sort=impressions"><button class="button_most">НАЙ-ПРЕГЛЕЖДАНИ</button></a> 
		
		<a class="link_most" href="Top5?sort=commented"><button class="button_most">НАЙ-КОМЕНТИРАНИ</button></a> 
				
		<a class="link_most" href="Top5?sort=leading"><button class="button_most">ВОДЕЩИ</button></a>
		
		<c:forEach items="${ requestScope.articles }" var="article">
			<h2><a href="pickArticle?articleId=${article.id}" >${article.title }</a>
			
			</h2>
				<!--  title:<c:out value="${article.title }"></c:out> <br>-->
				<!--<c:out value="${article.textContent }"></c:out> <br>-->
				Преглеждания: <c:out value="${article.impressions }"></c:out> <br>
				Създаден на: <c:out value="${article.created }"></c:out> <br>
				Коментари: <c:out value= "${article.commentsCount}"></c:out> <br>
				<c:forEach items="${article.mediaFiles}" var="media">
				<a href="pickArticle?articleId=${article.id}" ><img id="media" src="ShowMedia?mediaId=${media.media_id}" alt=""
						style="width: 150px; height: auto; border: 0;"></a>
				</c:forEach>

			<hr>
		</c:forEach>
		
		</div>
		
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>