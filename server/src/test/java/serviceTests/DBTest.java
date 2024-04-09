package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import services.DB;
import services.Game;
import services.User;

import static org.junit.jupiter.api.Assertions.*;

class DBTest {
  MemoryUserDAO userDAO = new MemoryUserDAO();
  MemoryAuthDAO authDAO = new MemoryAuthDAO();
  MemoryGameDAO gameDAO = new MemoryGameDAO();
  DB testDB = new DB(userDAO, authDAO, gameDAO);
  Game testGame = new Game(gameDAO, authDAO);
  User testUser = new User(userDAO, authDAO);


  @Test
  public void testClearAll() throws DataAccessException {
    AuthData auth = testUser.register(new RegisterRequest("will", "lewis", "hello"));
    testDB.clear();
    assertEquals(null, authDAO.getUser(auth.getAuthToken()));

  }

}