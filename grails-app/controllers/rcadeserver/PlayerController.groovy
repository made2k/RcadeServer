package rcadeserver

import grails.converters.*;

class PlayerController {

	def scaffold = Player

	def xmlList = {
		render Player.list() as XML
	}

	def xmlShow = {
		render Player.get(params.id) as XML
	}

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
				def player = Player.findByPlayerID(params.playerID)
				player.properties = params.player
				render player.properties
				if(player.save()) {
					response.status = 200 //Okay
					render player as XML
				} else {
					response.status = 500 //Internal server error
					render "Could not create new Player due to errors:\n ${player.errors}"
				}
				break

			case "DELETE":
				if(params.playerID){
					def player = Player.findByPlayerID(params.playerID)
					if(player){
						player.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.playerID} not found."
					}
				}else{
					response.status = 400 //Bad Request
					render "DELETE request must include the player's ID\nExample: /rest/player/playerID"
				}
				break
		}
	}
}
