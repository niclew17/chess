import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }


  public void logout() throws DataAccessException {
    var path = "/session";
    this.makeRequest("DELETE", path, null, null);
  }
  public AuthData login(LoginRequest login) throws DataAccessException {
    var path = "/session";
    return this.makeRequest("POST", path, login, AuthData.class);
  }
  public AuthData register(RegisterRequest register) throws DataAccessException {
    var path = "/user";
    return this.makeRequest("POST", path, register, AuthData.class);
  }
  public CreateGameResponse createGame(CreateGameRequest gameRequest) throws DataAccessException {
    var path = "/game";
    return this.makeRequest("POST", path, gameRequest, CreateGameResponse.class);
  }

  public ListGamesResponse listGames() throws DataAccessException {
    var path = "/game";
    return this.makeRequest("GET", path, null, ListGamesResponse.class);
  }

  public void joinGame(JoinGameRequest request) throws DataAccessException {
    var path = "/game";
    this.makeRequest("PUT", path, request, null);
  }

  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new DataAccessException("failure: " + status);
    }
  }




  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}