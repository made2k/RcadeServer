package rcadeserver

import javax.net.ssl.SSLEngineResult.Status;

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class PlayerController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index = {
		switch(request.method){
			case "POST":
				render "Create\n"
				def player = new Player(params.player)
				if(player.save()){
					response.status = 201 // Created
					render player as XML
				}
				else{
					response.status = 500 //Internal Server Error
					render "Could not create new Player due to errors:\n ${player.errors}"
				}
				break

			case "GET":
				if(params.name){render Player.findByName(params.name) as XML}
				else{render Player.list() as XML}
				break

			case "PUT":
				def player = Player.findByPlayerIDAndName(params.playerId, params.name)
				if(!player){
					response.status = 404
					render "Player ${params.name} not found"
					break
				}
				
				player.properties = params.player
				
				if(player.save()) {
					response.status = 200 //Okay
					render player as XML
				} else {
					response.status = 500 //Internal server error
					render "Could not create new Player due to errors:\n ${player.errors}"
				}
				break

			case "DELETE":
				if(params.playerId){
					def player = Player.findById(params.playerId)
					if(player){
						player.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.playerID} not found."
					}
				}else if(params.name){
					def player = Player.findByName(params.name)
					if(player){
						player.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.name} not found."
					}
				}else{
					response.status = 400 //Bad Request
					render "DELETE request must include the player's ID\nExample: /rest/player/playerID"
				}
				break
		}
	}

	def RSS = {
		// Find scores for this game
		// Find x most recent scores for this game
		// Render out an RSS XML document of these scores
		// http://grails.org/plugin/feeds

		// Which player?
		def thePlayer = Player.findByName(params.playerName)
		// If player not found, return 404
		if (thePlayer == null){
			response.sendError(404)
		}
		// Which game?
		def theGame = (params.gameName ? Game.findByRomName(params.romName) : null)
		// Get scores
		def latestScores = Score.findAllByPlayer(thePlayer)
		render(feedType:"rss", feedVersion:"2.0"){
			// Latest scores in {gameName} set by {playerName | everyone}
			title = "Latest scores set by " + thePlayer.name + " in " + (params.romName ? params.romName : "all games")
			// Should eventually point back to a reasonable web page
			link = "dummy.com"
			description = "High scores set by " + thePlayer.name

			latestScores.each(){ score ->
				// If a game was specified but this score does not match, don't render it
				if(params.romName != null && score.game != theGame){
					return
				}
				entry{
					title = score.game.gameName + ": " + score.score
					link = "http://localhost:8080/RcadeServer/score/show/${score.id}"
					publishedDate = score.dateCreated
				}
			}
		}
	}



	//************************************************************************************

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 25, 100)
		params.sort="name"
		[playerInstanceList: Player.list(params), playerInstanceTotal: Player.count()]
	}

	def create() {
		[playerInstance: new Player(params)]
	}

	def save() {
		def playerInstance = new Player(params)
		if (!playerInstance.save(flush: true)) {
			render(view: "create", model: [playerInstance: playerInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'player.label', default: 'Player'), playerInstance.id])
		redirect(action: "show", id: playerInstance.id)
	}

	def show() {
		def playerInstance = Player.get(params.id)
		if (!playerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "list")
			return
		}

		[playerInstance: playerInstance]
	}

	def edit() {
		def playerInstance = Player.get(params.id)
		if (!playerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "list")
			return
		}

		[playerInstance: playerInstance]
	}

	def update() {
		def playerInstance = Player.get(params.id)
		if (!playerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (playerInstance.version > version) {
				playerInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'player.label', default: 'Player')] as Object[],
						"Another user has updated this Player while you were editing")
				render(view: "edit", model: [playerInstance: playerInstance])
				return
			}
		}

		playerInstance.properties = params

		if (!playerInstance.save(flush: true)) {
			render(view: "edit", model: [playerInstance: playerInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'player.label', default: 'Player'), playerInstance.id])
		redirect(action: "show", id: playerInstance.id)
	}

	def delete() {
		def playerInstance = Player.get(params.id)
		if (!playerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "list")
			return
		}

		try {
			playerInstance.delete(flush: true)
			flash.message = message(code: 'generic.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
