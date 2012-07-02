
<%@ page import="rcadeserver.Game"%>
<!doctype html>
<html>
<head>
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
			<li><g:link class="create" action="create">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link></li>
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
		<div class="pagination">
			<g:paginate total="${gameInstanceTotal}" />
		</div>
	</div>
</body>
</html>
