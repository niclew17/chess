package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import services.DB;
import services.Game;
import services.User;

import java.util.Collection;
import java.util.HashMap;

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
    Collection<GameData> games = null;
    AuthData auth = testUser.register(new RegisterRequest("nic", "lewis", "hello"));
   // CreateGameResponse response = testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame"));
    testDB.clear();
    assertEquals(null, authDAO.getUser(auth.getAuthToken()));

  }

}