package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.RegisterRequest;
import services.DB;
import services.Game;
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
  void registerCorrect() {

  }
  @Test
  void registerError() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("jim", "lewis", "hello"));
    testUser.register(new RegisterRequest("jim", "lewis", "hello"));
    assertThrows(DataAccessException.class, () -> testUser.register(new RegisterRequest("jim", "lewis", "hello")), "Error: already taken");
  }

  @Test
  void loginCorrect() {

  }
  @Test
  void loginError() {

  }

  @Test
  void logoutCorrect() {

  }
  @Test
  void logoutError() {

  }
}