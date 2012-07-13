<!doctype html>
<html>
<head>
<script type="text/javascript">
	function refresh() {
		xmlhttp = new XMLHttpRequest();
		xmlhttp.open("GET", "score/testLatest", false);
		xmlhttp.send();
		document.getElementById('test').innerHTML = xmlhttp.responseText;
	}

	function startTimer() {
		timerVar = setInterval(	function(){
			refresh()
		}, 1000);
	}
	
	startTimer()
</script>
<meta name="layout" content="main" />
<title>Rcade High Scores</title>
<style type="text/css" media="screen">
#controller-list {
	background-color: #D6D6D6;
	border: .2em solid #E6E6E6;
	margin: 2em 2em 1em;
	padding: 1em;
	width: 7em;
	float: left;
	-moz-box-shadow: 0px 0px 1.25em #ccc;
	-webkit-box-shadow: 0px 0px 1.25em #ccc;
	box-shadow: 0px 0px 1.25em #ccc;
	-moz-border-radius: 0.6em;
	-webkit-border-radius: 0.6em;
	border-radius: 0.6em;
	text-align: center
}

.ie6 #controller-list {
	display: inline;
	/* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
}

#controller-list ul {
	font-size: 0.8em;
	list-style-type: none;
	list-style-position: inside;
	margin-bottom: 0.6em;
	padding: 0;
}

#controller-list li {
	line-height: 2.3;
	list-style-position: inside;
	font-size: 120%;
	font-weight: normal;
	margin: 0.25em 0;
}

#controller-list h1 {
	text-transform: uppercase;
	font-size: 1.2em;
	margin: 0 0 0.3em;
}

#page-body {
	text-align: justify;
	margin: 2em 3em 1.25em 14em;
}

#latest-scores {
	clear: both;
}

#latest-scores hr {
	margin: 1em 1em 0em 1em;
	color: #e6e6e6;
}

#latest-scores iframe {
	width: 96%;
	   -moz-box-shadow: 0 0 0.3em #5B5B5B;
	-webkit-box-shadow: 0 0 0.3em #5B5B5B;
	        box-shadow: 0 0 0.3em #5B5B5B;
	margin-top: 1em;
	margin-left: 2%;
}

h2 {
	margin-top: 1em;
	margin-bottom: 0.3em;
	font-size: 1em;
}

p {
	line-height: 1.5;
	margin: 0.25em 0;
}

@media screen and (max-width: 480px) {
	#status {
		display: none;
	}
	#page-body {
		margin: 0 1em 1em;
	}
	#page-body h1 {
		margin-top: 0;
	}
}
</style>
</head>
<body>
	<a href="#page-body" class="skip"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>

	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>

	<div id="controller-list" role="navigation">
		<h1>View:</h1>
		<ul>
			<g:each var="c"
				in="${grailsApplication.controllerClasses.sort { it.fullName } }">
				<g:if test="${!session?.user?.isAdmin() }">
					<!-- If user not admin, don't show special classes -->
					<g:if test="${c.name == 'Game' || c.name == 'Player' || c.name == 'Score'}">
						<li class="controller"><g:link controller="${c.name}"
								action="list">
								${c.name}s</g:link></li>
					</g:if>
				</g:if>
				<g:else>
					<!-- If user is admin, show all classes -->
					<li class="controller"><g:link controller="${c.name}"
							action="list">
							${c.name}s</g:link></li>
				</g:else>
			</g:each>
		</ul>
	</div>

	<div id="page-body" role="main">
		<h1>Welcome to Rcade</h1>
		<p>Rcade is a front-end interface for the MAME emulator. Rcade is
			built off of Wah!Cade and adds networking and persistent high scores
			for all of your favorite games. With Rcade you are able to play games
			like Galaga and Airwolf and compete with your friends across
			different machines. Battle it out for the high score!</p>
	</div>
	<!--
	<span>Temporary:</span>
	<button id="startButton" onclick="startTimer()">Start iframe
		refresh timer</button>
	<button id="stopButton" onclick="stopTimer()" disabled="true">Stop iframe
		refresh timer</button>
	-->
	<div id="latest-scores" role="main">
		
	</div>
	<div id="test"></div>
</body>
</html>
