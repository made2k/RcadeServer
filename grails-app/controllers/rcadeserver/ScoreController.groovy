package rcadeserver

import java.util.List;

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class ScoreController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	//    def index() {
	//        redirect(action: "list", params: params)
	//    }


	def xmlList = {
		render Score.list() as XML
	}
	
	def xmlLatest = {
		render Score.list() as XML
	}
	
	def latestAJAX = {
		render(view:"_latest")
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


	//*****************************************************************************************

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
//		params.sort = "dateCreated"
//		params.order = "desc"
		[scoreInstanceList: Score.list(params), scoreInstanceTotal: Score.count()]
	}

	def create() {
		[scoreInstance: new Score(params)]
	}

	def save() {
		def scoreInstance = new Score(params)
		if (!scoreInstance.save(flush: true)) {
			render(view: "create", model: [scoreInstance: scoreInstance])
			return
		}

		flash.message = message(code: 'generic.created.message', args: [message(code: 'score.label', default: 'Score'), scoreInstance.id])
		redirect(action: "show", id: scoreInstance.id)
	}

	def show() {
		def scoreInstance = Score.get(params.id)
		if (!scoreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "list")
			return
		}

		[scoreInstance: scoreInstance]
	}

	def edit() {
		def scoreInstance = Score.get(params.id)
		if (!scoreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "list")
			return
		}

		[scoreInstance: scoreInstance]
	}

	def update() {
		def scoreInstance = Score.get(params.id)
		if (!scoreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (scoreInstance.version > version) {
				scoreInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'score.label', default: 'Score')] as Object[],
						"Another user has updated this Score while you were editing")
				render(view: "edit", model: [scoreInstance: scoreInstance])
				return
			}
		}

		scoreInstance.properties = params

		if (!scoreInstance.save(flush: true)) {
			render(view: "edit", model: [scoreInstance: scoreInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'score.label', default: 'Score'), scoreInstance.id])
		redirect(action: "show", id: scoreInstance.id)
	}

	def delete() {
		def scoreInstance = Score.get(params.id)
		if (!scoreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "list")
			return
		}

		try {
			scoreInstance.delete(flush: true)
			flash.message = message(code: 'generic.deleted.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'score.label', default: 'Score'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
	
	def latest() {
		render(view: "latest")
		return
	}
}
