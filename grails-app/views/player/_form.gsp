<%@ page import="rcadeserver.Player"%>



<div
	class="fieldcontain ${hasErrors(bean: playerInstance, field: 'playerID', 'error')} required">
	<label for="playerID"> <g:message code="player.playerID.label"
			default="Player ID" /> <span class="required-indicator">*</span>
	</label>
	<g:field name="playerID" required=""
		value="${fieldValue(bean: playerInstance, field: 'playerID')}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: playerInstance, field: 'name', 'error')} required">
	<label for="name"> <g:message code="player.name.label"
			default="Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${playerInstance?.name}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: playerInstance, field: 'scores', 'error')} ">
	<label for="scores"> <g:message code="player.scores.label"
			default="Scores" />

	</label>

	<ul class="one-to-many">
		<g:each in="${playerInstance?.scores?}" var="s">
			<li><g:link controller="score" action="show" id="${s.id}">
					${s?.encodeAsHTML()}
				</g:link></li>
		</g:each>
		<li class="add"><g:link controller="score" action="create"
				params="['player.id': playerInstance?.id]">
				${message(code: 'default.add.label', args: [message(code: 'score.label', default: 'Score')])}
			</g:link></li>
	</ul>

</div>

