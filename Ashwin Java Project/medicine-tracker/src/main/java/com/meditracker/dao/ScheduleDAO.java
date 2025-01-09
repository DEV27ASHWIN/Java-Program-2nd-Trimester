package com.meditracker.dao;

import com.meditracker.model.Schedule;
import com.meditracker.model.Schedule.MealTiming;
import com.meditracker.model.Schedule.TimeOfDay;
import com.meditracker.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public void createSchedule(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO schedules (user_id, medicine_id, time_of_day, meal_timing) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getUserId());
            stmt.setInt(2, schedule.getMedicineId());
            stmt.setString(3, schedule.getTimeOfDay().name());
            stmt.setString(4, schedule.getMealTiming().name());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                schedule.setId(rs.getInt(1));
            }
        }
    }

    public List<Schedule> getSchedulesByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE user_id = ?";
        List<Schedule> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void markTaken(int scheduleId) throws SQLException {
        String sql = "UPDATE schedules SET is_taken = 1 WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();
        }
    }

    private Schedule mapRow(ResultSet rs) throws SQLException {
        Schedule s = new Schedule();
        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        s.setMedicineId(rs.getInt("medicine_id"));
        s.setTimeOfDay(TimeOfDay.valueOf(rs.getString("time_of_day")));
        s.setMealTiming(MealTiming.valueOf(rs.getString("meal_timing")));
        s.setTaken(rs.getBoolean("is_taken"));
        return s;
    }
}
