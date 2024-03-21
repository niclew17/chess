package server;

import com.google.gson.Gson;
import model.AuthData;
import model.Message;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }


  public void logout(String authtoken) throws Exception {
    this.makeRequestWithHeaders(authtoken, null,"/session", "DELETE", null);
  }
  public AuthData login(LoginRequest login) throws Exception {
    return this.makeRequestNoHeaders(login, "/session","POST", AuthData.class);
  }
  public AuthData register(RegisterRequest register) throws Exception {
    return this.makeRequestNoHeaders(register, "/user", "POST", AuthData.class);
  }
  public CreateGameResponse createGame(CreateGameRequest gameRequest, String authtoken) throws Exception {
    return this.makeRequestWithHeaders(authtoken, gameRequest, "/game", "POST", CreateGameResponse.class);
  }

  public ListGamesResponse listGames(String authtoken) throws Exception {
    return this.makeRequestWithHeaders(authtoken,null,"/game","GET", ListGamesResponse.class);
  }

  public void joinGame(JoinGameRequest request, String authtoken) throws Exception {
    this.makeRequestWithHeaders(authtoken, request, "/game","PUT", null);
  }
  private <T> T makeRequestNoHeaders(Object request, String path, String method, Class<T> rclass) throws Exception {
    URL url = (new URI(serverUrl + path)).toURL();
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method);
    http.setDoOutput(true);
    var body = request;
    try (var outputStream = http.getOutputStream()) {
      var jsonBody = new Gson().toJson(body);
      outputStream.write(jsonBody.getBytes());
    }
    http.connect();
    throwIfNotSuccessful(http);
    try (InputStream respBody = http.getInputStream()) {
      InputStreamReader inputStreamReader = new InputStreamReader(respBody);
      return new Gson().fromJson(inputStreamReader, rclass);
    }
  }

  private <T> T makeRequestWithHeaders(String token, Object request, String path, String method, Class<T> rclass) throws Exception {
    URL url = (new URI(serverUrl + path)).toURL();
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method);
    http.setDoOutput(true);
    http.addRequestProperty("authorization", token);
    var body = request;
    if(request!= null) {
      try (var outputStream=http.getOutputStream()) {
        var jsonBody=new Gson().toJson(body);
        outputStream.write(jsonBody.getBytes());
      }
    }
    http.connect();
    throwIfNotSuccessful(http);
    if(rclass != null) {
      try (InputStream respBody=http.getInputStream()) {
        InputStreamReader inputStreamReader=new InputStreamReader(respBody);
        return new Gson().fromJson(inputStreamReader, rclass);
      }
    }
    else{
      return null;
    }
  }
  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      try (InputStream respBody = http.getErrorStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
        throw new RuntimeException(new Gson().fromJson(inputStreamReader, Message.class).message());
      }
    }
  }
  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }


}