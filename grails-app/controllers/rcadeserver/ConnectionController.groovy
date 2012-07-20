package rcadeserver

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class ConnectionController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index = {
		switch(request.method){
			case "POST":
				def connection = new Connection()
				connection.ipAddress = params.ipAddress
				connection.port = params.port
				if(connection.save()){
					response.status = 201 // Created
					render connection as XML
				}
				else{
					response.status = 500 //Internal Server Error
					render "Could not create new Score due to errors:\n ${connection.errors}"
				}
				break

			case "GET":
				render Connection.list() as XML
				break

			case "PUT":
				def connection = Connection.findById(params.connectionId)
				connection.properties = params.connection
				render connection.properties
				if(connection.save()) {
					response.status = 200 //Okay
					render connection as XML
				} else {
					response.status = 500 //Internal server error
					render "Could not update Connection due to errors:\n ${connection.errors}"
				}
				break

			case "DELETE":
				if(params.ipAddress){
					System.out.println(params.ipAddress);
					def connection = Connection.findByIpAddress(params.ipAddress)
					if(connection){
						connection.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.ipAddress} not found."
					}
				}
				else if(params.connectionId){
					System.out.println(params.connectionId);
					def connection = Connection.findById(params.connectionId)
					if(connection){
						connection.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.ipAddress} not found."
					}
				}
				else{
					response.status = 400 //Bad Request
					render "DELETE request must include the connection's IP\nExample: /rest/connection/\"127.0.0.1\""
				}
				break
		}
	}

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[connectionInstanceList: Connection.list(params), connectionInstanceTotal: Connection.count()]
	}

	def create() {
		[connectionInstance: new Connection(params)]
	}

	def save() {
		def connectionInstance = new Connection(params)
		if (!connectionInstance.save(flush: true)) {
			render(view: "create", model: [connectionInstance: connectionInstance])
			return
		}

		flash.message = message(code: 'generic.created.message', args: [message(code: 'connection.label', default: 'Connection'), connectionInstance.id])
		redirect(action: "show", id: connectionInstance.id)
	}

	def show() {
		def connectionInstance = Connection.get(params.id)
		if (!connectionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "list")
			return
		}

		[connectionInstance: connectionInstance]
	}

	def edit() {
		def connectionInstance = Connection.get(params.id)
		if (!connectionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "list")
			return
		}

		[connectionInstance: connectionInstance]
	}

	def update() {
		def connectionInstance = Connection.get(params.id)
		if (!connectionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (connectionInstance.version > version) {
				connectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'connection.label', default: 'Connectios')] as Object[],
						"Another user has updated this Connection while you were editing")
				render(view: "edit", model: [connectionInstance: connectionInstance])
				return
			}
		}

		connectionInstance.properties = params

		if (!connectionInstance.save(flush: true)) {
			render(view: "edit", model: [connectionInstance: connectionInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'connection.label', default: 'Connection'), connectionInstance.id])
		redirect(action: "show", id: connectionInstance.id)
	}

	def delete() {
		def connectionInstance = Connection.get(params.id)
		if (!connectionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "list")
			return
		}

		try {
			connectionInstance.delete(flush: true)
			flash.message = message(code: 'generic.deleted.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'connection.label', default: 'Connection'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
