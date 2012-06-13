package rcadeserver
import java.util.Date;
class Score {
    static constraints = {
		player()
		score()
		game()
		cabinetID()
		dateCreated()
	}
	
	static belongsTo = [player:Player, game:Game]
	BigInteger score
	String cabinetID
	Date dateCreated
	String arcadeName
	
	String toString() {
		"${player}:${score}"
	}
}