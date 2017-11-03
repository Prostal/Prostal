<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<!DOCTYPE html>
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>admin menu</title>
<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
</head>
<body>

	<form action="addCategory" method="post" >
		<input type="text" name ="category" placeholder = "category name">
		<input type="submit" value="post category"><br>
	</form>
	<br>
	
	<form action="postArticle" method="post" enctype="multipart/form-data">
	
		<input type="text" name ="title" placeholder = "title" required="true">
		<!-- <input type="text" name ="textContent" placeholder = "content"> -->
		<label for="editor2">Editor 1:</label>
		<textarea cols="60" id="editor2" name="textContent" rows="4"></textarea>
		<script>
              	CKEDITOR.replace( 'editor2' );
       	</script>
		<select name="category">
			<c:forEach items="${applicationScope.categories}" var="category">
				<option value=${ category.categoryId}> ${ category.name}</option>
	    	</c:forEach>
		</select>
		 
		<input type="checkbox" name="isLeading" value="true"> Leading <br>
		<input type="file" name="image" placeholder = "images"><br>
		
		<input type="submit" value="post article">
	</form>
	
	<form action="deleteArticle" method="post" >
	
		<input type="text" name ="title" placeholder = "title">
		
		<input type="submit" value="delete article">
	</form>
</body>
</html>