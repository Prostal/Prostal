<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/jquery.jcarousel.pack.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.jcarousel.css" />
<link rel="stylesheet" type="text/css" href="css/skin.css" />
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
/* Slide Show */
body {
	font-family: arial;
}

img {
	border: 0;
}

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




	<div id="welcomeHero">

		<div id="slideshow-main">

			<ul>
				<li class="p1 active"><a href="#"> <img src="img/1_big.gif"
						width="430" height="290" alt="" /> <span class="opacity"></span> <span
						class="content"><h1>Title 1</h1>
							<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p></span>
				</a></li>
				<li class="p2"><a href="#"> <img src="img/2_big.gif"
						width="430" height="290" alt="" /> <span class="opacity"></span> <span
						class="content"><h1>Title 2</h1>
							<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p></span>
				</a></li>
				<li class="p3"><a href="#"> <img src="img/3_big.gif"
						width="430" height="290" alt="" /> <span class="opacity"></span> <span
						class="content"><h1>Title 3</h1>
							<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p></span>
				</a></li>
				<li class="p4"><a href="#"> <img src="img/4_big.gif"
						width="430" height="290" alt="" /> <span class="opacity"></span> <span
						class="content"><h1>Title 4</h1>
							<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p></span>
				</a></li>
				<li class="p5"><a href="#"> <img src="img/5_big.gif"
						width="430" height="290" alt="" /> <span class="opacity"></span> <span
						class="content"><h1>Title 5</h1>
							<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p></span>
				</a></li>
			</ul>
		</div>

		<div id="slideshow-carousel">
			<ul id="carousel" class="jcarousel jcarousel-skin-tango">

				<li><a href="#" rel="p1"><img src="img/1.gif" width="206"
						height="95" alt="#" /></a></li>
				<li><a href="#" rel="p2"><img src="img/2.gif" width="206"
						height="95" alt="#" /></a></li>
				<li><a href="#" rel="p3"><img src="img/3.gif" width="206"
						height="95" alt="#" /></a></li>
				<li><a href="#" rel="p4"><img src="img/4.gif" width="206"
						height="95" alt="#" /></a></li>
				<li><a href="#" rel="p5"><img src="img/5.gif" width="206"
						height="95" alt="#" /></a></li>

			</ul>
		</div>

		<div class="clear"></div>

	</div>






</body>
</html>