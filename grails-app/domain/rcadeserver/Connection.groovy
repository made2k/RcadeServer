package rcadeserver

class Connection {

    static constraints = {
		ipAddress(blank:false, unique:true)
		port(blank:false)
    }
	
	String ipAddress
	String port
	
	static transients = ['active']
	
	Boolean active
	
	void setActive(Boolean bool){
		println "Setting Active to " + bool
		active = bool
		println "Active is currently " + active + " for " + ipAddress
	}
}
