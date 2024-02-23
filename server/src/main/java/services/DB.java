package services;

import dataAccess.*;

public class DB {
  private final MemoryAuthDAO authDAO;
  private final MemoryGameDAO gameDAO;
  private final MemoryUserDAO userDAO;
  public DB() {
    authDAO = new MemoryAuthDAO();
    gameDAO = new MemoryGameDAO();
    userDAO = new MemoryUserDAO();
  }

  public void clear(){
    authDAO.deleteAll();
    gameDAO.deleteAll();
    userDAO.deleteAll();
  }
}
