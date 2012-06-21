<%@ page import="rcadeserver.Game" %>



<div class="fieldcontain ${hasErrors(bean: gameInstance, field: 'romName', 'error')} required">
	<label for="romName">
		<g:message code="game.romName.label" default="Rom Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="romName" required="" value="${gameInstance?.romName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: gameInstance, field: 'gameName', 'error')} ">
	<label for="gameName">
		<g:message code="game.gameName.label" default="Game Name" />
		
	</label>
	<g:textField name="gameName" value="${gameInstance?.gameName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: gameInstance, field: 'scores', 'error')} ">
	<label for="scores">
		<g:message code="game.scores.label" default="Scores" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${gameInstance?.scores?}" var="s">
    <li><g:link controller="score" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="score" action="create" params="['game.id': gameInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'score.label', default: 'Score')])}</g:link>
</li>
</ul>

</div>

