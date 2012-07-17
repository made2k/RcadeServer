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
					def connection = Connection.findByIpAddress(params.ipAddress)
					if(connection){
						connection.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.ipAddress} not found."
					}
				}else{
					response.status = 400 //Bad Request
					render "DELETE request must include the connection's IP\nExample: /rest/connection/\"127.0.0.1\""
				}
				break
		}
	}

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[connectionsInstanceList: Connection.list(params), connectionsInstanceTotal: Connection.count()]
	}

	def create() {
		[connectionsInstance: new Connection(params)]
	}

	def save() {
		def connectionsInstance = new Connection(params)
		if (!connectionsInstance.save(flush: true)) {
			render(view: "create", model: [connectionsInstance: connectionsInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'connections.label', default: 'Connections'), connectionsInstance.id])
		redirect(action: "show", id: connectionsInstance.id)
	}

	def show() {
		def connectionsInstance = Connection.get(params.id)
		if (!connectionsInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "list")
			return
		}

		[connectionsInstance: connectionsInstance]
	}

	def edit() {
		def connectionsInstance = Connection.get(params.id)
		if (!connectionsInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "list")
			return
		}

		[connectionsInstance: connectionsInstance]
	}

	def update() {
		def connectionsInstance = Connection.get(params.id)
		if (!connectionsInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (connectionsInstance.version > version) {
				connectionsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'connections.label', default: 'Connections')] as Object[],
						"Another user has updated this Connections while you were editing")
				render(view: "edit", model: [connectionsInstance: connectionsInstance])
				return
			}
		}

		connectionsInstance.properties = params

		if (!connectionsInstance.save(flush: true)) {
			render(view: "edit", model: [connectionsInstance: connectionsInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'connections.label', default: 'Connections'), connectionsInstance.id])
		redirect(action: "show", id: connectionsInstance.id)
	}

	def delete() {
		def connectionsInstance = Connection.get(params.id)
		if (!connectionsInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "list")
			return
		}

		try {
			connectionsInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'connections.label', default: 'Connections'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
