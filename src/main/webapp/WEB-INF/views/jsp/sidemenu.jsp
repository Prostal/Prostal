<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<div id="side_menu">

<c:forEach items="${applicationScope.categories}" var="category" varStatus="status">	
	<a href="categoryArticles?category=${category.categoryId}">
		<button class="side_button">${category.name}</button>
	</a>
 </c:forEach>
</div>
