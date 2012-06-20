package rcadeserver
import java.util.Date;
class Player {
	static constraints = {
		name(blank:false)
		playerID(blank:false, display:false)
	}

	static hasMany = [scores:Score]

	BigInteger playerID
	String name

	String recentScore(){
		if(scores.empty){
			"No Scores"
		} else {
			def score = this.scores.asList()
			score.max{it.dateCreated}.toStringWithGame()
		}
	}

	String toString() {
		"${name}"
	}

}