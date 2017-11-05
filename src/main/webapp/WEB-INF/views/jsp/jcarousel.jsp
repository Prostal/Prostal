<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="welcomeHero">
	<div id="slideshow-main">
		<ul>
			<c:forEach items="${sessionScope.leading}" var="leading" varStatus="status">				
				<li class="p${status.index} ${status.first ? 'active' : ''}">
					<a href="pickArticle?articleId=${leading.id}"> 
							
						<c:forEach items="${leading.mediaFiles}" var="media">
							<c:if test="${!media.isVideo }">
								<img class="media" src="ShowMedia?mediaId=${media.media_id}"  width="580" height="326">
							</c:if>
						</c:forEach>
						<span class="opacity"></span>
						<span class="content">
							<h1>${leading.title}</h1>
							<p>${leading.created }</p>
						</span>
					</a>
				</li>
			 </c:forEach>
		</ul>
	</div>

	<div id="slideshow-carousel">
		<ul id="carousel" class="jcarousel jcarousel-skin-tango">
			<c:forEach items="${sessionScope.leading}" var="leading" varStatus="status">
				<li>
					<a href="pickArticle?articleId=${leading.id}" rel="p${status.index}">
						<c:forEach items="${leading.mediaFiles}" var="media">
							<c:if test="${!media.isVideo }">
								<img class="media" src="ShowMedia?mediaId=${media.media_id}"  width="234" height="108" alt="${leading.title}">
							</c:if>
						</c:forEach>
					</a>
				</li>
			 </c:forEach>
		</ul>
	</div>

	<div class="clear"></div>

</div>