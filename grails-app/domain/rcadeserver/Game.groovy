package rcadeserver

class Game{
	static constraints = {
		romName(blank:false, unique:true)
		gameName(blank:true)
	}

	static hasMany = [scores:Score]
	String romName
	String gameName

	String getHighScore() {
		if(scores.empty)
			"No High Score"
		else {
			def score = this.scores.asList()
			score.max{it.score}
		}
	}

	Score highScore() {
		if(scores.empty)
			return null
		else {
			def score = this.scores.asList()
			score.max{it.score}
		}
	}

	def mc = [
		compare: {a,b-> a.equals(b)? 0: Math.abs(a)<Math.abs(b)? -1: 1 }
	] as Comparator


	String toString() {
		"${gameName}"
	}

}