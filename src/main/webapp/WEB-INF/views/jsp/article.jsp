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
			var responseData = JSON.parse(this.response);
			var username = responseData.username;
			var time = responseData.commentTime;
			var userId = responseData.userId;
			
			var table = document.getElementById("commentstable");
			
			// Create an empty <tr> element and add it to the 1st position of the table:
			var row1 = table.insertRow(0);//<tr></tr>
			var row2 = table.insertRow(1);
			var row3 = table.insertRow(2);
			// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
			var cell1 = row1.insertCell(0);//<td></td>
			var cell2 = row2.insertCell(0);
			var cell3 = row3.insertCell(0);
			// Add some text to the new cells:
				//<img  src= showAvatar/userId  width="50" height= auto>;
			cell1.innerHTML = username;
			cell2.innerHTML = data;
			cell3.innerHTML = time;
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
	
	
}


function likeComment(commentId) {
	var request = new XMLHttpRequest();
	var path = "commentId="+commentId;
	
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			var responseData = JSON.parse(this.response);
			var likesCount = responseData.likesCount;
			var commentId = responseData.commentId;
			var button = document.getElementById("likebutton-".concat(commentId));
			button.innerHTML = "liked ".concat(likesCount);
		}
		else if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to like this comment!");
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
	var path = "commentId="+commentId;
	
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			var responseData = JSON.parse(this.response);
			var dislikesCount = responseData.dislikesCount;
			var commentId = responseData.commentId;
			
			var button = document.getElementById("dislikebutton-".concat(commentId));
			button.innerHTML = "disliked ".concat(dislikesCount);
		}
		else if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to dislike this comment!");
		}
		else if (this.readyState == 4 && this.status == 402) {
				alert("Sorry, you have voted already!");
		}
			
	}
	request.open("post", "dislike", true);
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	var response = request.send(path);

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
	<span>${article.textContent }</span> <br>
	</div>			
	<span>Брой преглеждания: </span><c:out value="${article.impressions }"></c:out> <br>
	<span>Създаден на: </span><c:out value="${article.created }"></c:out> <br>
	
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
	
		<c:set var="comentator" scope = "page" value="${comment.userId}" ></c:set>
		<table border="1" id="commentstable" >
			<c:forEach items="${article.comments}" var="comment">
				<tr>
					<td class="avatar-row">
						<img class="avatar-image" src="showAvatar/${comment.userId}"  width="50" height= auto>
					</td>
				</tr>
				<tr class="comment-row">
					<td>${comment.content }</td>
				</tr>
				<tr class="timeCreated-row">
					<td>${comment.timeCreated }</td>
				</tr>
				<tr>
					<td>
						<button style="background-color: green" id="likebutton-${comment.commentId}" onclick="likeComment(commentId=${comment.commentId })">Like ${comment.likes }</button>
						<button style="background-color: red" id="dislikebutton-${comment.commentId}" onclick="dislikeComment(commentId=${comment.commentId })">Dislike ${comment.dislikes }</button>	
					</td>
				</tr>
				
			</c:forEach>
			
		</table>
		<c:if test="${user!=null }">
			
		</c:if>
		
		<hr>
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