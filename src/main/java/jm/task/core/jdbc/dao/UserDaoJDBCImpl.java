package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final Connection connection = Util.getConnection();//todo: ..на проекте подобное именование 'con' будет вызывать удивление у коллег. Привыкаем к нормальной работе сейчас

    private final String createUsersQuery = "CREATE TABLE IF NOT EXISTS user" +
            "(id BIGINT not NULL AUTO_INCREMENT, " +
            " name VARCHAR(255), " +
            " lastname VARCHAR(255), " +
            " age TINYINT, " +
            " PRIMARY KEY ( id ))";//todo: выносим переменные из методов, именуем их по codeStyle (по смыслу)

    public UserDaoJDBCImpl() {//todo: заносим переменную через конструктор

    }

    //todo: потеряли транзакции.. однотипрая - над классом, с параметром (readOnly) - над нужными методами

    @Override
    public void createUsersTable() {
        try (PreparedStatement ps = connection.prepareStatement(createUsersQuery)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error with createUsersTable: " + ex.getMessage());//todo: примерно так..
        }

    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement dropTable = connection.prepareStatement(
                String.format("DROP TABLE IF EXISTS %s", "user"))) {//todo: здесь и далее - по примеру выше
            dropTable.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO user(name, lastname, age) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {// (query,Statement.RETURN_GENERATED_KEYS) ?)
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setInt(3, age);
            ps.executeUpdate();
            System.out.printf("User с именем %s добавлен в таблицу\n", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeUserById(long id) {
        String query = "DELETE FROM user WHERE id =?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM user";
        List<User> ls = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getByte("age"));
                ls.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ls;
    }

    @Override
    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE user";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
