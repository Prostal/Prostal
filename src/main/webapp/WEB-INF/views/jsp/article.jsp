<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${article.title }</title>

<script type="text/javascript">

function likeComment(commentId) {
	var request = new XMLHttpRequest();
	var id = "commentId="+commentId;
	request.open("post", "like", true);
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
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
	
	request.send(id);

}

function disLikeComment(commentId) {
	var request = new XMLHttpRequest();
	var id = "commentId="+commentId;
	request.open("post", "dislike", true);
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			var button = document.getElementById("dislikebutton");
			button.innerHTML = "disliked";
		}
		else if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to dislike this comment!");
		}
		else if (this.readyState == 4 && this.status == 402) {
			alert("Sorry, you have voted already!");
		}
			
	}
	
	request.send(id);
}

function postComment(user) {
	var request = new XMLHttpRequest();
	var user = "user="+user;
	request.open("post", "comment", true);
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			
			var commenttxt = document.getElementById("comment").value;
			
			var table = document.getElementById("commentstable");

			// Create an empty <tr> element and add it to the 1st position of the table:
			var row1 = table.insertRow(0);//<tr></tr>
			var row2 = table.insertRow(1);
			// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
			var cell1 = row1.insertCell(0);//<td></td>
			var cell2 = row2.insertCell(0);

			// Add some text to the new cells:
			//cell1.innerHTML = userId;
			cell2.innerHTML = commenttxt;
			
			//append first child to table of comments with the value
		}
		else
		if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to post a comment");
		}
			
	}
	
	request.send(user);
}

</script>
</head>
<body>

	<c:set var="article" scope = "page" value="${sessionScope.article}" ></c:set>
				
	<h1><c:out value="${article.title }"></c:out></h1> <br>
	<c:out value="${article.textContent }"></c:out> <br>
	immpresions: <c:out value="${article.impressions }"></c:out> <br>
	created : <c:out value="${article.created }"></c:out> <br>
	
	<c:forEach items="${article.mediaFiles}" var="media">
		<img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="100" height= auto>
	</c:forEach>
	<hr>
	<h2>Comments</h2>
	<c:if test="${user!=null }">
		<textarea id="comment" rows="4" ></textarea><br>
		<button onclick="postComment(${sessionScope.user})">Submit Comment</button>
	</c:if>
	<c:forEach items="${article.comments}" var="comment">
		
		<table border="1" id="commentstable">
			<tr>
				<td><c:out value="${comment.userId }"></c:out></td>
			</tr>
			<tr>
				<td><c:out value="${comment.content }"></c:out></td>
			</tr>
		</table>
		<c:if test="${user!=null }">
			<button style="background-color: green" id="likebutton" onclick= "likeComment(commentId=${comment.commentId })">Like ${comment.likes }</button>
			<button style="background-color: red" id="dislikebutton" onclick= "dislikeComment(commentId=${comment.commentId })">Dislike ${comment.dislikes }</button>	
		</c:if>
		<hr>
	</c:forEach>
	<c:if test="${user.admin }">
		<hr>
		<h2>Admin panel</h2>
		<hr>
			delete article :
			<a href="deleteArticle?articleId=${article.id }"><button>delete ${article.title } </button></a>
	</c:if>
	
</body>
</html>