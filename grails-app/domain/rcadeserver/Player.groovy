package rcadeserver
import java.util.Date;
class Player {
    static constraints = {
		playerID(blank:false)
		name(blank:false)		
    }
	
	static hasMany = [scores:Score]
	
	BigInteger playerID
	String name
	
	String toString() {
		"${name}"
	}
	
}