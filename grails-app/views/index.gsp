<!doctype html>
<html>
<head>
<script type="text/javascript">
	function resize() {
		/***********************************************
		 * IFrame SSI script II- © Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
		 * Visit DynamicDrive.com for hundreds of original DHTML scripts
		 * This notice must stay intact for legal use
		 ***********************************************/

		//Input the IDs of the IFRAMES you wish to dynamically resize to match its content height:
		//Separate each ID with a comma. Examples: ["myframe1", "myframe2"] or ["myframe"] or [] for none:
		var iframeids = [ "latest-iframe" ]

		//Should script hide iframe from browsers that don't support this script (non IE5+/NS6+ browsers. Recommended):
		var iframehide = "yes"

		var getFFVersion = navigator.userAgent.substring(
				navigator.userAgent.indexOf("Firefox")).split("/")[1]
		var FFextraHeight = parseFloat(getFFVersion) >= 0.1 ? 31 : 0 //extra height in px to add to iframe in FireFox 1.0+ browsers

		function resizeCaller() {
			var dyniframe = new Array()
			for (i = 0; i < iframeids.length; i++) {
				if (document.getElementById)
					resizeIframe(iframeids[i])
					//reveal iframe for lower end browsers? (see var above):
				if ((document.all || document.getElementById)
						&& iframehide == "no") {
					var tempobj = document.all ? document.all[iframeids[i]]
							: document.getElementById(iframeids[i])
					tempobj.style.display = "block"
				}
			}
		}

		function resizeIframe(frameid) {
			var currentfr = document.getElementById(frameid)
			if (currentfr && !window.opera) {
				currentfr.style.display = "block"
				if (currentfr.contentDocument
						&& currentfr.contentDocument.body.offsetHeight) //ns6 syntax
					currentfr.height = currentfr.contentDocument.body.offsetHeight
							+ FFextraHeight;
				else if (currentfr.Document
						&& currentfr.Document.body.scrollHeight) //ie5+ syntax
					currentfr.height = currentfr.Document.body.scrollHeight;
				if (currentfr.addEventListener)
					currentfr.addEventListener("load", readjustIframe, false)
				else if (currentfr.attachEvent) {
					currentfr.detachEvent("onload", readjustIframe) // Bug fix line
					currentfr.attachEvent("onload", readjustIframe)
				}
			}
		}

		function readjustIframe(loadevt) {
			var crossevt = (window.event) ? event : loadevt
			var iframeroot = (crossevt.currentTarget) ? crossevt.currentTarget
					: crossevt.srcElement
			if (iframeroot)
				resizeIframe(iframeroot.id);
		}

		function loadintoIframe(iframeid, url) {
			if (document.getElementById)
				document.getElementById(iframeid).src = url
		}

		if (window.addEventListener)
			window.addEventListener("load", resizeCaller, false)
		else if (window.attachEvent)
			window.attachEvent("onload", resizeCaller)
		else
			window.onload = resizeCaller
	}

	resize();
	document.getElementById("stopButton").disabled = true;
	document.getElementById("stopButton").disabled = true;

	function refresh() {
		document.getElementById('latest-iframe').src = document
				.getElementById('latest-iframe').src;
		resize();
	}

	function startTimer() {
		timerVar = setInterval(function() {
			refresh()
		}, 5000);
		document.getElementById("startButton").disabled = true;
		document.getElementById("stopButton").disabled = false;
	}

	function stopTimer() {
		clearInterval(timerVar);
		document.getElementById("startButton").disabled = false;
		document.getElementById("stopButton").disabled = true;
	}
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
	width: 100%;
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
	<button id="startButton" onclick="startTimer()">Start iframe
		refresh timer</button>
	<button id="stopButton" onclick="stopTimer()">Stop iframe
		refresh timer</button>

	<div id="latest-scores" role="main">
		<hr>
		<iframe id="latest-iframe" src="score/latest" scrolling="no"
			frameborder="0"></iframe>
	</div>
</body>
</html>
