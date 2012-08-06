package rcadeserver
import java.util.Date;
class Player {
	static constraints = {
		name(blank:false)
		playerID(blank:false, display:false, unique:true)
	}

	static hasMany = [scores:Score]
	
	String playerID
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