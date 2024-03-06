package services;

import dataAccess.*;

public class DB {
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;
  private final UserDAO userDAO;
  public DB(UserDAO userDAO, AuthDAO authDao, GameDAO gameDAO) {
    this.authDAO = authDao;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }

  public void clear() throws DataAccessException {
    authDAO.deleteAll();
    gameDAO.deleteAll();
    userDAO.deleteAll();
  }
}
