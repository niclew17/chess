package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import server.Server;

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
  void getUser() {
  }

  @Test
  void deleteAuth() {
  }

  @Test
  void deleteAll() {
  }

  @Test
  void createAuthNeg() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> sqlAuthDAO.createAuth(null));
  }

  @Test
  void getUserNeg() {
  }

  @Test
  void deleteAuthNeg() {
  }

  @Test
  void deleteAllNeg() {
  }
}