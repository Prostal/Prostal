<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<!DOCTYPE>
<html>
<head>
<link type="text/css" href="css/main.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${article.title }</title>

<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
<script type="text/javascript">  
function postComment() {
	var request = new XMLHttpRequest();
	var data = CKEDITOR.instances.editor1.getData();
	var comment = "comment="+data;
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			//reset comments input field???
					
			var table = document.getElementsByClassName("commentstable")[0].cloneNode();
			/* table. */
			
			var table = document.getElementById("commentstable");
			
			// Create an empty <tr> element and add it to the 1st position of the table:
			var row = table.insertRow(0);//<tr></tr>

			// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
			var cell1 = row.insertCell(0);//<td></td>

			// Add some text to the new cells:
			cell1.innerHTML = data;
			//append first child to table of comments with the value
		}
		else
		if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to post a comment");
		}
			
	}
	
	request.open("post", "comment", true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(comment);
	debugger;
	
}


function likeComment(commentId) {
	var request = new XMLHttpRequest();
	var path = "commentId="+commentId;
	
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			var responseData = JSON.parse(this.response);
			var likesCount = responseData.likesCount;
			var username = responseData.username;
			
			var button = document.getElementById("likebutton");
			button.innerHTML = "liked";
		}
		else if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to like this video!");
		}
		else if (this.readyState == 4 && this.status == 402) {
				alert("Sorry, you have voted already!");
		}
			
	}
	request.open("post", "like", true);
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	var response = request.send(path);

}

function dislikeComment(commentId) {
	var request = new XMLHttpRequest();
	var id = "commentId="+commentId;
	
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			
			var button = document.getElementById("dislikebutton");
			button.innerHTML = "disliked";
		}
		else if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to like this video!");
		}
		else if (this.readyState == 4 && this.status == 402) {
				alert("Sorry, you have voted already!");
		}
			
	}
	request.open("post", "dislike", true);
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	request.send(id);

}
</script>  
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
		<nav> <!-- side menu at the left page side --> 
	<jsp:include
		page="sidemenu.jsp"></jsp:include> </nav>
	
		<div id="right_body"> <!-- all the rest page content between header and footer-->
	<c:set var="article" scope = "page" value="${sessionScope.article}" ></c:set>
	
	<div id="article">
	<h1><c:out value="${article.title }"></c:out></h1> <br>
	<c:out value="${article.textContent }"></c:out> <br>
	</div>			
	<c:out value="${article.impressions }"></c:out> <br>
	<c:out value="${article.created }"></c:out> <br>
	
	<c:forEach items="${article.mediaFiles}" var="media">
		<c:if test="${media.isVideo }">
			<video width="320" height="240"  controls autoplay >
			  <source src="ShowMedia?mediaId=${media.media_id}" >
			</video>
		</c:if>
		<c:if test="${!media.isVideo }">
			<img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="320" height= auto><br>
		</c:if>
	</c:forEach>
	<hr>
<h2>Comments</h2>
	 <c:if test="${user!=null }">
	 
			
		<label for="editor1">Editor 1:</label>
		<textarea cols="60" id="editor1" name="editor1" rows="4"></textarea>
		<script>
              	CKEDITOR.replace( 'editor1' );
       	</script>
       	<button onclick="postComment()">Submit Comment</button>
			
	</c:if>
	
	<div id="comment-section">
	<c:forEach items="${article.comments}" var="comment">
		<c:set var="comentator" scope = "page" value="${comment.userId}" ></c:set>
		<table border="1" id="commentstable-${comment.commentId}" class="commentstable class2 class3">
			<tr>
				<td class="avatar-row">
					<img class="avatar-image" src="showAvatar/${comment.userId}"  width="50" height= auto>
				</td>
			</tr>
			<tr class="comment-content">
				<td>${comment.content }</td>
			</tr>
			<tr>
				<td>${comment.timeCreated }</td>
			</tr>
		</table>
		<c:if test="${user!=null }">
			<button style="background-color: green" id="likebutton-${comment.commentId}" onclick="likeComment(commentId=${comment.commentId })">Like ${comment.likes }</button>
			<button style="background-color: red" id="dislikebutton-${comment.commentId}" onclick="dislikeComment(commentId=${comment.commentId })">Dislike ${comment.dislikes }</button>	
		</c:if>
		
		<hr>
	</c:forEach>
	</div>
	<c:if test="${user.admin }">
		<hr>
		<h2>Admin panel</h2>
		<hr>
			delete article :
			<a href="deleteArticle?articleId=${article.id }"><button>delete ${article.title } </button></a>
			<br>
			add video :
			<form action="addVideo" method="post" enctype="multipart/form-data">
				<input type="text" name="articleId"  value="${article.id }"><br>
				<input type="file" name="video"><br>
				<input type="submit" value="add video in ${article.title }"><br>
			</form> 
			<%-- <a href="addArticleMedia?articleId=${article.id }"><button>delete ${article.title } </button></a> --%>
	</c:if>

	</div>
<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>