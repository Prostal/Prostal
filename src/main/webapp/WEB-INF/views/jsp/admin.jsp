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
	<h1 style="color:#fe850c">ADMIN PANEL</h1>
	<br>
	<h3>Добавяне на категория. Моля, въведете наименование:</h3>
	<form action="addCategory" method="post" >
		<input type="text" name ="category" placeholder = "category name">
		<input type="submit" value="post category" class="content_button"><br>
	</form>
	<br>
	
	<h3>Добавяне статия:</h3>
	<form action="postArticle" method="post" enctype="multipart/form-data">
	
		<input type="text" name ="title" placeholder = "title" required="required">
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
		<input type="file" name="image" placeholder = "images" class="content_button"><br>
		
		<input type="submit" value="post article" class="content_button">
	</form>
	<br>
	<br>
	<form action="deleteArticle" method="post" >
		<h3 style="color:red">Изтриване на статия. Моля, въведете име:</h3>
		<input type="text" name ="title" placeholder = "title">
		
		<input type="submit" value="delete article" class="delete_button">
	</form>
</body>
</html>