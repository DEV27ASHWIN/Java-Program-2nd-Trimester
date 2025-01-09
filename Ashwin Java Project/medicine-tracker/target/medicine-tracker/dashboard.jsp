<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Medicine Tracker</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2>Dashboard</h2>
    <p>
        <a href="<%= request.getContextPath() %>/auth/logout" class="btn btn-secondary">Logout</a>
        <a href="<%= request.getContextPath() %>/medicine" class="btn btn-primary">View Medicines</a>
    </p>
    <hr>
    <p>Welcome to the Medicine Tracker! Choose an action above.</p>
</div>
</body>
</html>
