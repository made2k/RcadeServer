
<%@ page import="rcadeserver.Game" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'game.label', default: 'Game')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-game" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-game" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list game">
			
<!-- 			
				<g:if test="${gameInstance?.romName}">
				<li class="fieldcontain">
					<span id="romName-label" class="property-label"><g:message code="game.romName.label" default="Rom Name" /></span>
					
						<span class="property-value" aria-labelledby="romName-label"><g:fieldValue bean="${gameInstance}" field="romName"/></span>
					
				</li>
				</g:if>
 -->
 			
				<g:if test="${gameInstance?.gameName}">
				<li class="fieldcontain">
					<span id="gameName-label" class="property-label"><g:message code="game.gameName.label" default="Game Name" /></span>
					
						<span class="property-value" aria-labelledby="gameName-label"><g:fieldValue bean="${gameInstance}" field="gameName"/></span>
					
				</li>
				</g:if>
				
				<li class="fieldcontain">
					<span id="rss-label" class="property-label">RSS Feed</span>
					<span class="property-value" aria-labelledby="rss-label">
						<link rel="alternate" type="application/rss+xml" title="RCade Scores" href="http://localhost:8080/RcadeServer/player/RSS?id=1"/>
						<a href="http://localhost:8080/RcadeServer/player/RSS?id=1">Active</a>
					</span>
				</li>
			
				<g:if test="${!gameInstance?.scores}">
					<li class="fieldcontain">
					<span id="scores-label" class="property-label"><g:message code="game.scores.label" default="High Scores" /></span>
					
						<span class="property-value" aria-labelledby="scores-label">No high scores. You should play!</span>
					
				</li>
				</g:if>
			
				<g:if test="${gameInstance?.scores}">
				<li class="fieldcontain">
					<span id="scores-label" class="property-label"><g:message code="game.scores.label" default="High Scores" /></span>
					
						<g:each in="${gameInstance.scores.sort{a,b-> (a.score>b.score ? -1 : 1)}}" var="s" status="i">
							<g:if test="${ i<15 }">
								<span class="property-value" aria-labelledby="scores-label"><g:link controller="score" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
							</g:if>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${gameInstance?.id}" />
					<g:link class="edit" action="edit" id="${gameInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
