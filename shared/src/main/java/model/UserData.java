package model;

public class UserData {
  private int id;
  private String username;
  private String password;
  private String email;

  public UserData(int id, String username, String password, String email) {
    this.id = id;
    this.username=username;
    this.password=password;
    this.email=email;
  }

  public String getUsername() {
    return username;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id=id;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password=password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email=email;
  }
}

