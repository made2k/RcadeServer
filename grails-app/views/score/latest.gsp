
<%@ page import="rcadeserver.Score"%>
<!doctype html>
<html>
<head>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'latest.css')}"
	type="text/css">
<g:set var="entityName"
	value="${message(code: 'score.label', default: 'Latest Scores')}" />
<title><g:message code="default.generic.label" args="[entityName]" /></title>
</head>
<body>
	<div id="list-score" class="content scaffold-list" role="main">
		<h1>
			<g:message code="default.generic.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<th>Player</th>
					<th>Score</th>
					<th>Game</th>
					<th>Cabined ID</th>
					<th>Date</th>
					<th>Arcade Name</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${Score.class.getLatest()}" status="i" var="scoreInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
							${fieldValue(bean: scoreInstance, field: "player")}
						</td>
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
	</div>
</body>
</html>
