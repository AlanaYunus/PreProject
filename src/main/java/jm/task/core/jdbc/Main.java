package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("David", "Black", (byte) 36);
        userService.saveUser("Sarah", "Red", (byte) 45);
        userService.saveUser("Latif", "Yellow", (byte) 33);
        userService.saveUser("Kevin", "Brown", (byte) 24);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
