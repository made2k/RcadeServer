
<%@ page import="rcadeserver.Connection"%>
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
<g:set var="entityName" value="${message(code: 'connection.label', default: 'Connection')}" />
<title><g:message code="generic.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-connection" class="skip" tabindex="-1"><g:message
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
	<div id="list-connection" class="content scaffold-list" role="main">
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
					<g:if test="${session?.user?.isAdmin()}">
						<th class="nohov"><input type="checkbox" name="batch-all"></th>
					</g:if>
					<g:sortableColumn property="ipAddress"
						title="${message(code: 'connection.ipAddress.label', default: 'Ip Address')}" />
					<g:sortableColumn property="port"
						title="${message(code: 'connection.port.label', default: 'Port')}" />
				</tr>
			</thead>
			<tbody>
				<g:each in="${connectionInstanceList}" status="i"
					var="connectionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<g:if test="${session?.user?.isAdmin() }">
							<td><input type="checkbox" name="batch" value="${connectionInstance.id}"></td>
						</g:if>
						<td><g:link action="show" id="${connectionInstance.id}">
								${fieldValue(bean: connectionInstance, field: "ipAddress")}
							</g:link></td>
						<td>
							${fieldValue(bean: connectionInstance, field: "port")}
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<g:if test="${session?.user?.isAdmin() }">
			<div id="batch-buttons">
				<button id="delete">
					Delete selected
					${params.controller}s
				</button>
			</div>
		</g:if>
		<div class="pagination">
			<g:if test="${connectionInstanceTotal <= params.max}">
				<span class="currentStep">1</span>
			</g:if>
			<g:else>
				<g:paginate total="${connectionInstanceTotal}" />
			</g:else>
		</div>
	</div>
</body>
</html>
