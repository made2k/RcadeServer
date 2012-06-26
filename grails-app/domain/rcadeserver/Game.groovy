package rcadeserver

class Game{
	static constraints = {
		romName(blank:false, unique:true)
		gameName(blank:true)
	}

	static hasMany = [scores:Score]
	String romName
	String gameName

	String getHighScoreString() {
		if(scores.empty)
			"No High Score"
		else {
			def score = this.scores.asList()
			score.max{it.score}
		}
	}
	
	Score getHighScore() {
		if(scores.empty)
			"No High Score"
		else {
			def score = this.scores.asList()
			score.max{it.score}
		}
	}

	String highScore(int count) {
		String formattedHighScores = ""
		List<Score> highScores = []
		List<Score> allScores = scores.asList()
		for(int i = 0; i < count; i++){
			if(allScores.empty) {
				break
			} else {
				Score topScore = allScores.max{it.score}
				highScores.add(topScore)
				//allScores.minus(topScore)
				allScores.remove(topScore)
			}
		}
		for (score in highScores){
			formattedHighScores += score.toString() + "\n"
		}
		formattedHighScores
	}


	String toString() {
		"${gameName}"
	}

}