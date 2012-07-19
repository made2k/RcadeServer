
<%@ page import="rcadeserver.Game"%>
<!doctype html>
<html>
<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/batch-operations.js"></script>
<script>
$(document).ready(function()
{
	batch("${request.contextPath}", "${params.controller}");
});
</script>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'game.label', default: 'Game')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-game" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<g:if test="${session?.user?.isAdmin() }">		
			<li><g:link class="create" action="create">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link></li>
				</g:if>
				
		</ul>
	</div>
	<div id="list-game" class="content scaffold-list" role="main">
		<h1>
			<g:message code="default.list.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<!-- <g:sortableColumn property="romName" title="${message(code: 'game.romName.label', default: 'Rom Name')}" />  -->
					<g:if test="${session?.user?.isAdmin()}">
						<th class="nohov"><input type="checkbox" name="batch-all"></th>
					</g:if>
					<g:sortableColumn property="gameName"
						title="${message(code: 'game.gameName.label', default: 'Game Name')}" />

					<!-- <g:sortableColumn property="scores" title="${message(code: 'game.scores.label', default: 'High Score')}" /> -->
					<th>High Score</th>
					<!-- non sortable column -->

				</tr>
			</thead>
			<tbody>
				<g:each in="${gameInstanceList}" status="i" var="gameInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<!-- <td><g:link action="show" id="${gameInstance.id}">${fieldValue(bean: gameInstance, field: "romName")}</g:link></td>  -->
						<g:if test="${session?.user?.isAdmin() }">
							<td><input type="checkbox" name="batch" value="${gameInstance.id}"></td>
						</g:if>
						<td><g:link action="show" id="${gameInstance.id}">
								${fieldValue(bean: gameInstance, field: "gameName")}
							</g:link></td>
						<g:if test="${gameInstance?.scores }">
							<td><g:link controller="score" action="show"
									id="${gameInstance.getHighScore().id}">
									${gameInstance.getHighScoreString()}
								</g:link></td>
						</g:if>
						<g:else>
							<td>
								${gameInstance.getHighScoreString()}
							</td>
						</g:else>

					</tr>
				</g:each>
			</tbody>
		</table>
		<g:if test="${session?.user?.isAdmin() }">
			<div id="batch-buttons">
				<button id="delete">Delete selected ${params.controller}s</button>
			</div>
		</g:if>
		<div class="pagination">
			<g:if test="${gameInstanceTotal <= params.max}">
    			<span class="currentStep">1</span>
    		</g:if>
    		<g:else>
				<g:paginate total="${gameInstanceTotal}"/>
			</g:else>
		</div>
	</div>
</body>
</html>
