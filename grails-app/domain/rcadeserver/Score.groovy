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
		"${score} : ${player}"
	}
	
	String toStringWithGame() {
		"${score} : ${game}"
	}
	
	static mapping = {
		sort score: "asc"
	}
	
	BigInteger getScore(){
		return this.score
	}
	
}