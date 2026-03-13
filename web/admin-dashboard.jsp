<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
            <c:redirect url="home" />
        </c:if>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
                <!-- Thêm thư viện Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                                                    ${totalCustomers}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col l-3 m-6 c-12">
                                        <div class="card">
                                            <div class="card-body">
                                                <h3 class="card-title">Sản phẩm</h3>
                                                <p class="card-text"
                                                    style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                    ${totalProducts}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col l-3 m-6 c-12">
                                        <div class="card">
                                            <div class="card-body">
                                                <h3 class="card-title">Đơn hàng mới</h3>
                                                <p class="card-text"
                                                    style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                    ${newOrders}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col l-3 m-6 c-12">
                                        <div class="card">
                                            <div class="card-body">
                                                <h3 class="card-title">Doanh thu tháng</h3>
                                                <p class="card-text"
                                                    style="font-size: 2.4rem; font-weight: bold; color: var(--primary-color); margin-top: 10px;">
                                                    <fmt:formatNumber value="${monthRevenue}" type="currency"
                                                        currencySymbol="₫" />
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- KHU VỰC BIỂU ĐỒ -->
                                <div class="row" style="margin-top: 30px;">
                                    <!-- Biểu đồ doanh thu ngày -->
                                    <div class="col l-8 m-12 c-12">
                                        <div class="card" style="height: 100%">
                                            <div class="card-header">
                                                <h3 class="card-title">Doanh thu theo ngày</h3>
                                            </div>
                                            <div class="card-body">
                                                <canvas id="dailyChart" style="width: 100%; height: 300px;"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Biểu đồ sản phẩm -->
                                    <div class="col l-4 m-12 c-12">
                                        <div class="card" style="height: 100%">
                                            <div class="card-header">
                                                <h3 class="card-title">Top sản phẩm</h3>
                                            </div>
                                            <div class="card-body">
                                                <canvas id="productChart" style="width: 100%; height: 250px;"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row" style="margin-top: 20px;">
                                    <div class="col l-12 m-12 c-12">
                                        <div class="card">
                                            <div class="card-header">
                                                <h3 class="card-title">Doanh thu theo tháng</h3>
                                            </div>
                                            <div class="card-body">
                                                <canvas id="monthlyChart" style="width: 100%; height: 350px;"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                </div>

                <!-- Script vẽ biểu đồ -->
                <script>
                    // 1. Biểu đồ ngày (Line Chart)
                    new Chart(document.getElementById('dailyChart'), {
                        type: 'line',
                        data: {
                            labels: ${ dailyLabels },
                        datasets: [{
                            label: 'Doanh thu (VNĐ)',
                            data: ${ dailyData },
                        borderColor: '#0d6efd',
                        backgroundColor: 'rgba(13, 110, 253, 0.1)',
                        tension: 0.3, fill: true
                            }]
                        },
                        options: { responsive: true, maintainAspectRatio: false }
                    });

                    // 2. Biểu đồ sản phẩm (Doughnut Chart)
                    new Chart(document.getElementById('productChart'), {
                        type: 'doughnut',
                        data: {
                            labels: ${ productLabels },
                        datasets: [{
                            data: ${ productData },
                        backgroundColor: ['#ff6384', '#36a2eb', '#ffce56', '#4bc0c0', '#9966ff']
                            }]
                        },
                        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
                    });

                    // 3. Biểu đồ tháng (Bar Chart)
                    new Chart(document.getElementById('monthlyChart'), {
                        type: 'bar',
                        data: {
                            labels: ${ monthlyLabels },
                        datasets: [{
                            label: 'Doanh thu tháng (VNĐ)',
                            data: ${ monthlyData },
                        backgroundColor: '#198754'
                            }]
                        },
                        options: { responsive: true, maintainAspectRatio: false }
                    });
                </script>
            </body>

            </html>