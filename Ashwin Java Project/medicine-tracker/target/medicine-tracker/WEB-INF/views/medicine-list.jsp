<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Medicine List - Medicine Tracker</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
    <div class="container mt-4">
        <div class="row mb-4">
            <div class="col">
                <h2>Medicine List</h2>
            </div>
            <div class="col text-end">
                <!-- Add Medicine Button -->
                <a href="${pageContext.request.contextPath}/medicine/add" class="btn btn-primary">
                    Add New Medicine
                </a>
            </div>
        </div>

        <!-- Display any error messages if they exist -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <!-- Medicines Table -->
        <c:choose>
            <c:when test="${empty medicines}">
                <div class="alert alert-info">No medicines found. Add your first medicine using the button above.</div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead class="table-light">
                            <tr>
                                <th>Medicine</th>
                                <th>Dosage</th>
                                <th>Quantity</th>
                                <th>Threshold</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="m" items="${medicines}">
                                <c:set var="lowStock" value="${m.quantity < m.threshold}" />
                                <tr class="${lowStock ? 'table-danger' : ''}">
                                    <td>${m.name}</td>
                                    <td>${m.dosage}</td>
                                    <td>${m.quantity}</td>
                                    <td>${m.threshold}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/medicine/edit?medicineId=${m.id}" 
                                           class="btn btn-sm btn-warning">Edit</a>
                                        <form action="${pageContext.request.contextPath}/medicine/delete" 
                                              method="post" style="display:inline;">
                                            <input type="hidden" name="medicineId" value="${m.id}" />
                                            <button class="btn btn-sm btn-danger" type="submit">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
    