package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import dataAccess.MySQLGameDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;

import static org.junit.jupiter.api.Assertions.*;

class MySQLGameDAOTest {
  private MySQLGameDAO sqlGameDAO;
  private MySQLAuthDAO sqlAuthDAO;


  @BeforeEach
  public void setup() throws DataAccessException {
    sqlGameDAO = new MySQLGameDAO();
    sqlGameDAO.deleteAll();
    sqlAuthDAO = new MySQLAuthDAO();
    sqlAuthDAO.deleteAll();
  }


  @Test
  void createGame() throws DataAccessException {
    sqlGameDAO.createGame(new CreateGameRequest("game"));
    assertNotNull(sqlGameDAO.listGames());
  }

  @Test
  void listGames() throws DataAccessException {
    sqlGameDAO.createGame(new CreateGameRequest("lame"));
    assertNotNull(sqlGameDAO.listGames());
  }

  @Test
  void getGame() throws DataAccessException {
    CreateGameResponse response = sqlGameDAO.createGame(new CreateGameRequest("game"));
    assertNotNull(sqlGameDAO.getGame(response.gameID()));
  }

  @Test
  void updateBlackGame() throws DataAccessException {
    CreateGameResponse response= sqlGameDAO.createGame(new CreateGameRequest("fame"));
    sqlGameDAO.updateBlackGame(new JoinGameRequest("BLACK",response.gameID()), "123");
    assertEquals("123",sqlGameDAO.getGame(response.gameID()).getBlackUsername());
  }

  @Test
  void updateWhiteGame() throws DataAccessException {
    CreateGameResponse response= sqlGameDAO.createGame(new CreateGameRequest("same"));
    sqlGameDAO.updateWhiteGame(new JoinGameRequest("WHITE",response.gameID()), "123");
    assertEquals("123",sqlGameDAO.getGame(response.gameID()).getWhiteUsername());
  }

  @Test
  void deleteAll() throws DataAccessException {
    CreateGameResponse response= sqlGameDAO.createGame(new CreateGameRequest("same"));
    CreateGameResponse response1= sqlGameDAO.createGame(new CreateGameRequest("same"));
    sqlGameDAO.deleteAll();
    assertNull(sqlGameDAO.getGame(response.gameID()));
    assertNull(sqlGameDAO.getGame(response1.gameID()));
  }

  @Test
  void createGameNeg() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> sqlGameDAO.createGame(new CreateGameRequest(null)));
  }

  @Test
  void listGamesNeg() throws DataAccessException {
    assertNotNull(sqlGameDAO.listGames());
  }

  @Test
  void getGameNeg() throws DataAccessException {
    sqlGameDAO.createGame(new CreateGameRequest("game"));
    assertNull(sqlGameDAO.getGame(456));

  }

  @Test
  void updateBlackGameNeg() throws DataAccessException {;
    assertThrows(DataAccessException.class, () ->sqlGameDAO.updateBlackGame(null, "Boy"));
  }

  @Test
  void updateWhiteGameNeg() {
    assertThrows(DataAccessException.class, () ->sqlGameDAO.updateWhiteGame(null, "Boy"));
  }

}