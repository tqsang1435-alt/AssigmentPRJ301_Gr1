<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
                <c:redirect url="home" />
            </c:if>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Quản lý Đơn hàng - PhoneShop Admin</title>
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
                                <h3>Quản lý Đơn hàng</h3>
                                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                                </div>
                            </div>

                            <div class="admin-content">
                                <div class="card">
                                    <div class="card-header">
                                        <h3 class="card-title">Danh sách đơn hàng</h3>
                                    </div>
                                    <div class="card-body">
                                        <%-- Lưu ý: Cần có Servlet load danh sách đơn hàng và setAttribute("listOrders")
                                            --%>
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Mã ĐH</th>
                                                        <th>Khách hàng</th>
                                                        <th>Ngày đặt</th>
                                                        <th>Tổng tiền</th>
                                                        <th>Trạng thái</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:if test="${empty listOrders}">
                                                        <tr>
                                                            <td colspan="6" style="text-align: center; padding: 40px;">
                                                                Chưa có đơn hàng nào.</td>
                                                        </tr>
                                                    </c:if>
                                                    <c:forEach items="${listOrders}" var="o">
                                                        <tr>
                                                            <td>#${o.orderId}</td>
                                                            <td>${o.customerName}</td>
                                                            <td>
                                                                <fmt:formatDate value="${o.orderDate}"
                                                                    pattern="dd/MM/yyyy HH:mm" />
                                                            </td>
                                                            <td style="color: var(--primary-color); font-weight: bold;">
                                                                <fmt:formatNumber value="${o.totalMoney}"
                                                                    type="currency" currencySymbol="₫" />
                                                            </td>
                                                            <td>
                                                                <c:if test="${o.status == 1}">
                                                                    <span class="badge badge--normal">Chờ xử lý</span>
                                                                </c:if>
                                                                <c:if test="${o.status == 4}">
                                                                    <span class="badge badge--tech">Hoàn thành</span>
                                                                </c:if>
                                                                <%-- Bạn có thể thêm các trạng thái khác ở đây --%>
                                                            </td>
                                                            <td>
                                                                <a href="order-detail?id=${o.orderId}"
                                                                    class="action-btn" title="Xem chi tiết"><i
                                                                        class="ti-eye"></i></a>
                                                                <a href="update-order-status?orderId=${o.orderId}&status=4"
                                                                    class="action-btn" title="Hoàn thành"><i
                                                                        class="ti-check"></i></a>
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