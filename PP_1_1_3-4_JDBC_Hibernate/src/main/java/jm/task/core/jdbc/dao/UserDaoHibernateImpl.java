package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                // Создаем таблицу через SQL, так как Hibernate может не создать её автоматически в тестах
                session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL, " +
                        "last_name VARCHAR(50) NOT NULL, " +
                        "age TINYINT NOT NULL)").executeUpdate();
                transaction.commit();
                System.out.println("Таблица users создана успешно");
            } catch (Exception e) {
                transaction.rollback();
                System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
                transaction.commit();
                System.out.println("Таблица users удалена успешно");
            } catch (Exception e) {
                transaction.rollback();
                System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
                System.out.println("Пользователь " + name + " добавлен в базу данных");
            } catch (Exception e) {
                transaction.rollback();
                System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                    transaction.commit();
                    System.out.println("Пользователь с id " + id + " удален");
                } else {
                    transaction.commit();
                    System.out.println("Пользователь с id " + id + " не найден");
                }
            } catch (Exception e) {
                transaction.rollback();
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM User").executeUpdate();
                transaction.commit();
                System.out.println("Таблица users очищена");
            } catch (Exception e) {
                transaction.rollback();
                System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            }
        }
    }
}
