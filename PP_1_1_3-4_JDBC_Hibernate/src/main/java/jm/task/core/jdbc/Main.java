package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        // Создаем таблицу
        userService.createUsersTable();

        // Добавляем пользователей
        userService.saveUser("Иван", "Иванов", (byte) 25);
        userService.saveUser("Петр", "Петров", (byte) 30);
        userService.saveUser("Мария", "Сидорова", (byte) 28);
        userService.saveUser("Анна", "Козлова", (byte) 35);

        // Получаем всех пользователей
        List<User> users = userService.getAllUsers();
        System.out.println("\nВсе пользователи:");
        for (User user : users) {
            System.out.println(user.getName() + " " + user.getLastName() + " (возраст: " + user.getAge() + ")");
        }

        // Удаляем пользователя по ID
        userService.removeUserById(2L);

        // Очищаем таблицу
        userService.cleanUsersTable();

        // Удаляем таблицу
        userService.dropUsersTable();
    }
}
