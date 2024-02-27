package dataAccess;

import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private int nextId=1;
  final private static HashMap<Integer, GameData> games=new HashMap<>();

  public CreateGameResponse createGame(CreateGameRequest game) {
    GameData newgame=new GameData(nextId++, null, null, game.gamename(), new ChessGame());

    games.put(newgame.getGameID(), newgame);
    return new CreateGameResponse(newgame.getGameID());
  }

  public Collection<GameData> listGames() {
    return games.values();
  }

  public GameData getGame(int id) {
    return games.get(id);
  }

  public void updateBlackGame(JoinGameRequest game, String username) {
    games.get(game.gameID()).setBlackUsername(username);
  }
  public void updateWhiteGame(JoinGameRequest game, String username) {
    games.get(game.gameID()).setWhiteUsername(username);
  }

  public void deleteAll() {
    games.clear();
  }
}
