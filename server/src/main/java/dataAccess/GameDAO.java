package dataAccess;

import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;

import java.util.Collection;

public interface GameDAO {

  CreateGameResponse createGame(CreateGameRequest game) throws DataAccessException;

  Collection<GameData> listGames() throws DataAccessException;

  GameData getGame(int id) throws DataAccessException;

  void updateBlackGame(JoinGameRequest request, String username) throws DataAccessException;
  void updateWhiteGame(JoinGameRequest request, String username) throws DataAccessException;

  void deleteAll() throws DataAccessException;
}