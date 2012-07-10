package rcadeserver

class Game{
	static constraints = {
		romName(blank:false, unique:true)
		gameName(blank:false)
	}

	static hasMany = [scores:Score]
	String romName
	String gameName
	int year
	String manufacturer
	String clone_of
	String rom_of
	String display_type
	String screen_type
	String driver_status
	String color_status
	String category
	

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
		List<Score> highScores = []	//Array for the day's high scores to be stored in
		List<Score> allScores = scores.asList()
		for(int i = 0; i < count;){	//Display the number of scores desired
			if(allScores.empty) {
				break
			} else {
				Score topScore = allScores.max{it.score}
				Date today = new Date()
				
				if(today.minus(topScore.dateCreated) <= 1){
					highScores.add(topScore)
					i++
				}
				
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