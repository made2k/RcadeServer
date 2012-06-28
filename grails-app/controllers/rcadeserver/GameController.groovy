package rcadeserver

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class GameController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	//    def index() {
	//        redirect(action: "list", params: params)
	//    }
	

	def xmlList = {
		render Game.list() as XML
	}

	def xmlShow = {
		render Game.get(params.id) as XML
	}
	
	def popular = {
		//Get count param as integer
		def num = params.int('count')
		num = ( num == null ? 1 : num )
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
		//Num is floored to the size of the keys list, and then decremented
		//by one since the number of items displayed is one more than
		//the index of the last item so displayed
		num = Math.min( num , tops.keySet().toArray().size() )
		num--
		List<String> popNames = tops.keySet().toArray()[0 .. Math.max(num,0)]
		List<Game> popGames = []
		for ( g in popNames) {
			popGames.add(g)
		}
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
		def theID = params.int('id')
		def theGame = Game.findById(theID)
		def latestScores = Score.findAllByGame(theGame)
		render(feedType:"rss", feedVersion:"2.0"){
			title = "top-scoring players of " + theGame.gameName
			link = "dummy.com"
			description = "High scores set in " + theGame.gameName
			
			latestScores.each(){ score ->
				entry{
					title = score.player.name + ": " + score.score
					link = "http://localhost:8080/RcadeServer/score/show/${score.id}"
					publishedDate = score.dateCreated
				}
			}
		}
	}

	def yammer = {
		String currentDir = new File(".").getAbsolutePath()
		currentDir = currentDir.substring(0, currentDir.length()-1)
		def cmd = ['python', currentDir + "/SupportScripts/PostToYammer.py", 'this is a test from grails!']
		cmd.execute()
		redirect(action: "list")
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
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'game.label', default: 'Game'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
