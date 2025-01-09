<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.meditracker.model.Medicine" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Medicine</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h2 class="card-title">Edit Medicine</h2>
                    </div>
                    <div class="card-body">
                        <%
                            Medicine medicine = (Medicine) request.getAttribute("medicine");
                            if (medicine == null) {
                        %>
                            <div class="alert alert-danger">Medicine not found.</div>
                        <%
                            } else {
                        %>
                        <form action="<%= request.getContextPath() %>/medicine/update" method="post">
                            <input type="hidden" name="medicineId" value="<%= medicine.getId() %>" />

                            <div class="mb-3">
                                <label class="form-label">Name</label>
                                <input type="text" name="name" value="<%= medicine.getName() %>" 
                                       required class="form-control" />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Dosage</label>
                                <input type="text" name="dosage" value="<%= medicine.getDosage() %>" 
                                       class="form-control" />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Quantity</label>
                                <input type="number" name="quantity" value="<%= medicine.getQuantity() %>" 
                                       min="0" class="form-control" />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Threshold</label>
                                <input type="number" name="threshold" value="<%= medicine.getThreshold() %>" 
                                       min="0" class="form-control" />
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="<%= request.getContextPath() %>/medicine" 
                                   class="btn btn-secondary me-md-2">Cancel</a>
                                <button type="submit" class="btn btn-primary">Update</button>
                            </div>
                        </form>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS (optional, but needed for some Bootstrap features) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
