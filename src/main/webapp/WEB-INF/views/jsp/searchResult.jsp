<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

   <c:forEach items="${sessionScope.search}" var="search">
				<c:out value="${search.title }"></c:out> <br>
				<c:out value="${search.textContent }"></c:out> <br>
				<c:out value="${search.impressions }"></c:out> <br>
				<c:out value="${search.created }"></c:out> <br>
				
				<c:forEach items="${search.mediaFiles}" var="media">
					<img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="100" height= auto>
				</c:forEach>
				<hr>
	 </c:forEach>

<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>