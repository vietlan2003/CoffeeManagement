package com.ttl.coffeemanagement.login;

import com.ttl.coffeemanagement.data.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginManager {
    public static boolean login(String username, String password) {
        Connection connection = DatabaseHelper.connect();
        if (connection != null) {
            try {
                String query = "SELECT COUNT(*) FROM Users WHERE Username = ? AND Password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Trả về true nếu tài khoản tồn tại
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
