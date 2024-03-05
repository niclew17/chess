package services;

import dataAccess.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

public class Game {
  private final MemoryGameDAO gameDAO;
  private final MemoryAuthDAO authDAO;
  public Game(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;

  }
  public void isAuthorized(String authtoken) throws DataAccessException {
    if(authDAO.getUser(authtoken) == null){
      throw new DataAccessException("Error: unauthorized");
    }
  }
  public ListGamesResponse listGames(String auth) throws DataAccessException{
    isAuthorized(auth);
    return gameDAO.listGames();
  }
  public CreateGameResponse createGame(String auth, CreateGameRequest game) throws DataAccessException{
    isAuthorized(auth);
    return gameDAO.createGame(game);
  }
  public void joinGame(String auth, JoinGameRequest game) throws DataAccessException{
    isAuthorized(auth);
    if(gameDAO.getGame(game.gameID()) == null){
      throw new DataAccessException("Error: bad request");
    }
    else if(game.playerColor()==null){
    }
    else if(gameDAO.getGame(game.gameID()).getBlackUsername() == null && game.playerColor().equals("BLACK")){
      String username = authDAO.getUser(auth).getUsername();
      gameDAO.updateBlackGame(game, username);
    }
    else if(gameDAO.getGame(game.gameID()).getWhiteUsername() == null && game.playerColor().equals("WHITE")){
      String username = authDAO.getUser(auth).getUsername();
      gameDAO.updateWhiteGame(game, username);
    }
    else{
      throw new DataAccessException("Error: already taken");
    }
    }
  }

