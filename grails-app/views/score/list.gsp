
<%@ page import="rcadeserver.Score"%>
<!doctype html>
<html>
<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script>
$(document).ready(function()
{
	$("[type='checkbox']").each(function(i)
	{
		this.checked = false;
	});
	$("[name='batch-all']").click(function()
	{
		$("[name='batch']").prop("checked", $("[name='batch-all']").prop("checked"));
	});
	$("button").click(function()
	{
		$("[name='batch']:checked").parent().parent().fadeOut("slow");
		str = "Score IDs selected:" + "\n";
		$("[name='batch']:checked").each(function(i)
		{
			str += this.value + "\n"
			$.ajax({
				url: "${request.contextPath}/rest/score/" + this.value,
				type: "DELETE",
				dataType: 'json',
				success: function(data) {
					alert("Data: " + data);
				},
				error: function(request, status, error) {
					//alert("Error: " + error);
				}
			});

		});
		//alert(str);
	});
});
</script>
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
		<form id="batch-operation" name="batch-operation">
		<table>
			<thead>
				<tr>
					<g:if test="${session?.user?.isAdmin()}">
						<th class="nohov"><input type="checkbox" name="batch-all"></th>
					</g:if>
					<g:sortableColumn property="player"
					title="${message(code: 'player.name.label', default: 'Player') }" />
					<g:sortableColumn property="score"
						title="${message(code: 'score.score.label', default: 'Score')}" />	
					<g:sortableColumn property="game"
					title="${message(code: 'score.game.label', default: 'Game') }" />
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
						<g:if test="${session?.user?.isAdmin() }">
							<td><input type="checkbox" name="batch" value="${scoreInstance.id}"></td>
						</g:if>
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
		</form>
		<g:if test="${session?.user?.isAdmin() }">
			<div id="batch-buttons">
				<button id="delete">Test</button>
			</div>
		</g:if>
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
