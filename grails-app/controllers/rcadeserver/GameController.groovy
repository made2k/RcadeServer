package rcadeserver

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class GameController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def xmlList = {
		render Game.list() as XML
	}

	def xmlShow = {
		render Game.get(params.id) as XML
	}

	def popular = {
		//Get count param as integer
		def num = params.int('count')
		num = ( num == null ? 10 : num )
		def allScores = Score.getAll()
		//Empty map
		def counts = [:]
		//Tally occurrences of games in the score listings
		for (s in allScores){
			if (counts[s.game] == null)
				counts[s.game] = 0
			counts[s.game] += 1
		}
		//Sort the map by occurrence count
		def tops = counts.sort{ a,b -> b.value <=> a.value}
		//Num is floored to the size of the keys list, less one (0-indexing)
		num = Math.min( num , tops.keySet().toArray().size()-1 )
		//If num won't validly slice popNames, popNames becomes empty
		List<String> popNames = num <= 0 ? [] : tops.keySet().toArray()[0 .. num]
		List<Game> popGames = []
		for (g in popNames) {
			popGames.add(g)
		}
		//popGames = popGames.sort{ a,b -> a.gameName <=> b.gameName}
		if (params.boolean('renderXML')){
			render popGames as XML
		}
		else{
			render(view:"list", model:[gameInstanceList:popGames, gameInstanceTotal:popGames.size()])
		}
	}

	def RSS = {
		// Find scores for this game
		// Find x most recent scores for this game
		// Render out an RSS XML document of these scores
		// http://grails.org/plugin/feeds

		// Which game?
		def theGame = Game.findByRomName(params.romName)
		// If no such game, 404
		if (theGame == null){
			response.sendError(404)
		}
		// Which player?
		def thePlayer = (params.playerName ? Player.findByName(params.playerName) : null)
		// Get scores
		def latestScores = Score.findAllByGame(theGame)
		render(feedType:"rss", feedVersion:"2.0"){
			// Latest scores in {gameName} set by {playerName | everyone}
			title = "Latest scores in " + theGame.gameName + " set by " + (params.playerName ? params.playerName : "everyone")
			// Should eventually point back to a reasonable web page
			link = "dummy.com"
			description = "High scores set in " + theGame.gameName

			latestScores.each(){ score ->
				// If a player was specified but this score does not match, don't render it
				if(params.playerName != null && score.player != thePlayer){
					return
				}
				entry{
					title = score.player.name + ": " + score.score
					link = "http://localhost:8080/RcadeServer/score/show/${score.id}"
					publishedDate = score.dateCreated
				}
			}
		}
	}

	def customXmlList = {
		def list = Game.list()
		render(contentType:"text/xml"){
			games{
				for(g in list){
					game(id:g.id){
						"rom-name"(g.romName)
						"game-name"(g.gameName)
					}
				}
			}
		}
	}

	def highScoreList = {
		def game = Game.findByRomName(params.romName)
		if(game) {
			def result = game.scores.asList()
			result.sort()
			render result
		} else {
			response.status = 404 //Not found
			render "Could not find game ${params.romName}"
		}
	}

	def index = {
		switch(request.method){
			case "POST":
			render "Create\n"
			def game = new Game(params.game)
			if(game.save()){
				response.status = 201 // Created
				//render game as XML
			}
			else{
				response.status = 500 //Internal Server Error
				render "Could not create new Game due to errors:\n ${game.errors}"
			}
			break

			case "GET":
			if(params.romName){
				// Put the proper id in params, then redirect to the single-item XML
				def g = Game.findByRomName(params.romName)
				params.id = g == null ? null : g.id
				redirect(action: "xmlShow", params: params)
				response.status = 200 //Okay
			}
			else{
				redirect(action: "xmlList")
			}
			break

			case "PUT":
			def game = Game.findByRomName(params.romName)
			game.properties = params.game
			if(game.save()) {
				response.status = 200 //Okay
				render game as XML
			} else {
				response.status = 500 //Internal server error
				render "Could not create new game due to errors:\n ${game.errors}"
			}
			break

			case "DELETE":
			if(params.romName){
				def game = Game.findByRomName(params.romName)
				if(game){
					game.delete()
					render "Successfully Deleted."
					response.status = 200 //Okay
				}else{
					response.status = 404 //Not Found
					render "${params.romName} not found."
				}
			}else{
				response.status = 400 //Bad Request
				render "DELETE request must include the romName\nExample: /rest/game/romName"

			}
			break
		}
	}

	def showHighScore() {
		def gameInstance = Game.get(params.id)
		if (!gameInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
			return
		}
		scores = gameInstance.scores
		if(scores)
		gameInstance.highScore = scores[0]
	}





	//*****************************************************************************

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 25, 100)
		params.sort="gameName"
		[gameInstanceList: Game.list(params), gameInstanceTotal: Game.count()]
	}

	def create() {
		[gameInstance: new Game(params)]
	}

	def save() {
		def gameInstance = new Game(params)
		if (!gameInstance.save(flush: true)) {
			render(view: "create", model: [gameInstance: gameInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'game.label', default: 'Game'), gameInstance.id])
		redirect(action: "show", id: gameInstance.id)
	}

	def show() {
		def gameInstance = Game.get(params.id)
		if (!gameInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
			return
		}

		[gameInstance: gameInstance]
	}

	def edit() {
		def gameInstance = Game.get(params.id)
		if (!gameInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
			return
		}

		[gameInstance: gameInstance]
	}

	def update() {
		def gameInstance = Game.get(params.id)
		if (!gameInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (gameInstance.version > version) {
				gameInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
				[message(code: 'game.label', default: 'Game')] as Object[],
				"Another user has updated this Game while you were editing")
				render(view: "edit", model: [gameInstance: gameInstance])
				return
			}
		}

		gameInstance.properties = params

		if (!gameInstance.save(flush: true)) {
			render(view: "edit", model: [gameInstance: gameInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'game.label', default: 'Game'), gameInstance.id])
		redirect(action: "show", id: gameInstance.id)
	}

	def delete() {
		def gameInstance = Game.get(params.id)
		if (!gameInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
			return
		}

		try {
			gameInstance.delete(flush: true)
			flash.message = message(code: 'generic.deleted.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
