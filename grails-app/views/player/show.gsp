
<%@ page import="rcadeserver.Player"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'player.label', default: 'Player')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#show-player" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<li><g:link class="list" action="list">
					<g:message code="default.list.label" args="[entityName]" />
				</g:link></li>
			<g:if test="${session?.user?.isAdmin() }">
				<li><g:link class="create" action="create">
						<g:message code="default.new.label" args="[entityName]" />
					</g:link></li>
			</g:if>
		</ul>
	</div>
	<div id="show-player" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.show.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list player">
			<!--
				<g:if test="${playerInstance?.playerID}">
				<li class="fieldcontain">
					<span id="playerID-label" class="property-label"><g:message code="player.playerID.label" default="Player ID" /></span>
					
						<span class="property-value" aria-labelledby="playerID-label"><g:fieldValue bean="${playerInstance}" field="playerID"/></span>
					
				</li>
				</g:if>
-->

			<g:if test="${playerInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label">
						<g:message code="player.name.label" default="Name" />
					</span>
					<span class="property-value" aria-labelledby="name-label">
						<g:fieldValue bean="${playerInstance}" field="name" />
					</span>
				</li>
			</g:if>
			
			<g:if test="${playerInstance?.scores}">
				<li class="fieldcontain">
					<span id="scores-label" class="property-label">
						<g:message code="player.scores.label" default="Recent Scores" />
					</span>
					<g:each in="${playerInstance.scores.sort{a,b-> (a.dateCreated>b.dateCreated ? -1 : 1)}}" var="s" status="i">
						<g:if test="${ i<10 }">
							<span class="property-value" aria-labelledby="scores-label">
								<g:link controller="score" action="show" id="${s.id}">
									${s?.toStringWithGame().encodeAsHTML()}
								</g:link>
							</span>
						</g:if>
					</g:each>
				</li>
			</g:if>
			<g:else>
				<li class="fieldcontain">
					<span id="scores-label" class="property-label">
						<g:message code="player.scores.label" default="Recent Scores" />
					</span>
					<span class="property-value" aria-labelledby="scores-label">
						<g:message code="player.scores.label" default="None" />
					</span>
				</li>
			</g:else>
		</ol>
		<g:if test="${session?.user?.isAdmin() }">
		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${playerInstance?.id}" />
				<g:link class="edit" action="edit" id="${playerInstance?.id}">
					<g:message code="default.button.edit.label" default="Edit" />
				</g:link>
				<g:actionSubmit class="delete" action="delete"
					value="${message(code: 'default.button.delete.label', default: 'Delete')}"
					onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			</fieldset>
		</g:form>
		</g:if>
	</div>
</body>
</html>
