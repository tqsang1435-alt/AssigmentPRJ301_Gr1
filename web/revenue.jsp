<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
                <c:redirect url="user-login" />
            </c:if>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Thống kê Doanh thu - PhoneShop Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1008">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            </head>

            <body>
                <div class="admin-container">
                    <jsp:include page="admin-sidebar.jsp"></jsp:include>
                    <div class="admin-main">
                        <div class="admin-header">
                            <h3>Thống kê Doanh thu</h3>
                            <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
                        </div>
                        <div class="admin-content">
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">Biểu đồ doanh thu hàng tháng</h3>
                                </div>
                                <div class="card-body">
                                    <canvas id="revenueChart"></canvas>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">Chi tiết doanh thu theo tháng</h3>
                                </div>
                                <div class="card-body">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Thời gian</th>
                                                <th>Số lượng đơn</th>
                                                <th style="text-align: right;">Tổng doanh thu</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${monthlyRevenue}" var="stat">
                                                <tr>
                                                    <td>Tháng ${stat.month}/${stat.year}</td>
                                                    <td>${stat.orderCount}</td>
                                                    <td
                                                        style="text-align: right; font-weight: bold; color: var(--primary-color);">
                                                        <fmt:formatNumber value="${stat.totalRevenue}" type="currency"
                                                            currencySymbol="₫" />
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty monthlyRevenue}">
                                                <tr>
                                                    <td colspan="3" style="text-align: center; padding: 40px;">Chưa có
                                                        dữ liệu doanh thu.</td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <script>
                    const ctx = document.getElementById('revenueChart').getContext('2d');
                    const labels = [];
                    const data = [];
                    <c:forEach items="${monthlyRevenue}" var="stat">
                        labels.unshift(`Tháng ${'${stat.month}'}/${'${stat.year}'}`);
                        data.unshift(${stat.totalRevenue});
                    </c:forEach>

                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: 'Doanh thu (VNĐ)',
                                data: data,
                                backgroundColor: 'rgba(255, 193, 7, 0.5)',
                                borderColor: 'rgba(255, 193, 7, 1)',
                                borderWidth: 1
                            }]
                        },
                        options: {
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                        }
                    });
                </script>
            </body>

            </html>