package clientTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import dataAccess.MySQLGameDAO;
import dataAccess.MySQLUserDAO;
import model.AuthData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private MySQLAuthDAO sqlAuthDAO;
    private MySQLUserDAO sqlUserDAO;
    private MySQLGameDAO sqlGameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        sqlAuthDAO = new MySQLAuthDAO();
        sqlAuthDAO.deleteAll();
        sqlUserDAO = new MySQLUserDAO();
        sqlUserDAO.deleteAll();
        sqlGameDAO = new MySQLGameDAO();
        sqlGameDAO.deleteAll();
    }

    @BeforeAll
    public static void init() {

        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void signIn() throws Exception{
        AuthData auth = facade.register(new RegisterRequest("james", "lewis", "123"));
        facade.logout(auth.getAuthToken());
        Assertions.assertEquals("james", facade.login(new LoginRequest("james", "lewis")).getUsername());
    }
    @Test
    void signInFalse() throws Exception{
        assertThrows(Exception.class, () ->  facade.login(new LoginRequest("nic", "lewis")).getUsername());
    }

    @Test
    void logout() throws Exception{
        AuthData auth = facade.register(new RegisterRequest("james", "lewis", "123"));
        Assertions.assertDoesNotThrow(() ->  facade.logout(auth.getAuthToken()));
    }
    @Test
    void logoutFalse() throws Exception{
        AuthData auth = facade.register(new RegisterRequest("james", "lewis", "123"));
        assertThrows(Exception.class, () ->  facade.logout(null));
    }











}
