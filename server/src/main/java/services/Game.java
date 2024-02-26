package services;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class Game {
  private final MemoryUserDAO userDAO;
  private final MemoryAuthDAO authDAO;
  public User() {
    userDAO = new MemoryUserDAO();
    authDAO = new MemoryAuthDAO();
  }
  public Collection<GameData> listGames(){

  }
  public GameData createGame(AuthData data){
    return null;
  }
  public void joinGame(AuthData data){

  }
}
