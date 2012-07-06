package rcadeserver

class LoginTagLib {
	def loginControl = {
		if(request.getSession(false) && session.user){
			out << "Hello, ${session.user.login}. "
			out << """[${link(action:"logout",
	controller:"user"){"Log Out"}}]"""
		} else {
			out << """[${link(action:"login",
	controller:"user"){"Log In"}}]"""
		}
	}
}
