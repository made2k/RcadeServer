import rcadeserver.Game;

class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/rest/game/$romName?"(controller:"game", action:"index")
		"/rest/player/$name?"(controller:"player", action:"index")	//Need to change with RFID
		"/rest/score/$scoreId?"(controller:"score", action:"index")
		"/rest/game/$romName/highscore"(controller:"game", action:"highScoreList")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
