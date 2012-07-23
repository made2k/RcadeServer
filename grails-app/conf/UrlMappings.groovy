import rcadeserver.Game;

class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/game"(controller:"game", action:"list")
		"/player"(controller:"player", action:"list")
		"/score"(controller:"score", action:"list")
		"/user"(controller:"user", action:"list")
		"/game/post"(controller:"game", action:"yammer")
		"/connection"(controller:"connection", action:"list")
		
		"/rest/game/$romName?"(controller:"game", action:"index")
		"/rest/game/RSS/$romName/$playerName?"(controller:"game", action:"RSS")
		"/rest/player/$playerId?"(controller:"player", action:"index")	//Need to change with RFID
		"/rest/player/rcade/$name?"(controller:"player", action:"index")
		"/rest/player/RSS/$playerName/$romName?"(controller:"player", action:"RSS")
		"/rest/score/$scoreId?"(controller:"score", action:"index")
		"/rest/game/$romName/highscore"(controller:"game", action:"highScoreList")
		"/rest/connection/rcade/$ipAddress?"(controller:"connection", action:"index")
		"/rest/connection/$connectionId?"(controller:"connection", action:"index")
		"/rest/user/$userId?"(controller:"user", action:"index")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
