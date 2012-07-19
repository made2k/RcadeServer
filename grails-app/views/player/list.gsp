
<%@ page import="rcadeserver.Player"%>
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
	value="${message(code: 'player.label', default: 'Player')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-player" class="skip" tabindex="-1"><g:message
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
	<div id="list-player" class="content scaffold-list" role="main">
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
					<!-- <g:sortableColumn property="playerID" title="${message(code: 'player.playerID.label', default: 'Player ID')}" /> -->
					<g:if test="${session?.user?.isAdmin()}">
						<th class="nohov"><input type="checkbox" name="batch-all"></th>
					</g:if>
					<g:sortableColumn property="name"
						title="${message(code: 'player.name.label', default: 'Name')}" />
					<th>Most Recent Score</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${playerInstanceList}" status="i" var="playerInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<!-- <td><g:link action="show" id="${playerInstance.id}">${fieldValue(bean: playerInstance, field: "playerID")}</g:link></td> -->
						<g:if test="${session?.user?.isAdmin() }">
							<td><input type="checkbox" name="batch" value="${playerInstance.id}"></td>
						</g:if>
						<td><g:link action="show" id="${playerInstance.id}">
								${fieldValue(bean: playerInstance, field: "name")}
							</g:link></td>

						<td><g:link action="show" id="${playerInstance.id}">
								${playerInstance.recentScore()}
							</g:link></td>

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
    		<g:if test="${playerInstanceTotal <= params.max}">
    			<span class="currentStep">1</span>
    		</g:if>
    		<g:else>
				<g:paginate total="${playerInstanceTotal}" />
			</g:else>
		</div>
	</div>
</body>
</html>
