import rcadeserver.*;
class BootStrap
{
	def init = { servletContext ->
		def admin = User.findByLogin("admin")
		if(!admin){
			admin = new User(login:"admin", password:"password", role:"admin")
			admin.save()
			if(admin.hasErrors())
				println admin.errors
		}
	}

	def destroy =
	{
	}
}