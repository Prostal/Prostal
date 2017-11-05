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
	$(document).ready(function() {
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

</head>
<body>

	<!-- page header with logo, search, login, user etc.  -->
	<c:if test="${ requestScope.error != null }">
			<h3 style="color: red"> ${requestScope.error }</h3>
	</c:if>
	<jsp:include page="header.jsp"></jsp:include>


	<nav> <!-- side menu at the left page side --> 
		<jsp:include page="sidemenu.jsp"></jsp:include> 
	</nav>


	<div id="center_body"> <!-- all the rest page content between header and footer-->
	
		<div class="menu-section">

		<a href="Top5?sort=impressions"><button class="button_most">НАЙ-ЧЕТЕНИ</button></a> 
		<a href="Top5?sort=commented"><button class="button_most">НАЙ-КОМЕНТИРАНИ</button></a>
		<a href="Top5?sort=leading"><button class="button_most">ВОДЕЩИ</button></a>
		
		</div>
		
		<div id="carousel_pos">		
		<jsp:include page="jcarousel.jsp"></jsp:include>
		</div>
	
		</div>

	<div id="right_body">
	
		<img src="/Sportal/img/SmokingKills.jpg" width="100%" alt="Пушенето убива!">
	
	
	</div>
	
	
	<!-- footer with copy rights and cotacts -->
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>