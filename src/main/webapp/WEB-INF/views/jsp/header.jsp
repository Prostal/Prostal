<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header_body">
	<a href="index"><img src="/Sportal/img/logo.png" alt=""></a>
	<div id="top_user_options">
		<!--  <form action="index">
			<input type="submit" value="Home">
		</form> -->
		<!--  <c:if test="${ sessionScope.user!=null }">
			<form action="userPage">
				<input type="submit" value="User">
			</form>
		</c:if>
		This is the ex-"USER" button. -->
		<div style="font-size:20px">
		Welcome, <a
			href=<c:if test="${ sessionScope.user!=null }">"userPage"
		</c:if>>
			<c:out value="${sessionScope.user.username }">Guest</c:out>
		</a>
		</div>

		<c:if test="${ sessionScope.user==null }">
			<form action="loginPage">
				<input type="submit" value="log in" id="login_button">
			</form>
		</c:if>
		
		<c:if test="${ sessionScope.user!=null }">
			<form action="Logout" method="post">
				<input type="submit" value="log out" id="logout_button">
			</form>
		</c:if>
		<!--  <a href="userPage?=${sessionScope.user.username }>${sessionScope.user.username }</a> -->

		<!--  <a href = "userPage"> ------------------- </a> -->


		<!--  Welcome, <c:out value="${sessionScope.user.username }">Guest</c:out> -->
			
			<!-- 
			<button class="img_button" type="submit" value="log out"
				style="border: 0; background: transparent">
				<img src="/Sportal/img/logout.png" width="20" height="20"
					alt="Log out" />
			</button> ATTEMPT TO MAKE A LOGOUT BUTTON
			 -->
	</div>

	<jsp:include page="search.jsp"></jsp:include>
	<br>
</div> <!-- header body div end... DO NOT TOUCH! -->