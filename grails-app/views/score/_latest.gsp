
<%@ page import="rcadeserver.Score"%>
	<h1 id="LSheader" class="center">
		Latest Scores
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<table>
		<thead>
			<tr class="nohov">
				<th class="nohov" id="latest">Player</th>
				<th class="nohov" id="latest">Score</th>
				<th class="nohov" id="latest">Game</th>
				<th class="nohov" id="latest">Cabinet ID</th>
				<th class="nohov" id="latest">Date</th>
				<th class="nohov" id="latest">Arcade Name</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${Score.class.getLatest()}" status="i"
				var="scoreInstance">
				<tr id="${i}" class="${(i % 2) == 0 ? 'even' : 'odd'} nohov">
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
