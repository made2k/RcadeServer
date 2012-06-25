package rcadeserver



import org.junit.*
import grails.test.mixin.*

@TestFor(GameController)
@Mock(Game)
class GameControllerTests {

	protected void setup(){
		super.setup()
		controller = new GameController()
		mockController(GameController)
	}
	
    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex_Post() {
		
		// Mock an http POST request
		request.method = "POST"
		// With a mock Game object in the params
		// Mock nav to index for Game domain
        controller.index()
		// We expect to be bounced to xmlList action by index@POST
        assert response.redirectedUrl == "/game/xmlList"
    }
	
	void testIndex_Get_Anonymous() {	
		// Mock an http GET request without specified rom
		request.method = "GET"
		// Mock nav to index for Game domain
		controller.index()
		// We expect to be bounced to xmlList action by index@GET
		assert response.redirectedUrl == "/game/xmlList"
	}
		
	void testIndex_Get_Named() {
		// Mock an http GET request with specified rom (not in listing)
		request.method = "GET"
		params.romName = "a"
		// Mock nav to index for Game domain
		controller.index()
		// We expect to be bounced to xmlList action by index@GET
		assert response.redirectedUrl == "/game/xmlShow/?romName=" + params.romName
	}
		
	void textIndex_Put() {
		// Mock an http GET request
		request.method = "GET"
		// Mock nav to index for Game domain
		controller.index()
		// We expect to be bounced to xmlList action by index@GET
		assert response.redirectedUrl == "/game/xmlList"
	}
		
	void testIndex_Delete() {
		// Mock an http GET request
		request.method = "GET"
		// Mock nav to index for Game domain
		controller.index()
		// We expect to be bounced to xmlList action by index@GET
		assert response.redirectedUrl == "/game/xmlList"
    }

    void testList() {

        def model = controller.list()

        assert model.gameInstanceList.size() == 0
        assert model.gameInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.gameInstance != null
    }

    void testSave() {
        controller.save()

        assert model.gameInstance != null
        assert view == '/game/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/game/show/1'
        assert controller.flash.message != null
        assert Game.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/game/list'


        populateValidParams(params)
        def game = new Game(params)

        assert game.save() != null

        params.id = game.id

        def model = controller.show()

        assert model.gameInstance == game
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/game/list'


        populateValidParams(params)
        def game = new Game(params)

        assert game.save() != null

        params.id = game.id

        def model = controller.edit()

        assert model.gameInstance == game
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/game/list'

        response.reset()


        populateValidParams(params)
        def game = new Game(params)

        assert game.save() != null

        // test invalid parameters in update
        params.id = game.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/game/edit"
        assert model.gameInstance != null

        game.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/game/show/$game.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        game.clearErrors()

        populateValidParams(params)
        params.id = game.id
        params.version = -1
        controller.update()

        assert view == "/game/edit"
        assert model.gameInstance != null
        assert model.gameInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/game/list'

        response.reset()

        populateValidParams(params)
        def game = new Game(params)

        assert game.save() != null
        assert Game.count() == 1

        params.id = game.id

        controller.delete()

        assert Game.count() == 0
        assert Game.get(game.id) == null
        assert response.redirectedUrl == '/game/list'
    }
}
