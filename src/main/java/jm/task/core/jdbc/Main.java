package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

public class Main {
    public static void main(String[] args) {

        UserDao userDao = new UserDaoJDBCImpl();
        userDao.createUsersTable();
        userDao.saveUser("Вася", "Иванов", (byte) 20);
        userDao.saveUser("Петя", "Петров", (byte) 24);
        userDao.saveUser("Коля", "Сидоров", (byte) 25);
        userDao.saveUser("Дима", "Пушкин", (byte) 19);

        //todo: п. Удаление User из таблицы ( по id )
        System.out.println(userDao.getAllUsers());
        userDao.cleanUsersTable();
        userDao.dropUsersTable();
    }
}
