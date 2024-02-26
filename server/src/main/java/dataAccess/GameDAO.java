package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

  GameData createGame(GameData game) throws DataAccessException;

  Collection<GameData> listGames() throws DataAccessException;

  GameData getGame(int id) throws DataAccessException;

  void updateGame(int id, String name) throws DataAccessException;

  void deleteAll() throws DataAccessException;
}