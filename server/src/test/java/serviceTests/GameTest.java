package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import services.DB;
import services.Game;
import services.User;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
  MemoryUserDAO userDAO = new MemoryUserDAO();
  MemoryAuthDAO authDAO = new MemoryAuthDAO();
  MemoryGameDAO gameDAO = new MemoryGameDAO();
  DB testDB;
  Game testGame;
  User testUser;

  @BeforeEach
  void setup(){
    testDB = new DB(userDAO, authDAO, gameDAO);
    testGame = new Game(gameDAO, authDAO);
    testUser = new User(userDAO, authDAO);
  }

  @Test
  void listGamesCorrect() throws DataAccessException {
    AuthData auth = testUser.register(new RegisterRequest("don", "lewis", "hello"));
    CreateGameResponse response = testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame"));
    assertEquals(testGame.listGames(auth.getAuthToken()), gameDAO.listGames());

  }
  @Test
  void listGamesError() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("john", "lewis", "hello"));
    CreateGameResponse response = testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame"));
    assertThrows(DataAccessException.class, () -> testGame.listGames("12344"), "Error: unauthorized");
  }


  @Test
  void createGameCorrect() throws DataAccessException {
    AuthData auth = testUser.register(new RegisterRequest("jane", "lewis", "hello"));
    assertEquals(new CreateGameResponse(1), testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame")));
  }
  @Test
  void createGameError() throws DataAccessException {
    AuthData auth = testUser.register(new RegisterRequest("jim", "lewis", "hello"));
    assertThrows(DataAccessException.class, () -> testGame.createGame("1234", new CreateGameRequest("newGame")), "Error: unauthorized");
  }

  @Test
  void joinGameCorrect() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("smith", "lewis", "hello"));
    CreateGameResponse response = testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame"));
    testGame.joinGame(auth.getAuthToken(), new JoinGameRequest("WHITE", response.gameID()));
    GameData data = new GameData(response.gameID(), auth.getUsername(), null, "newGame",new ChessGame());
    assertEquals("smith", data.getWhiteUsername());

  }
  @Test
  void joinGameError() throws DataAccessException{
    AuthData auth = testUser.register(new RegisterRequest("dawn", "lewis", "hello"));
    CreateGameResponse response = testGame.createGame(auth.getAuthToken(), new CreateGameRequest("newGame"));
    assertThrows(DataAccessException.class, () -> testGame.joinGame("1234", new JoinGameRequest("WHITE", response.gameID())), "Error: unauthorized");
  }
}