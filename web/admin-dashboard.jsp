<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
            <c:redirect url="home" />
        </c:if>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Admin Dashboard - PhoneShop</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        </head>

        <body>
            <div class="admin-container">
                <%-- INCLUDE SIDEBAR --%>
                    <jsp:include page="admin-sidebar.jsp"></jsp:include>

                    <div class="admin-main">
                        <div class="admin-header">
                            <h3>Tổng quan hệ thống</h3>
                            <div class="admin-user-info">
                                Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                            </div>
                        </div>

                        <div class="admin-content">
                            <div class="row">
                                <div class="col l-3 m-6 c-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <h3 class="card-title">Khách hàng</h3>
                                            <p class="card-text"
                                                style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                120</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col l-3 m-6 c-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <h3 class="card-title">Sản phẩm</h3>
                                            <p class="card-text"
                                                style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                45</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col l-3 m-6 c-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <h3 class="card-title">Đơn hàng mới</h3>
                                            <p class="card-text"
                                                style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                12</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col l-3 m-6 c-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <h3 class="card-title">Doanh thu tháng</h3>
                                            <p class="card-text"
                                                style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                150M</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="card" style="margin-top: 30px;">
                                <div class="card-header">
                                    <h3 class="card-title">Hoạt động gần đây</h3>
                                </div>
                                <div class="card-body">
                                    <p style="text-align: center; color: #888; padding: 40px;">Chưa có dữ liệu thống kê
                                        chi tiết.</p>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
        </body>

        </html>