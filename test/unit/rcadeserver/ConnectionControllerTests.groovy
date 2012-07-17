package rcadeserver



import org.junit.*
import grails.test.mixin.*

@TestFor(ConnectionController)
@Mock(Connection)
class ConnectionControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/connections/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.connectionsInstanceList.size() == 0
        assert model.connectionsInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.connectionsInstance != null
    }

    void testSave() {
        controller.save()

        assert model.connectionsInstance != null
        assert view == '/connections/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/connections/show/1'
        assert controller.flash.message != null
        assert Connection.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/connections/list'


        populateValidParams(params)
        def connections = new Connection(params)

        assert connections.save() != null

        params.id = connections.id

        def model = controller.show()

        assert model.connectionsInstance == connections
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/connections/list'


        populateValidParams(params)
        def connections = new Connection(params)

        assert connections.save() != null

        params.id = connections.id

        def model = controller.edit()

        assert model.connectionsInstance == connections
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/connections/list'

        response.reset()


        populateValidParams(params)
        def connections = new Connection(params)

        assert connections.save() != null

        // test invalid parameters in update
        params.id = connections.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/connections/edit"
        assert model.connectionsInstance != null

        connections.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/connections/show/$connections.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        connections.clearErrors()

        populateValidParams(params)
        params.id = connections.id
        params.version = -1
        controller.update()

        assert view == "/connections/edit"
        assert model.connectionsInstance != null
        assert model.connectionsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/connections/list'

        response.reset()

        populateValidParams(params)
        def connections = new Connection(params)

        assert connections.save() != null
        assert Connection.count() == 1

        params.id = connections.id

        controller.delete()

        assert Connection.count() == 0
        assert Connection.get(connections.id) == null
        assert response.redirectedUrl == '/connections/list'
    }
}
