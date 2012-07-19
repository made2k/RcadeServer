
<%@ page import="rcadeserver.User"%>
<!doctype html>
<html>
<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/batch-operations.js"></script>
<script>
$(document).ready(function()
{
	check();
	batch("${request.contextPath}", "${params.controller}");
	$("[name='batch-all']").click(check)
	
	function check()
	{
		$("[type='checkbox'][value='${session.user.id}']").each(function(i)
		{
			this.disabled = true;
			this.checked = false;
		});
	}
});
</script>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'user.label', default: 'User')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-user" class="skip" tabindex="-1"><g:message
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
	<div id="list-user" class="content scaffold-list" role="main">
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
					<th class="nohov"><input type="checkbox" name="batch-all"></th>
					<g:sortableColumn property="login"
						title="${message(code: 'user.login.label', default: 'Login')}" />

					<g:sortableColumn property="role"
						title="${message(code: 'user.role.label', default: 'Role')}" />
				</tr>
			</thead>
			<tbody>
				<g:each in="${userInstanceList}" status="i" var="userInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><input type="checkbox" name="batch" value="${userInstance.id}"></td>
						<td><g:link action="show" id="${userInstance.id}">
								${fieldValue(bean: userInstance, field: "login")}
							</g:link></td>
						<td>
							${fieldValue(bean: userInstance, field: "role")}
						</td>

					</tr>
				</g:each>
			</tbody>
		</table>
		<div id="batch-buttons">
			<button id="delete">Delete selected ${params.controller}s</button>
		</div>
		<div class="pagination">
			<g:if test="${userInstanceTotal <= params.max}">
    			<span class="currentStep">1</span>
    		</g:if>
    		<g:else>
				<g:paginate total="${userInstanceTotal}" />
			</g:else>
		</div>
	</div>
</body>
</html>
