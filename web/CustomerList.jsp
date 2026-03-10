<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
            <c:redirect url="home" />
        </c:if>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Quản lý Khách hàng - PhoneShop Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        </head>

        <body>
            <div class="admin-container">
                <%-- SIDEBAR --%>
                    <jsp:include page="admin-sidebar.jsp"></jsp:include>

                    <div class="admin-main">
                        <div class="admin-header">
                            <h3>Quản lý Khách hàng</h3>
                            <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
                        </div>

                        <div class="admin-content">
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">Danh sách khách hàng</h3>
                                </div>
                                <div class="card-body">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Họ và tên</th>
                                                <th>Email</th>
                                                <th>SĐT</th>
                                                <th>Điểm</th>
                                                <th>Hạng</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${listC}" var="c">
                                                <tr>
                                                    <td>#${c.userID}</td>
                                                    <td>${c.fullName}</td>
                                                    <td>${c.email}</td>
                                                    <td>${c.phoneNumber}</td>
                                                    <td style="font-weight: bold; color: var(--primary-color);">
                                                        ${c.rewardPoints}</td>
                                                    <td>
                                                        <span class="badge badge--normal">${c.customerType}</span>
                                                    </td>
                                                    <td>
                                                        <a href="#" class="action-btn" title="Chỉnh sửa"><i
                                                                class="ti-pencil"></i></a>
                                                        <a href="#" class="action-btn action-btn--delete" title="Xóa"><i
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