<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>admin menu</title>
</head>
<body>

	<form action="addCategory" method="post" >
		<input type="text" name ="category" placeholder = "category name" required>
		<input type="submit" value="post category"><br>
	</form>
	
	<hr>
	<form action="postArticle" method="post" enctype="multipart/form-data">
	
		<input type="text" name ="title" placeholder = "title" required> <br>
		<input type="text" name ="textContent" placeholder = "content" required> <br>
		
		<select name="category">
			<c:forEach items="${applicationScope.categories}" var="category">
				<option value=${ category.categoryId}> ${ category.name}</option>
	    	</c:forEach>
		</select>
		 
		<input type="checkbox" name="isLeading" value="true"> Leading <br>
		<input type="file" name="image" placeholder = "image" accept="image/*"><br>
		
		<input type="submit" value="post article"> <br>
	</form>
	
	
</body>
</html>