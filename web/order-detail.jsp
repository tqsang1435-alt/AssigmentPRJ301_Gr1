<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Chi tiết Đơn hàng - PhoneShop</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body>
                <c:choose>
                    <c:when test="${sessionScope.ACC.role == 'Admin'}">
                        <div class="admin-container">
                            <jsp:include page="admin-sidebar.jsp" />
                            <div class="admin-main">
                                <div class="admin-header">
                                    <h3>Chi tiết Đơn hàng #${order.orderId}</h3>
                                    <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                                    </div>
                                </div>
                                <div class="admin-content">
                                    <div class="card">
                                        <div class="card-header d-flex justify-content-between align-items-center">
                                            <h3 class="card-title">Thông tin giao hàng</h3>
                                            <a href="order-list" class="btn btn--normal"><i class="ti-arrow-left"></i>
                                                Quay lại danh sách</a>
                                        </div>
                                        <div class="card-body">
                                            <jsp:include page="_orderDetailContent.jsp" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <jsp:include page="header.jsp" />
                        <div style="background-color: #f5f5f5; padding: 40px 0; min-height: 70vh;">
                            <div class="grid wide">
                                <div class="card">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Chi tiết Đơn hàng #${order.orderId}</h3>
                                        <a href="order-history" class="btn btn--normal"><i class="ti-arrow-left"></i>
                                            Quay lại lịch sử</a>
                                    </div>
                                    <div class="card-body">
                                        <jsp:include page="_orderDetailContent.jsp" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" />
                    </c:otherwise>
                </c:choose>
            </body>

            </html>