<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE >
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
</head>
<body>

<form action="Search">
  <button class="img_button" type="submit" value="search" style="border: 0; background: transparent">
    <img src="/Sportal/img/magnifier.png" width="20" height="20" alt="submit" />
</button>
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
 

  
</form>

</body>
</html>