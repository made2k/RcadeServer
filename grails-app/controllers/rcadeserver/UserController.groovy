package rcadeserver

import org.springframework.dao.DataIntegrityViolationException

class UserController {

	def beforeInterceptor = [action:this.&auth, except:['login', 'logout', 'authenticate']]
	
	def debug(){
		println "DEGUB: ${actionUri} called."
		println "DEBUG: ${params}"
	}
	
	def login = {}
	def logout = {
		flash.message = "Goodbye, ${session.user.login}."
		session.user = null
		redirect(action:'login')
		}
	def authenticate = {
		def user =
				User.findByLoginAndPassword(params.login, params.password.encodeAsSHA())
		if(user){
			session.user = user
			flash.message = "Hello, ${user.login}!"
			redirect(uri: '/')
		}else{
			flash.message =
					"Sorry, ${params.login}. Please try again."
			redirect(action:"login")
		}
	}
	
	def auth() {
		if(!session.user) {
		  redirect(controller:"user", action:"login")
		  return false
		}
		 
		if(!session.user.admin){
		  flash.message = "Tsk tsk—admins only"
		  redirect(uri:"/")
		  return false
		}
	  }

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
		switch(request.method){
			case "DELETE":
				if(params.userId){
					def user = User.findById(params.userId)
					if(user){
						user.delete()
						render "Successfully Deleted."
					}else{
						response.status = 404 //Not Found
						render "${params.userId} not found."
					}
				}else{
					response.status = 400 //Bad Request
					render "DELETE request must include the user's ID\nExample: /rest/user/userId"
				}
				break
		}
	}

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[userInstanceList: User.list(params), userInstanceTotal: User.count()]
	}

	def create() {
		[userInstance: new User(params)]
	}

	def save() {
		def userInstance = new User(params)
		if (!userInstance.save(flush: true)) {
			render(view: "create", model: [userInstance: userInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
		redirect(action: "show", id: userInstance.id)
	}

	def show() {
		def userInstance = User.get(params.id)
		if (!userInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "list")
			return
		}

		[userInstance: userInstance]
	}

	def edit() {
		def userInstance = User.get(params.id)
		if (!userInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "list")
			return
		}

		[userInstance: userInstance]
	}

	def update() {
		def userInstance = User.get(params.id)
		if (!userInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (userInstance.version > version) {
				userInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'user.label', default: 'User')] as Object[],
						"Another user has updated this User while you were editing")
				render(view: "edit", model: [userInstance: userInstance])
				return
			}
		}

		userInstance.properties = params

		if (!userInstance.save(flush: true)) {
			render(view: "edit", model: [userInstance: userInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
		redirect(action: "show", id: userInstance.id)
	}

	def delete() {
		def userInstance = User.get(params.id)
		if (!userInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "list")
			return
		}

		try {
			userInstance.delete(flush: true)
			flash.message = message(code: 'generic.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])
			redirect(action: "show", id: params.id)
		}
	}
}
