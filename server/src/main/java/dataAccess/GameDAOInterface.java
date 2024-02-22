package dataAccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;

public interface GameDAOInterface {

  GameData add(GameData game);

  HashMap<Integer, GameData> list();

  GameData get(int id);

  void delete(int id);

  void deleteAll();
}