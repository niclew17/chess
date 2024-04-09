package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import services.DB;
import services.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  MemoryUserDAO userDAO = new MemoryUserDAO();
  MemoryAuthDAO authDAO = new MemoryAuthDAO();
  MemoryGameDAO gameDAO = new MemoryGameDAO();
  DB testDB;
  User testUser;

  @BeforeEach
  void setup(){
    testDB = new DB(userDAO, authDAO, gameDAO);
    testUser = new User(userDAO, authDAO);
  }


  @Test
  void registerCorrect() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("jeff", "lewis", "hello"));
    assertEquals("jeff", userDAO.getUser(auth.getUsername()).getUsername());
  }
  @Test
  void registerError() throws DataAccessException{
    testUser.register(new RegisterRequest(null, "lewis", "hello"));
    assertThrows(DataAccessException.class, () -> testUser.register(new RegisterRequest(null, "lewis", "hello")), "Error: already taken");
  }

  @Test
  void loginCorrect() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("stam", "lewis", "hello"));
    testUser.logout(auth.getAuthToken());
    assertThrows(DataAccessException.class, () -> testUser.login(new LoginRequest("stam", "lewis")).getUsername());
  }
  @Test
  void loginError() throws DataAccessException{
    assertThrows(DataAccessException.class, () -> testUser.login(new LoginRequest(null, "lewis")), "Error: unauthorized");
  }

  @Test
  void logoutCorrect() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("jen", "lewis", "hello"));
    testUser.logout(auth.getAuthToken());
    assertEquals(null, authDAO.getUser(auth.getAuthToken()));
  }
  @Test
  void logoutError() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("maddie", "lewis", "hello"));
    assertThrows(DataAccessException.class, () -> testUser.logout("1234"), "Error: unauthorized");
  }
}