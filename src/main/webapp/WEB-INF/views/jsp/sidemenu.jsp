<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
	<c:forEach items="${applicationScope.categories}" var="category">	
		<a href="categoryArticles?category=${ category.categoryId}"><button>${category.name}</button></a>
 		<br>
	 </c:forEach>
</body>
</html>