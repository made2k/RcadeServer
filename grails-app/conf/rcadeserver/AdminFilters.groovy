package rcadeserver

class AdminFilters {


	def filters = {
		adminOnly(controller:'*', action:"(create|edit|update|delete|save)") {
			before = {
				if(!session?.user?.admin){
					flash.message = "Sorry, admin only"
					String targetURL = (request.forwardURI - request.contextPath)
					targetURL = targetURL.substring(0, targetURL.lastIndexOf('/'))
					redirect(uri:targetURL)
					return false
				}
			}
		}
	}
}
