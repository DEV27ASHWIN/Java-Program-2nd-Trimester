package com.meditracker.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.meditracker.dao.MedicineDAO;
import com.meditracker.dao.ScheduleDAO;
import com.meditracker.model.Medicine;
import com.meditracker.model.Schedule;

@WebServlet("/medicine/*")
public class MedicineServlet extends HttpServlet {

    private MedicineDAO medicineDAO = new MedicineDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            // Show the main medicine list
            showMedicineList(req, resp, userId);
        } else if ("/edit".equals(path)) {
            // Load a single medicine for editing
            handleEditForm(req, resp, userId);
        } else if ("/add".equals(path)) {
            // Show the add medicine form
            req.getRequestDispatcher("/WEB-INF/views/add-medicine.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showMedicineList(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        try {
            List<Medicine> meds = medicineDAO.getMedicinesByUser(userId);
            List<Schedule> schedules = scheduleDAO.getSchedulesByUser(userId);

            req.setAttribute("medicines", meds);
            req.setAttribute("schedules", schedules);

            req.getRequestDispatcher("/WEB-INF/views/medicine-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleEditForm(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        String medIdParam = req.getParameter("medicineId");
        
        // Add validation for medicineId parameter
        if (medIdParam == null || medIdParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing medicineId");
            return;
        }

        try {
            int medId = Integer.parseInt(medIdParam);
            Medicine med = medicineDAO.getMedicineById(medId, userId);
            if (med == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Medicine not found or belongs to another user");
                return;
            }
            req.setAttribute("medicine", med);
            req.getRequestDispatcher("/WEB-INF/views/edit-medicine.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid medicineId format");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        String path = req.getPathInfo();
        if ("/add".equals(path)) {
            handleAddMedicine(req, resp, userId);
        } else if ("/schedule".equals(path)) {
            handleCreateSchedule(req, resp, userId);
        } else if ("/markTaken".equals(path)) {
            handleMarkTaken(req, resp);
        } else if ("/update".equals(path)) {
            handleUpdateMedicine(req, resp, userId);
        } else if ("/delete".equals(path)) {
            handleDeleteMedicine(req, resp, userId);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAddMedicine(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String dosage = req.getParameter("dosage");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        int threshold = Integer.parseInt(req.getParameter("threshold"));

        Medicine med = new Medicine(userId, name, dosage, quantity, threshold);

        try {
            medicineDAO.addMedicine(med);
            resp.sendRedirect(req.getContextPath() + "/medicine");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleUpdateMedicine(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        int medId = Integer.parseInt(req.getParameter("medicineId"));
        String name = req.getParameter("name");
        String dosage = req.getParameter("dosage");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        int threshold = Integer.parseInt(req.getParameter("threshold"));

        Medicine med = new Medicine();
        med.setId(medId);
        med.setUserId(userId);
        med.setName(name);
        med.setDosage(dosage);
        med.setQuantity(quantity);
        med.setThreshold(threshold);

        try {
            medicineDAO.updateMedicine(med);
            resp.sendRedirect(req.getContextPath() + "/medicine");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleDeleteMedicine(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        int medId = Integer.parseInt(req.getParameter("medicineId"));
        try {
            medicineDAO.deleteMedicine(medId, userId);
            resp.sendRedirect(req.getContextPath() + "/medicine");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleCreateSchedule(HttpServletRequest req, HttpServletResponse resp, int userId)
            throws ServletException, IOException {
        int medicineId = Integer.parseInt(req.getParameter("medicineId"));
        String timeOfDay = req.getParameter("timeOfDay");
        String mealTiming = req.getParameter("mealTiming");

        Schedule schedule = new Schedule(
            userId,
            medicineId,
            Schedule.TimeOfDay.valueOf(timeOfDay),
            Schedule.MealTiming.valueOf(mealTiming)
        );

        try {
            scheduleDAO.createSchedule(schedule);
            resp.sendRedirect(req.getContextPath() + "/medicine");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleMarkTaken(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
        try {
            scheduleDAO.markTaken(scheduleId);
            resp.sendRedirect(req.getContextPath() + "/medicine");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
