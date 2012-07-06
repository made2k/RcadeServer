
<%@ page import="rcadeserver.Score"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'score.label', default: 'Score')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-score" class="skip" tabindex="-1"><g:message
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
	<div id="list-score" class="content scaffold-list" role="main">
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

					<th><g:message code="score.player.label" default="Player" /></th>

					<g:sortableColumn property="score"
						title="${message(code: 'score.score.label', default: 'Score')}" />

					<th><g:message code="score.game.label" default="Game" /></th>

					<g:sortableColumn property="cabinetID"
						title="${message(code: 'score.cabinetID.label', default: 'Cabinet Name')}" />

					<g:sortableColumn property="dateCreated"
						title="${message(code: 'score.dateCreated.label', default: 'Date Played')}" />

					<g:sortableColumn property="arcadeName"
						title="${message(code: 'score.arcadeName.label', default: 'Arcade Name')}" />

				</tr>
			</thead>
			<tbody>
				<g:each in="${scoreInstanceList}" status="i" var="scoreInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link action="show" id="${scoreInstance.id}">
								${fieldValue(bean: scoreInstance, field: "player")}
							</g:link></td>

						<td>
							${fieldValue(bean: scoreInstance, field: "score")}
						</td>

						<td>
							${fieldValue(bean: scoreInstance, field: "game")}
						</td>

						<td>
							${fieldValue(bean: scoreInstance, field: "cabinetID")}
						</td>

						<td><g:formatDate date="${scoreInstance.dateCreated}" /></td>

						<td>
							${fieldValue(bean: scoreInstance, field: "arcadeName")}
						</td>

					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:if test="${scoreInstanceTotal <= params.max}">
    			<span class="currentStep">1</span>
    		</g:if>
    		<g:else>
				<g:paginate total="${scoreInstanceTotal}" />
			</g:else>
		</div>
	</div>
</body>
</html>
