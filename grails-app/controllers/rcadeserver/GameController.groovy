package rcadeserver

import grails.converters.*

class GameController {

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
			def result = game.scores
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


	def scaffold = Game
}
