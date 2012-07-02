<%@ page import="rcadeserver.Score"%>



<div
	class="fieldcontain ${hasErrors(bean: scoreInstance, field: 'player', 'error')} required">
	<label for="player"> <g:message code="score.player.label"
			default="Player" /> <span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id"
		from="${rcadeserver.Player.list()}" optionKey="id" required=""
		value="${scoreInstance?.player?.id}" class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: scoreInstance, field: 'score', 'error')} required">
	<label for="score"> <g:message code="score.score.label"
			default="Score" /> <span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="score" required=""
		value="${fieldValue(bean: scoreInstance, field: 'score')}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: scoreInstance, field: 'game', 'error')} required">
	<label for="game"> <g:message code="score.game.label"
			default="Game" /> <span class="required-indicator">*</span>
	</label>
	<g:select id="game" name="game.id" from="${rcadeserver.Game.list()}"
		optionKey="id" required="" value="${scoreInstance?.game?.id}"
		class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: scoreInstance, field: 'cabinetID', 'error')} ">
	<label for="cabinetID"> <g:message code="score.cabinetID.label"
			default="Cabinet ID" />

	</label>
	<g:textField name="cabinetID" value="${scoreInstance?.cabinetID}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: scoreInstance, field: 'arcadeName', 'error')} ">
	<label for="arcadeName"> <g:message
			code="score.arcadeName.label" default="Arcade Name" />

	</label>
	<g:textField name="arcadeName" value="${scoreInstance?.arcadeName}" />
</div>

