package rcadeserver
import java.util.Date;
import java.util.List;

class Score {	
    static constraints = {
		player()
		score(matches:"[0-9]+")
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
	
	static List<Score> getLatest() {
		List<Score> allScores = Score.getAll()
		allScores.sort{a, b -> b.dateCreated <=> a.dateCreated}
		return allScores.subList(0, 5)
	}
}