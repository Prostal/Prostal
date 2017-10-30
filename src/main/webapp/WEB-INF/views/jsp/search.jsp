<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE >
<html>
<head>

</head>
<body>

<form action="Search">
  <input type="text" name="search" placeholder="Search...">
  <select name="category">
			<c:forEach items="${applicationScope.categories}" var="category">
				<option value=${ category.categoryId}> ${ category.name}</option>
	    	</c:forEach>
		</select>
  <input type="checkbox" name="category" value="category"> search in category<br>
  <c:if test="${category!=null}">
  <select name="category">
			<c:forEach items="${applicationScope.categories}" var="category">
				<option value=${ category.categoryId}> ${ category.name}</option>
	    	</c:forEach>
		</select>
  </c:if>
  <input type="submit" value="search">
</form>

</body>
</html>