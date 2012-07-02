package rcadeserver

import org.springframework.stereotype.Controller;



class ScheduledTasksJob {
	static triggers = {
		//simple name: 'mySimpleTrigger', startDelay: 1000, repeatInterval: 15000
		cron name: 'cronTrigger', cronExpression: "0 30 17 ? * MON-FRI *"
	}

	def getPopularGames() {
		//Get count param as integer
		def num = 3
		def allScores = Score.getAll()
		//Empty map
		def counts = [:]
		//Tally occurrences of games in the score listings
		for (s in allScores){
			if (counts[s.game] == null)
				counts[s.game] = 0
			counts[s.game] += 1
		}
		//Sort the map by occurrence count
		def tops = counts.sort{ a,b -> b.value <=> a.value}
		//Num is floored to the size of the keys list, and then decremented
		//by one since the number of items displayed is one more than
		//the index of the last item so displayed
		num = Math.min( num , tops.keySet().toArray().size() )
		num--
		List<String> popNames = tops.keySet().toArray()[0 .. Math.max(num,0)]
		List<Game> popGames = []
		for ( g in popNames) {
			popGames.add(g)
		}

		println popGames
		def today = new Date().format('EEEEE MMMMM dd, yyyy')
		String popularGames = "High Scores for " + today + "\n\n"
		for(game in popGames) {
			if(game != null) {
				popularGames += game.toString() + "\n" + game.highScore(2).toString() + "\n"
			}
		}
		return popularGames
	}


	def execute() {
		String popularGames = getPopularGames()
		println popularGames + "\n"
		String currentDir = new File(".").getAbsolutePath()
		currentDir = currentDir.substring(0, currentDir.length()-1)
		def cmd = ['python', currentDir + "/SupportScripts/PostToYammer.py", popularGames]
		//cmd.execute()
	}

}
