package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDAOTest {

  private MySQLAuthDAO sqlAuthDAO;
  @BeforeEach
  public void setup() throws DataAccessException {
    sqlAuthDAO = new MySQLAuthDAO();
    sqlAuthDAO.deleteAll();
  }


  @Test
  void createAuth() throws DataAccessException {
    AuthData user = sqlAuthDAO.createAuth("nic");
    assertNotNull(user);
    assertEquals("nic", user.getUsername());
    assertEquals(user, sqlAuthDAO.getUser(user.getAuthToken()));
  }

  @Test
  void getUser() throws DataAccessException {
    AuthData user = sqlAuthDAO.createAuth("james");
    assertEquals(user, sqlAuthDAO.getUser(user.getAuthToken()));
  }

  @Test
  void deleteAuth() throws DataAccessException {
    AuthData user = sqlAuthDAO.createAuth("jeff");
    sqlAuthDAO.deleteAuth(user.getAuthToken());
    assertNull(sqlAuthDAO.getUser(user.getAuthToken()));
  }

  @Test
  void deleteAll() throws DataAccessException {
    AuthData user = sqlAuthDAO.createAuth("steve");
    AuthData user2 = sqlAuthDAO.createAuth("don");
    sqlAuthDAO.deleteAll();
    assertNull(sqlAuthDAO.getUser(user2.getAuthToken()));
    assertNull(sqlAuthDAO.getUser(user.getAuthToken()));

  }

  @Test
  void createAuthNeg() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> sqlAuthDAO.createAuth(null));
  }

  @Test
  void getUserNeg() throws DataAccessException {
    assertNull(sqlAuthDAO.getUser(null));
  }

  @Test
  void deleteAuthNeg() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> sqlAuthDAO.createAuth(null));
  }

}