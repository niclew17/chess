package services;

import dataAccess.*;

public class DB {
  private final MemoryAuthDAO authDAO;
  private final MemoryGameDAO gameDAO;
  private final MemoryUserDAO userDAO;
  public DB(MemoryUserDAO userDAO, MemoryAuthDAO authDao, MemoryGameDAO gameDAO) {
    this.authDAO = authDao;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }

  public void clear(){
    authDAO.deleteAll();
    gameDAO.deleteAll();
    userDAO.deleteAll();
  }
}
