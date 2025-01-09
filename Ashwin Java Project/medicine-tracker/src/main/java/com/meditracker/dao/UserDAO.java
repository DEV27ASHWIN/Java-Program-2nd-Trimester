package com.meditracker.dao;

import com.meditracker.model.User;
import com.meditracker.util.DatabaseUtil;
import com.meditracker.util.SecurityUtil;

import java.sql.*;

public class UserDAO {

    public boolean userExists(String username, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, salt) VALUES (?, ?, ?, ?)";
        String salt = SecurityUtil.generateSalt();
        String hashed = SecurityUtil.hashPassword(user.getPassword(), salt);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashed);
            stmt.setString(4, salt);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            user.setSalt(salt);
            user.setPassword(hashed);
        }
    }

    public User authenticate(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPass = rs.getString("password");
                String dbSalt = rs.getString("salt");
                String hashedInput = SecurityUtil.hashPassword(plainPassword, dbSalt);

                if (dbPass.equals(hashedInput)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(dbPass);
                    user.setSalt(dbSalt);
                    return user;
                }
            }
        }
        return null;
    }
}
