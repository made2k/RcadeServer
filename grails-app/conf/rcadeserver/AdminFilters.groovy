package rcadeserver

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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

		basicAuth(controller:'*', action:"index"){
			before = {
				if(!session?.user?.admin){
					def authString = request.getHeader('Authorization')

					if(!authString){
						render(status: "400")
						return false
					}

					def encodedPair = authString - 'Basic '
					def decodedPair =  new String(new sun.misc.BASE64Decoder().decodeBuffer(encodedPair));
					def credentials = decodedPair.split(':')
					def user = User.findByLoginAndPassword(credentials[0], credentials[1].encodeAsSHA())
					if(!user?.isAdmin()){
						render(status: "401")
						return false
					}
				}
			}
		}

	}
}
