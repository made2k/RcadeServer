package rcadeserver
class Game {
    static constraints = {
		romName(blank:false, unique:true)
		gameName(blank:true)
    }
	
	static hasMany = [scores:Score]
	String romName
	String gameName
	
	String toString() {
		"${gameName}"
	}
	
}