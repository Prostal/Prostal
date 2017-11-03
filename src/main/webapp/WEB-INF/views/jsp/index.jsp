<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sportal.bg- Home</title>
<link type="text/css" href="css/main.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/jquery.jcarousel.pack.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.jcarousel.css" />
<link rel="stylesheet" type="text/css" href="css/skin.css" />
<link rel="stylesheet" type="text/css" href="css/main.css" />

<script type="text/javascript">
	$(document).ready(
			function() {

				//jCarousel Plugin
				$('#carousel').jcarousel({
					vertical : true,
					scroll : 1,
					auto : 2,
					wrap : 'last',
					initCallback : mycarousel_initCallback
				});

				//Front page Carousel - Initial Setup
				// towa kakwo e slideshow-carousel
				$('div#slideshow-carousel a img').css({
					'opacity' : '0.5'
				});
				$('div#slideshow-carousel a img:first').css({
					'opacity' : '1.0'
				});
				$('div#slideshow-carousel li a:first').append(
						'<span class="arrow"></span>')

				//Combine jCarousel with Image Display
				$('div#slideshow-carousel li a').hover(
						function() {

							if (!$(this).has('span').length) {
								$('div#slideshow-carousel li a img').stop(true,
										true).css({
									'opacity' : '0.5'
								});
								$(this).stop(true, true).children('img').css({
									'opacity' : '1.0'
								});
							}
						},
						function() {

							$('div#slideshow-carousel li a img').stop(true,
									true).css({
								'opacity' : '0.5'
							});
							$('div#slideshow-carousel li a').each(function() {

								if ($(this).has('span').length)
									$(this).children('img').css({
										'opacity' : '1.0'
									});

							});

						}).click(
						function() {

							$('span.arrow').remove();
							$(this).append('<span class="arrow"></span>');
							$('div#slideshow-main li').removeClass('active');
							$('div#slideshow-main li.' + $(this).attr('rel'))
									.addClass('active');

							return false;
						});

			});

	//Carousel Tweaking

	function mycarousel_initCallback(carousel) {

		// Pause autoscrolling if the user moves with the cursor over the clip.
		carousel.clip.hover(function() {
			carousel.stopAuto();
		}, function() {
			carousel.startAuto();
		});
	}
</script>
<style>



#slideshow-main {
	width: 429px;
	float: left;
	margin-right: 3px;
}

#slideshow-main ul {
	margin: 0;
	padding: 0;
	width: 429px;
}

#slideshow-main li {
	width: 429px;
	height: 290px;
	display: none;
	position: relative;
}

#slideshow-main li.active {
	display: block !important;
}

#slideshow-main li span.opacity {
	position: absolute;
	bottom: 0;
	left: 0;
	display: block;
	width: 100%;
	height: 60px;
	background: #000;
	filter: alpha(opacity = 50);
	-moz-opacity: 0.5;
	-khtml-opacity: 0.5;
	opacity: 0.5;
	z-index: 500;
}

#slideshow-main li span.content {
	position: absolute;
	bottom: 0;
	left: 0;
	display: block;
	width: 100%;
	height: 60px;
	z-index: 1000;
}

#slideshow-main li span.content h1 {
	font-size: 14px;
	margin: 5px 0;
	padding: 0 10px;;
	color: #42e2e8;
}

#slideshow-main li span.content p {
	font-size: 11px;
	margin: 5px 0;
	padding: 0 10px;;
	color: #42e2e8;
}

#slideshow-carousel {
	float: left;
	width: 206px;
	position: relative
}

#slideshow-carousel ul {
	margin: 0;
	padding: 0;
	list-style: none;
}

#slideshow-carousel li {
	background: #fff;
	height: 97px;
	position: relative
}

#slideshow-carousel li .arrow {
	left: 3px;
	top: 28px;
	position: absolute;
	width: 20px;
	height: 40px;
	background: url(img/arrow_white.png) no-repeat 0 0;
	display: block;
}

#slideshow-carousel li a {
	background: #000;
	display: block;
	width: 206px;
	height: 95px;
}

#slideshow-carousel .active {
	filter: alpha(opacity = 100);
	-moz-opacity: 1.0;
	-khtml-opacity: 1.0;
	opacity: 1.0;
}

#slideshow-carousel .faded {
	filter: alpha(opacity = 50);
	-moz-opacity: 0.5;
	-khtml-opacity: 0.5;
	opacity: 0.5;
}
</style>
</head>
<body>

	<!-- page header with logo, search, login, user etc.  -->
	<jsp:include page="header.jsp"></jsp:include>


	<nav> <!-- side menu at the left page side --> 
		<jsp:include page="sidemenu.jsp"></jsp:include> 
	</nav>


	<div id="right_body"> <!-- all the rest page content between header and footer-->
	
		<div class="menu-section">

		<a href="Top5?sort=impressions"><button class="button_most">most viewed</button></a> 
		<a href="Top5?sort=commented"><button class="button_most">most commented</button></a>
		<a href="Top5?sort=leading"><button class="button_most">leading</button></a>
		
		</div>
		
		<jsp:include page="jcarousel.jsp"></jsp:include>
	
	</div>

	
	
	
	
	
	
	


	<!-- footer with copy rights and cotacts -->
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>