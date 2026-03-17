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

                                <!-- KHU VỰC AI DỰ ĐOÁN -->
                                <div class="row" style="margin-top: 20px;">
                                    <div class="col l-12 m-12 c-12">
                                        <div class="card">
                                            <div class="card-header"
                                                style="display: flex; justify-content: space-between; align-items: center;">
                                                <h3 class="card-title"><i class="ti-wand"
                                                        style="color: var(--primary-color);"></i> AI Phân Tích & Dự Đoán
                                                </h3>
                                                <button id="btn-ai-analyze" class="btn btn--primary"
                                                    style="height: 36px; padding: 0 15px; border-radius: 4px; border: none; cursor: pointer; color: #fff; background-color: var(--primary-color);">Phân
                                                    tích ngay</button>
                                            </div>
                                            <div class="card-body">
                                                <div id="ai-result"
                                                    style="padding: 15px; background: #f8f9fa; border-radius: 4px; min-height: 100px; font-size: 1.5rem; line-height: 1.6; white-space: pre-wrap; color: var(--text-color);">
                                                    Nhấn nút "Phân tích ngay" để AI đưa ra nhận xét về tình hình kinh
                                                    doanh và dự đoán xu hướng sắp tới dựa trên dữ liệu hiện tại...</div>
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

                    // AI Phân tích
                    document.getElementById('btn-ai-analyze').addEventListener('click', function () {
                        const btn = this;
                        const resultDiv = document.getElementById('ai-result');

                        btn.disabled = true;
                        btn.innerText = 'Đang phân tích...';
                        resultDiv.innerHTML = '<div style="text-align: center; color: #666;"><i class="ti-reload" style="animation: spin 1s linear infinite; display: inline-block;"></i> AI đang xử lý dữ liệu...</div>';

                        const prompt = "Bạn là chuyên gia phân tích dữ liệu kinh doanh. Dưới đây là số liệu của cửa hàng PhoneShop:\n" +
                            "- Tổng khách hàng: ${totalCustomers}\n" +
                            "- Tổng sản phẩm: ${totalProducts}\n" +
                            "- Đơn hàng mới: ${newOrders}\n" +
                            "- Doanh thu tháng này: ${monthRevenue} VNĐ\n" +
                            "- Doanh thu theo ngày: " + JSON.stringify(${ dailyData }) + "\n" +
                            "- Doanh thu theo tháng: " + JSON.stringify(${ monthlyData }) + "\n\n" +
                            "Dựa vào các số liệu trên, hãy đánh giá tình hình kinh doanh hiện tại, dự đoán xu hướng tháng tới và đề xuất 3 hành động cụ thể để tăng doanh thu. Viết ngắn gọn, trình bày rõ ràng.";

                        const formData = new URLSearchParams();
                        formData.append('message', prompt);
                        formData.append('mode', 'admin');

                        fetch('chat-bot', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: formData.toString()
                        })
                            .then(response => response.text())
                            .then(data => {
                                let formattedData = data.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
                                formattedData = formattedData.replace(/(^|\n)\* (.*?)/g, '$1• $2');
                                resultDiv.innerHTML = formattedData;
                            })
                            .catch(error => {
                                resultDiv.innerHTML = '<span style="color: red;">Lỗi kết nối đến AI. Vui lòng thử lại sau.</span>';
                            })
                            .finally(() => {
                                btn.disabled = false;
                                btn.innerText = 'Phân tích ngay';
                            });
                    });
                </script>
                <style>
                    @keyframes spin {
                        100% {
                            transform: rotate(360deg);
                        }
                    }
                </style>
            </body>

            </html>