package rcadeserver

class Connection {

    static constraints = {
		ipAddress(blank:false, unique:true)
		port(blank:false)
    }
	
	String ipAddress
	String port
}
