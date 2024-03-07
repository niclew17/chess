package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import dataAccess.MySQLUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

class MySQLUserDAOTest {
  private MySQLUserDAO sqlUserDAO;
  @BeforeEach
  public void setup() throws DataAccessException {
    sqlUserDAO = new MySQLUserDAO();
    sqlUserDAO.deleteAll();
  }

  @Test
  void createUser() throws DataAccessException {
    UserData data = sqlUserDAO.createUser(new RegisterRequest("1", "2","3"));
    assertNotNull(data);
    assertEquals("1", data.getUsername());
    assertEquals(data, sqlUserDAO.getUser(data.getUsername()));
  }

  @Test
  void getUser() throws DataAccessException {
    UserData user = sqlUserDAO.createUser(new RegisterRequest("1", "2","3"));
    assertEquals(user, sqlUserDAO.getUser(user.getUsername()));
  }

  @Test
  void deleteAll() throws DataAccessException {
    UserData user = sqlUserDAO.createUser(new RegisterRequest("1", "2","3"));
    UserData user1 = sqlUserDAO.createUser(new RegisterRequest("4", "5","6"));
    sqlUserDAO.deleteAll();
    assertNull(sqlUserDAO.getUser(user.getUsername()));
    assertNull(sqlUserDAO.getUser(user1.getUsername()));
  }

  @Test
  void createUserNeg() {
    assertThrows(NullPointerException.class, () -> sqlUserDAO.createUser(null));
  }

  @Test
  void getUserNeg() throws DataAccessException {

  }

}