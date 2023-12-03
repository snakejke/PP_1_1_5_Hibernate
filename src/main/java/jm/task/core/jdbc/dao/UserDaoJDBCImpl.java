package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS %s";
    private static final String INSERT_QUERY = "INSERT INTO USERTABLE(name, lastname, age) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM USERTABLE WHERE id = ?";
    private static final String SELECT_QUERY = "SELECT * FROM USERTABLE";
    private static final String TRUNCATE_QUERY = "TRUNCATE TABLE USERTABLE";

    private final Connection connection;

    private final String createUsersQuery =
            "CREATE TABLE IF NOT EXISTS USERTABLE" +
            "(id BIGINT not NULL AUTO_INCREMENT, " +
            " name VARCHAR(255), " +
            " lastname VARCHAR(255), " +
            " age TINYINT, " +
            " PRIMARY KEY ( id ))";

    public UserDaoJDBCImpl(Util util) {
        this.connection = util.getConnection();
    }

    @Override
    public void createUsersTable() {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(createUsersQuery)) {
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException sqlEx) {
                connection.rollback();
                throw new RuntimeException("Error with createUsersTable: " + sqlEx.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException connEx) {
            throw new RuntimeException("Error with createUsersTable: " + connEx.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement dropTable = connection.prepareStatement(
                    String.format(DROP_TABLE_QUERY, "USERTABLE"))) {
                dropTable.executeUpdate();
            } catch (SQLException ex) {
                connection.rollback();
                throw new RuntimeException("Error dropping users table:  " + ex.getMessage());
            }

            connection.commit();
        } catch (SQLException ex) {
            throw new RuntimeException("Error dropping users table: " + ex.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException("Error dropping users table: " + ex.getMessage());
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
                ps.setString(1, name);
                ps.setString(2, lastName);
                ps.setInt(3, age);
                ps.executeUpdate();
                System.out.printf("User с именем %s добавлен в таблицу\n", name);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error with saveUser: " + e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error with saveUser: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error with saveUser: " + e.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
                ps.setLong(1, id);
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException sqlEx) {
                connection.rollback();
                throw new RuntimeException("Error removing user with ID: " + id, sqlEx);
            }
        } catch (SQLException connEx) {
            throw new RuntimeException("Error managing transaction: " + connEx.getMessage(), connEx);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                throw new RuntimeException("Error resetting auto-commit: " + autoCommitEx.getMessage(), autoCommitEx);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> ls = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("lastname"));
                    user.setAge(rs.getByte("age"));
                    ls.add(user);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error retrieving all users: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error managing transaction: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException("Error resetting auto-commit: " + ex.getMessage(), ex);
            }
        }
        return ls;
    }

    @Override
    public void cleanUsersTable() {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(TRUNCATE_QUERY)) {
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error cleaning users table: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error managing transaction: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException("Error resetting auto-commit: " + ex.getMessage(), ex);
            }
        }
    }
}
