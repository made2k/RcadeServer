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
				if(params.romName){render Game.findByRomName(params.romName) as XML}
				else{render Game.list() as XML}
				break

			case "PUT":
				def game = Game.findByRomName(params.romName)
				game.properties = params.game
				render game.properties
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
