package model;

import java.util.Objects;

public class UserData {
  private String username;
  private String password;
  private String email;

  public UserData(String username, String password, String email) {

    this.username=username;
    this.password=password;
    this.email=email;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserData userData=(UserData) o;
    return Objects.equals(username, userData.username) && Objects.equals(password, userData.password) && Objects.equals(email, userData.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email);
  }

  public String getPassword() {
    return password;
  }

}

