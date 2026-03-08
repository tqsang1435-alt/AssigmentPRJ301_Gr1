<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Supplier List</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

<h2>Supplier Management</h2>

<!-- flash success message -->
<c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
</c:if>

<!-- error message -->
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<a href="supplier-form.jsp" class="btn btn-primary mb-2">Add Supplier</a>

<form action="supplier" method="get" class="mb-3">
    <input type="hidden" name="action" value="search"/>
    <input type="text" name="keyword" placeholder="Search by name"/>
    <button class="btn btn-secondary btn-sm">Search</button>
</form>

<table class="table table-bordered">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Contact</th>
    <th>Phone</th>
    <th>Email</th>
    <th>Status</th>
    <th>Action</th>
</tr>

<c:forEach items="${suppliers}" var="s">
<tr>
    <td>${s.supplierID}</td>
    <td>${s.supplierName}</td>
    <td>${s.contactName}</td>
    <td>${s.phone}</td>
    <td>${s.email}</td>
    <td>
        <c:choose>
            <c:when test="${s.status}">Active</c:when>
            <c:otherwise>Inactive</c:otherwise>
        </c:choose>
    </td>
    <td>
        <a href="supplier?action=edit&id=${s.supplierID}" class="btn btn-warning btn-sm">Edit</a>
        <a href="supplier?action=toggle&id=${s.supplierID}" class="btn btn-secondary btn-sm">
            <c:choose>
                <c:when test="${s.status}">Deactivate</c:when>
                <c:otherwise>Activate</c:otherwise>
            </c:choose>
        </a>
        <a href="supplier?action=remove&id=${s.supplierID}"
           onclick="return confirm('Permanently delete this supplier?')"
           class="btn btn-danger btn-sm">Delete</a>
    </td>
</tr>
</c:forEach>
</table>
</table>

</body>
</html>