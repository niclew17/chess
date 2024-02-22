package dataAccess;

import model.GameData;

import java.util.HashMap;

public class GameDAO implements GameDAOInterface{
  private int nextId=1;
  final private HashMap<Integer, GameData> games=new HashMap<>();

  public GameData add(GameData game) {
    game=new GameData(nextId++, game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame());

    games.put(game.getGameID(), game);
    return game;
  }

  public HashMap<Integer, GameData> list() {
    return games;
  }

  public GameData get(int id) {
    return games.get(id);
  }

  public void delete(int id) {
    games.remove(id);
  }

  public void deleteAll() {
    games.clear();
  }
}
