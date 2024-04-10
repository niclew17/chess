package dataAccess;

import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private int nextId=0;
  final private HashMap<Integer, GameData> games=new HashMap<>();
  final private HashMap<Integer, GameData> showgames=new HashMap<>();

  public CreateGameResponse createGame(CreateGameRequest game) {
    GameData newgame=new GameData(++nextId, null, null, game.gameName(), new ChessGame());
    games.put(newgame.getGameID(), newgame);
    GameData showgame=new GameData(nextId, null, null, game.gameName(), null);
    showgames.put(showgame.getGameID(), showgame);
    return new CreateGameResponse(newgame.getGameID());
  }

  public ListGamesResponse listGames() {
    return new ListGamesResponse(showgames.values());
  }

  public GameData getGame(int id) {
    return games.get(id);
  }
  public void updateGame(int gameID, ChessGame game){}

  public void updateBlackGame(JoinGameRequest game, String username) {
    games.get(game.gameID()).setBlackUsername(username);
    showgames.get(game.gameID()).setBlackUsername(username);
  }
  public void updateWhiteGame(JoinGameRequest game, String username) {
    games.get(game.gameID()).setWhiteUsername(username);
    showgames.get(game.gameID()).setWhiteUsername(username);
  }

  public void deleteAll() {
    games.clear();
    showgames.clear();
  }
}
