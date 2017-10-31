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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${article.title }</title>

<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
<script type="text/javascript">



function postComment() {
	var request = new XMLHttpRequest();
	//var user = "user="+user;
	request.open("post", "comment", true);
	request.onreadystatechange = function() {
		//when response is received
		if (this.readyState == 4 && this.status == 200) {
			
			var comment = JSON.parse(this.responseText);
			
			var commenttxt = document.getElementById("editor1").value;
			
			var table = document.getElementById("commentstable");

			// Create an empty <tr> element and add it to the 1st position of the table:
			var row1 = table.insertRow(0);//<tr></tr>
			//var row2 = table.insertRow(1);
			// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
			var cell1 = row1.insertCell(0);//<td></td>
			//var cell2 = row2.insertCell(0);

			// Add some text to the new cells:
			//cell1.innerHTML = userId;
			cell1.innerHTML = commenttxt;
			
			//append first child to table of comments with the value
		}
		else
		if (this.readyState == 4 && this.status == 401) {
			alert("Sorry, you must log in to post a comment");
		}
			
	}
	
	request.send();
}

</script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<c:set var="article" scope = "page" value="${sessionScope.article}" ></c:set>
	
	<div id="article">
	<h1><c:out value="${article.title }"></c:out></h1> <br>
	<c:out value="${article.textContent }"></c:out> <br>
	</div>			
	<c:out value="${article.impressions }"></c:out> <br>
	<c:out value="${article.created }"></c:out> <br>
	
	<c:forEach items="${article.mediaFiles}" var="media">
		<c:if test="${media.isVideo }">
			<video width="320" height="240" controls autoplay>
			  <source src="ShowMedia?mediaId=${media.media_id}" >
			</video>
		</c:if>
		
		<img id="media" src="ShowMedia?mediaId=${media.media_id}"  width="320" height= auto><br>
	</c:forEach>
	<hr>
<h2>Comments</h2>
	 <c:if test="${user!=null }">
		
		<form action="comment" method="post">
			<p>
				<label for="editor1">Editor 1:</label>
				<textarea cols="80" id="editor1" name="editor1" rows="10"></textarea>
				<script>
                	CKEDITOR.replace( 'editor1' );
            	</script>
			</p>
			<p>
				<input type="submit" value="Submit" />
			</p>
		</form>
	</c:if>
	<c:forEach items="${article.comments}" var="comment">
		
		<table border="1" id="commentstable">
			<%-- <tr>
				<td><c:out value="${comment.userId }"></c:out></td>
			</tr> --%>
			<tr>
				<td><c:out value="${comment.content }"></c:out></td>
			</tr>
		</table>
		
		<hr>
	</c:forEach>
	<c:if test="${user.admin }">
		<hr>
		<h2>Admin panel</h2>
		<hr>
			delete article :
			<a href="deleteArticle?articleId=${article.id }"><button>delete ${article.title } </button></a>
			<br>
			add media :
			<form action="addVideo" method="post" enctype="multipart/form-data">
				<input type="text" name="articleId"  value="${article.id }"><br>
				<input type="file" name="video"><br>
				<input type="submit" value="add video in ${article.title }"><br>
			</form> 
			<%-- <a href="addArticleMedia?articleId=${article.id }"><button>delete ${article.title } </button></a> --%>
	</c:if>

<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>