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
                                                    <%-- Dữ liệu mẫu hoặc vòng lặp c:forEach --%>
                                                        <tr>
                                                            <td>#1001</td>
                                                            <td>Nguyễn Văn A</td>
                                                            <td>2023-10-25</td>
                                                            <td style="color: var(--primary-color); font-weight: bold;">
                                                                15,000,000₫</td>
                                                            <td><span class="badge badge--normal">Chờ xử lý</span></td>
                                                            <td>
                                                                <a href="#" class="action-btn" title="Xem chi tiết"><i
                                                                        class="ti-eye"></i></a>
                                                                <a href="update-order-status?orderId=1001&status=4"
                                                                    class="action-btn" title="Hoàn thành"><i
                                                                        class="ti-check"></i></a>
                                                            </td>
                                                        </tr>
                                                </tbody>
                                            </table>
                                            <p style="text-align: center; color: #888; margin-top: 20px;">(Dữ liệu đang
                                                được cập nhật...)</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                </div>
            </body>

            </html>