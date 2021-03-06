<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>search result</title>
</head>
<body>

   <jsp:include page="header.jsp"></jsp:include>
   
    <c:if test="${ requestScope.error != null }">
			<h1 style="color: red">${requestScope.error }</h1>
	</c:if>
	
	<nav> <!-- side menu at the left page side --> 
	<jsp:include page="sidemenu.jsp"></jsp:include> </nav>


	<div id="center_body"> <!-- all the rest page content between header and footer-->
   


   <c:forEach items="${sessionScope.search}" var="article">
				<h1><a href="pickArticle?articleId=${article.id}" > ${article.title } </a></h1>
				<span>Брой преглеждания: </span><c:out value="${article.impressions }"></c:out> <br>
				<span>Брой преглеждания: </span><c:out value="${article.created }"></c:out> <br>
				<span> Коментари: </span><c:out value= "${article.commentsCount}"></c:out> <br>
				
				<c:forEach items="${article.mediaFiles}" var="media">
					<c:if test="${!media.isVideo }">
						<a href="pickArticle?articleId=${article.id}" ><img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="320" height= auto></a><br>
					</c:if>
				</c:forEach>
				<hr>
	 </c:forEach>

</div>

<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>