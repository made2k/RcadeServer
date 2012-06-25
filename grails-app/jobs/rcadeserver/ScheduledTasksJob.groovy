package rcadeserver



class ScheduledTasksJob {
	static triggers = {
		//simple name: 'mySimpleTrigger', startDelay: 1000, repeatInterval: 15000
		cron name: 'cronTrigger', cronExpression: "0 30 17 ? * MON-FRI *"
	}

	def execute() {
		String currentDir = new File(".").getAbsolutePath()
		currentDir = currentDir.substring(0, currentDir.length()-1)
		def cmd = ['python', currentDir + "/SupportScripts/PostToYammer.py", 'this is a test from grails!']
		cmd.execute()
	}

}
