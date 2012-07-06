
<%@ page import="rcadeserver.Score"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'score.label', default: 'Score')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#show-score" class="skip" tabindex="-1"><g:message
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
	<div id="show-score" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.show.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list score">

			<g:if test="${scoreInstance?.player}">
				<li class="fieldcontain"><span id="player-label"
					class="property-label"><g:message code="score.player.label"
							default="Player" /></span> <span class="property-value"
					aria-labelledby="player-label"><g:link controller="player"
							action="show" id="${scoreInstance?.player?.id}">
							${scoreInstance?.player?.encodeAsHTML()}
						</g:link></span></li>
			</g:if>

			<g:if test="${scoreInstance?.score}">
				<li class="fieldcontain"><span id="score-label"
					class="property-label"><g:message code="score.score.label"
							default="Score" /></span> <span class="property-value"
					aria-labelledby="score-label"><g:fieldValue
							bean="${scoreInstance}" field="score" /></span></li>
			</g:if>

			<g:if test="${scoreInstance?.game}">
				<li class="fieldcontain"><span id="game-label"
					class="property-label"><g:message code="score.game.label"
							default="Game" /></span> <span class="property-value"
					aria-labelledby="game-label"><g:link controller="game"
							action="show" id="${scoreInstance?.game?.id}">
							${scoreInstance?.game?.encodeAsHTML()}
						</g:link></span></li>
			</g:if>

			<g:if test="${scoreInstance?.cabinetID}">
				<li class="fieldcontain"><span id="cabinetID-label"
					class="property-label"><g:message
							code="score.cabinetID.label" default="Cabinet ID" /></span> <span
					class="property-value" aria-labelledby="cabinetID-label"><g:fieldValue
							bean="${scoreInstance}" field="cabinetID" /></span></li>
			</g:if>

			<g:if test="${scoreInstance?.dateCreated}">
				<li class="fieldcontain"><span id="dateCreated-label"
					class="property-label"><g:message
							code="score.dateCreated.label" default="Date" /></span> <span
					class="property-value" aria-labelledby="dateCreated-label"><g:formatDate
							date="${scoreInstance?.dateCreated}" /></span></li>
			</g:if>

			<g:if test="${scoreInstance?.arcadeName}">
				<li class="fieldcontain"><span id="arcadeName-label"
					class="property-label"><g:message
							code="score.arcadeName.label" default="Arcade Name" /></span> <span
					class="property-value" aria-labelledby="arcadeName-label"><g:fieldValue
							bean="${scoreInstance}" field="arcadeName" /></span></li>
			</g:if>

		</ol>
		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${scoreInstance?.id}" />
				<g:link class="edit" action="edit" id="${scoreInstance?.id}">
					<g:message code="default.button.edit.label" default="Edit" />
				</g:link>
				<g:actionSubmit class="delete" action="delete"
					value="${message(code: 'default.button.delete.label', default: 'Delete')}"
					onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
