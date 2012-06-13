package rcadeserver

import grails.converters.*
import java.math.*

class ScoreController {
	def scaffold = Score

	def xmlList = {
		render Score.list() as XML
	}

	def xmlShow = {
		render Score.get(params.id) as XML
	}


	def index = {
		switch(request.method){
			case "POST":
				def score = new Score()
				score.score = new BigInteger(params.score)
				score.arcadeName = params.arcadeName
				score.cabinetID = params.cabinetID
				score.dateCreated = new Date()
				score.game = Game.findByRomName(params.game)
				score.player = Player.findByName(params.player)	//Change for RFID
				if(score.save()){
					response.status = 201 // Created
					render score as XML
				}
				else{
					response.status = 500 //Internal Server Error
					render "Could not create new Score due to errors:\n ${score.errors}"
				}
				break

			case "GET":
				if(params.scoreId){render Score.findById(params.scoreId) as XML}
				else{render Score.list() as XML}
				break

			case "PUT":
				def score = Score.findById(params.scoreId)
				score.properties = params.score
				render score.properties
				if(score.save()) {
					response.status = 200 //Okay
					render score as XML
				} else {
					response.status = 500 //Internal server error
					render "Could not create new Score due to errors:\n ${score.errors}"
				}
				break

			case "DELETE":
				if(params.scoreId){
					def score = Score.findById(params.scoreId)
					if(score){
						score.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.scoreId} not found."
					}
				}else{
					response.status = 400 //Bad Request
					render "DELETE request must include the score's ID\nExample: /rest/score/scoreId"
				}
				break
		}
	}


}
