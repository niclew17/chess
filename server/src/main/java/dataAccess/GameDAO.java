package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

  GameData createGame(GameData game);

  Collection<GameData> listGames();

  GameData getGame(int id);

  void updateGame(int id, String name);

  void deleteAll();
}