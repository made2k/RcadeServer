<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Rcade</title>
</head>
<body>

	<div id="header">
		<div id="loginHeader">
			<g:loginControl />
		</div>
	</div>

	<div id="RcadeLogo" role="banner">
		<a href="/RcadeServer"><img
			src="${resource(dir: 'images', file: 'Rcade-Server.svg')}" width=30%
			alt="Rcade" /></a>
	</div>



	<div class="body"></div>
</body>
</html>