<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<nav> <!-- side menu at the left page side --> <jsp:include
		page="sidemenu.jsp"></jsp:include> </nav>
	<div id="right_body">
		<!-- all the rest page content between header and footer-->
		<hr>
		<c:forEach items="${sessionScope.articles}" var="article">
			<h1>
				<a href="pickArticle?articleId=${article.id}"> ${article.title }
				</a>
			</h1>
			<!--<c:out value="${article.title }"></c:out>  <br> -->
			<!--<c:out value="${article.textContent }"></c:out> <br> -->
			<span>Брой преглеждания: </span>
			<c:out value="${article.impressions }"></c:out>
			<br>
			<span>Създаден на: </span>
			<c:out value="${article.created }"></c:out>
			<br>

			<c:forEach items="${article.mediaFiles}" var="media">
				<c:if test="${!media.isVideo }">
					<a href="pickArticle?articleId=${article.id}"> <img
						src="ShowMedia?mediaId=${media.media_id}" alt=""
						style="width: 150px; height: auto; border: 0;"></a>
					<br>
				</c:if>
			</c:forEach>




			<hr>
		</c:forEach>
		<hr>
	</div>

	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>