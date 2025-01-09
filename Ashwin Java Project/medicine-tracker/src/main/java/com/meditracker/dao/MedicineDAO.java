package com.meditracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.meditracker.model.Medicine;
import com.meditracker.util.DatabaseUtil;

public class MedicineDAO {

    // Create (existing)
    public void addMedicine(Medicine med) throws SQLException {
        String sql = "INSERT INTO medicines (user_id, name, dosage, quantity, threshold) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, med.getUserId());
            stmt.setString(2, med.getName());
            stmt.setString(3, med.getDosage());
            stmt.setInt(4, med.getQuantity());
            stmt.setInt(5, med.getThreshold());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                med.setId(rs.getInt(1));
            }
        }
    }

    // Read (existing)
    public List<Medicine> getMedicinesByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM medicines WHERE user_id = ?";
        List<Medicine> meds = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meds.add(mapRow(rs));
            }
        }
        return meds;
    }

    // Read single medicine (new)
    public Medicine getMedicineById(int medicineId, int userId) throws SQLException {
        String sql = "SELECT * FROM medicines WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicineId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    // Update (new)
    public void updateMedicine(Medicine med) throws SQLException {
        String sql = "UPDATE medicines SET name = ?, dosage = ?, quantity = ?, threshold = ? "
                   + "WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, med.getName());
            stmt.setString(2, med.getDosage());
            stmt.setInt(3, med.getQuantity());
            stmt.setInt(4, med.getThreshold());
            stmt.setInt(5, med.getId());
            stmt.setInt(6, med.getUserId());
            stmt.executeUpdate();
        }
    }

    // Delete (new)
    public void deleteMedicine(int medicineId, int userId) throws SQLException {
        String sql = "DELETE FROM medicines WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicineId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    // Optional helper
    public void updateQuantity(int medicineId, int newQuantity) throws SQLException {
        String sql = "UPDATE medicines SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, medicineId);
            stmt.executeUpdate();
        }
    }

    // Helper method to map DB row to Medicine object
    private Medicine mapRow(ResultSet rs) throws SQLException {
        Medicine m = new Medicine();
        m.setId(rs.getInt("id"));
        m.setUserId(rs.getInt("user_id"));
        m.setName(rs.getString("name"));
        m.setDosage(rs.getString("dosage"));
        m.setQuantity(rs.getInt("quantity"));
        m.setThreshold(rs.getInt("threshold"));
        return m;
    }
}
