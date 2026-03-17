<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
                <c:redirect url="home" />
            </c:if>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <title>Supplier List</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body>
                <div class="admin-container">
                    <jsp:include page="admin-sidebar.jsp" />
                    <div class="admin-main">
                        <div class="admin-header">
                            <h2>Supplier Management</h2>
                        </div>
                        <div class="admin-content">
                            <div class="container-fluid">
                                <!-- flash success message -->
                                <c:if test="${not empty message}">
                                    <div class="alert alert-success">${message}</div>
                                </c:if>
                                <!-- error message -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:if>

                                <div class="card">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <h3 class="card-title mb-0">Supplier List</h3>
                                        <div class="filter-group">
                                            <form action="supplier" method="get"
                                                class="d-flex mb-0 align-items-center gap-2">
                                                <input type="hidden" name="action" value="search" />
                                                <input type="text" name="keyword" class="form-control"
                                                    placeholder="Search by name..." />
                                                <button class="btn btn-secondary">Search</button>
                                            </form>
                                            <a href="supplier-form.jsp" class="btn btn-success ms-2">Add Supplier</a>
                                        </div>
                                    </div>
                                    <table class="table table-bordered table-striped mb-0">
                                        <thead class="table-light">
                                            <tr>
                                                <th>ID</th>
                                                <!-- <th>Logo</th> -->
                                                <th>Name</th>
                                                <th>Contact</th>
                                                <th>Phone</th>
                                                <th>Email</th>
                                                <th>Address</th>
                                                <th>Status</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${suppliers}" var="s">
                                                <tr>
                                                    <td>${s.id}</td>
                                                    <!-- <td>
                                                        <c:choose>
                                                            <c:when test="${not empty s.logo}">
                                                                <img src="${s.logo}" alt="Logo" width="50" height="50" style="object-fit: contain; border: 1px solid #ddd; border-radius: 4px; background-color: #fff;">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div style="width: 50px; height: 50px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center; border-radius: 4px; color: #999; font-size: 10px; border: 1px solid #ddd;">N/A</div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td> -->
                                                    <td>${s.name}</td>
                                                    <td>${s.contactName}</td>
                                                    <td>${s.phone}</td>
                                                    <td>${s.email}</td>
                                                    <td>${s.address}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${s.status}"><span
                                                                    class="badge badge--tech">Active</span></c:when>
                                                            <c:otherwise><span
                                                                    class="badge badge--normal">Inactive</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="supplier?action=edit&id=${s.id}" class="action-btn"
                                                            title="Edit"><i class="ti-pencil"></i></a>
                                                        <a href="supplier?action=toggle&id=${s.id}" class="action-btn"
                                                            title="Toggle Status">
                                                            <i class="ti-exchange-vertical"></i>
                                                        </a>
                                                        <a href="supplier?action=remove&id=${s.id}"
                                                            onclick="return confirm('Permanently delete this supplier?')"
                                                            class="action-btn action-btn--delete" title="Delete"><i
                                                                class="ti-trash"></i></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </body>

            </html>