package com.ttl.coffeemanagement.data;

import android.os.StrictMode;

import com.ttl.coffeemanagement.staff.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHelper {
    private static final String URL = "*********";
    private static final String USER = "*********";
    private static final String PASSWORD = "*********";

    public static Connection connect() {
        Connection connection = null;
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static ResultSet getStatistics() throws SQLException {
        Connection conn = connect();
        if (conn != null) {
            PreparedStatement stmt = conn.prepareStatement("EXEC GetStatistics");
            return stmt.executeQuery();
        }
        return null;
    }

    public static ResultSet getChartData() throws SQLException {
        Connection conn = connect();
        if (conn != null) {
            PreparedStatement stmt = conn.prepareStatement("EXEC GetChartData");
            return stmt.executeQuery();
        }
        return null;
    }

    public static ArrayList<Employee> getEmployees() {
        ArrayList<Employee> list = new ArrayList<>();
        try (Connection conn = connect()) {
            PreparedStatement stmt = conn.prepareStatement("EXEC GetEmployees");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Employee(rs.getInt("Id"), rs.getString("Username"), rs.getString("PhoneNumber"), rs.getString("Name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertEmployee(String username, String password, String phone, String name) {
        try (Connection conn = connect()) {
            PreparedStatement stmt = conn.prepareStatement("EXEC InsertEmployee ?, ?, ?, ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, phone);
            stmt.setString(4, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteEmployee(int id) {
        try (Connection conn = connect()) {
            PreparedStatement stmt = conn.prepareStatement("EXEC DeleteEmployee ?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
