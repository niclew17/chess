package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private int nextId=1;
  final private HashMap<Integer, GameData> games=new HashMap<>();

  public GameData createGame(GameData game) {
    game=new GameData(nextId++, game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame());

    games.put(game.getGameID(), game);
    return game;
  }

  public Collection<GameData> listGames() {
    return games.values();
  }

  public GameData getGame(int id) {
    return games.get(id);
  }

  public void updateGame(int id, String name) {
    games.get(id).setGameName(name);
  }

  public void deleteAll() {
    games.clear();
  }
}
