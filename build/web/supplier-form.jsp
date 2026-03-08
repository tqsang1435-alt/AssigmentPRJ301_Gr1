<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Supplier Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="container mt-4">

<h2>
    <c:choose>
        <c:when test="${supplier != null}">
            Update Supplier
        </c:when>
        <c:otherwise>
            Add Supplier
        </c:otherwise>
    </c:choose>
</h2>

<form action="supplier" method="post" class="mt-3">

    <!-- show validation error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Action -->
    <c:choose>
        <c:when test='${supplier != null}'>
            <input type="hidden" name="action" value="update"/>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="action" value="add"/>
        </c:otherwise>
    </c:choose>

    <!-- ID (chỉ khi update) -->
    <c:if test="${supplier != null}">
        <input type="hidden" name="id" value="${supplier.supplierID}"/>
    </c:if>

    <div class="mb-3">
        <label class="form-label">Supplier Name</label>
        <input type="text" name="name" class="form-control"
               value="${supplier.supplierName}" required>
    </div>

    <div class="mb-3">
        <label class="form-label">Contact Name</label>
        <input type="text" name="contact" class="form-control"
               value="${supplier.contactName}">
    </div>

    <div class="mb-3">
        <label class="form-label">Phone</label>
        <input type="text" name="phone" class="form-control"
               value="${supplier.phone}">
    </div>

    <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" class="form-control"
               value="${supplier.email}">
    </div>

    <div class="mb-3">
        <label class="form-label">Address</label>
        <input type="text" name="address" class="form-control"
               value="${supplier.address}">
    </div>

    <div class="mb-3">
        <label class="form-label">Logo URL</label>
        <input type="text" name="logo" class="form-control"
               value="${supplier.logo}">
    </div>

    <button type="submit" class="btn btn-success">
        <c:choose>
            <c:when test="${supplier != null}">
                Update
            </c:when>
            <c:otherwise>
                Add
            </c:otherwise>
        </c:choose>
    </button>

    <a href="supplier" class="btn btn-secondary">Back</a>

</form>

</body>
</html>