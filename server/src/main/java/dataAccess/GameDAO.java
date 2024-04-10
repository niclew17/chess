package dataAccess;

import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

public interface GameDAO {

  CreateGameResponse createGame(CreateGameRequest game) throws DataAccessException;

  ListGamesResponse listGames() throws DataAccessException;

  GameData getGame(int id) throws DataAccessException;

  void updateGame(int gameID, ChessGame game) throws DataAccessException;
  void updateBlackGame(JoinGameRequest request, String username) throws DataAccessException;
  void updateWhiteGame(JoinGameRequest request, String username) throws DataAccessException;

  void deleteAll() throws DataAccessException;
}