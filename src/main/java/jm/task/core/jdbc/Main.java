package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.util.Util;

public class Main {

    public static void main(String[] args) {

        UserDao userDao = new UserDaoHibernateImpl(new Util());
        //UserDao userDao = new UserDaoJDBCImpl(new Util());
        userDao.createUsersTable();
        userDao.saveUser("Вася", "Иванов", (byte) 20);
        userDao.saveUser("Петя", "Петров", (byte) 24);
        userDao.saveUser("Коля", "Сидоров", (byte) 25);
        userDao.saveUser("Дима", "Пушкин", (byte) 19);
        userDao.removeUserById(4);
        System.out.println(userDao.getAllUsers());
        userDao.cleanUsersTable();
        userDao.dropUsersTable();
    }
}
