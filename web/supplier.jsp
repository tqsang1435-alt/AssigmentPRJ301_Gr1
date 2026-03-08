<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Supplier Management</title>
</head>
<body>

<h2>Add Supplier</h2>

<form method="post" action="supplier">
    Name: <input type="text" name="name" required/><br/>
    Contact: <input type="text" name="contact"/><br/>
    Phone: <input type="text" name="phone"/><br/>
    Email: <input type="text" name="email"/><br/>
    Address: <input type="text" name="address"/><br/>
    Logo URL: <input type="text" name="logo"/><br/>
    <button type="submit">Add</button>
</form>

<hr>

<h2>Supplier List</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Contact</th>
        <th>Phone</th>
        <th>Email</th>
        <th>Address</th>
        <th>Status</th>
    </tr>

    <c:forEach items="${suppliers}" var="s">
        <tr>
            <td>${s.supplierID}</td>
            <td>${s.supplierName}</td>
            <td>${s.contactName}</td>
            <td>${s.phone}</td>
            <td>${s.email}</td>
            <td>${s.address}</td>
            <td>
                <c:choose>
                    <c:when test="${s.status}">Active</c:when>
                    <c:otherwise>Inactive</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>