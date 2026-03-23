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
                <!-- Thêm thư viện Marked.js để render Markdown đầy đủ -->
                <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
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
                    // Khai báo data arrays ở scope ngoài để Chart.js và AI listener dùng chung
                    // Tránh lỗi "Cannot redeclare block-scoped variable"
                    var dailyLabels   = ${dailyLabels};
                    var dailyValues   = ${dailyData};
                    var monthlyLabels = ${monthlyLabels};
                    var monthlyValues = ${monthlyData};
                    var productLabels = ${productLabels};
                    var productValues = ${productData};

                    // 1. Biểu đồ ngày (Line Chart)
                    new Chart(document.getElementById('dailyChart'), {
                        type: 'line',
                        data: {
                            labels: dailyLabels,
                            datasets: [{
                                label: 'Doanh thu (VNĐ)',
                                data: dailyValues,
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
                            labels: productLabels,
                            datasets: [{
                                data: productValues,
                                backgroundColor: ['#ff6384', '#36a2eb', '#ffce56', '#4bc0c0', '#9966ff']
                            }]
                        },
                        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
                    });

                    // 3. Biểu đồ tháng (Bar Chart)
                    new Chart(document.getElementById('monthlyChart'), {
                        type: 'bar',
                        data: {
                            labels: monthlyLabels,
                            datasets: [{
                                label: 'Doanh thu tháng (VNĐ)',
                                data: monthlyValues,
                                backgroundColor: '#198754'
                            }]
                        },
                        options: { responsive: true, maintainAspectRatio: false }
                    });

                    // AI Phân tích
                    document.getElementById('btn-ai-analyze').addEventListener('click', function () {
                        var btn = this;
                        var resultDiv = document.getElementById('ai-result');

                        btn.disabled = true;
                        btn.innerText = 'Đang phân tích...';
                        resultDiv.innerHTML = '<div style="text-align: center; color: #666;"><i class="ti-reload" style="animation: spin 1s linear infinite; display: inline-block;"></i> AI đang xử lý dữ liệu...</div>';

                        // Fix Lỗi 2: Tạo chuỗi dữ liệu có label ngày/tháng để AI phân tích được chính xác
                        var dailyDetail = dailyLabels.map(function(d, i) {
                            return '  ' + d + ': ' + (dailyValues[i] || 0).toLocaleString('vi-VN') + ' VNĐ';
                        }).join('\n');

                        var monthlyDetail = monthlyLabels.map(function(m, i) {
                            return '  Tháng ' + m + ': ' + (monthlyValues[i] || 0).toLocaleString('vi-VN') + ' VNĐ';
                        }).join('\n');

                        var productDetail = productLabels.map(function(p, i) {
                            return '  ' + p + ': ' + (productValues[i] || 0).toLocaleString('vi-VN') + ' VNĐ doanh thu';
                        }).join('\n');

                        var prompt =
                            "Bạn là chuyên gia phân tích dữ liệu kinh doanh. Dưới đây là số liệu thực tế của cửa hàng PhoneShop:\n" +
                            "- Tổng khách hàng: ${totalCustomers} người\n" +
                            "- Tổng sản phẩm: ${totalProducts} sản phẩm\n" +
                            "- Đơn hàng mới (chờ xử lý): ${newOrders} đơn\n" +
                            "- Doanh thu tháng này: ${monthRevenue} VNĐ\n\n" +
                            "Doanh thu theo từng ngày gần đây:\n" + dailyDetail + "\n\n" +
                            "Doanh thu theo từng tháng:\n" + monthlyDetail + "\n\n" +
                            "Top sản phẩm bán chạy:\n" + productDetail + "\n\n" +
                            "Dựa vào các số liệu trên, hãy:\n" +
                            "1. Đánh giá tổng quan tình hình kinh doanh hiện tại (điểm mạnh, điểm yếu)\n" +
                            "2. Phân tích xu hướng doanh thu (ngày/tháng nào tốt, ngày/tháng nào kém)\n" +
                            "3. Dự đoán xu hướng tháng tới\n" +
                            "4. Đề xuất 3 hành động cụ thể để tăng doanh thu\n" +
                            "Trình bày rõ ràng bằng Markdown (dùng heading, bảng, bullet list).";

                        var formData = new URLSearchParams();
                        formData.append('message', prompt);
                        formData.append('mode', 'admin');

                        fetch('chat-bot', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                            body: formData.toString()
                        })
                        .then(function(response) { return response.text(); })
                        .then(function(data) {
                            // Fix Lỗi 3: Dùng marked.js để render Markdown đầy đủ
                            // (headers, bảng, bold, italic, bullets, code blocks, hr...)
                            if (typeof marked !== 'undefined') {
                                marked.setOptions({ breaks: true, gfm: true });
                                resultDiv.innerHTML = marked.parse(data);
                            } else {
                                // Fallback nếu marked.js không load được
                                var html = data
                                    .replace(/^### (.*?)$/gm, '<h3>$1</h3>')
                                    .replace(/^## (.*?)$/gm, '<h2>$1</h2>')
                                    .replace(/^# (.*?)$/gm, '<h1>$1</h1>')
                                    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
                                    .replace(/\*(.*?)\*/g, '<em>$1</em>')
                                    .replace(/^[-*] (.*?)$/gm, '<li>$1</li>')
                                    .replace(/^---$/gm, '<hr>')
                                    .replace(/\n/g, '<br>');
                                resultDiv.innerHTML = html;
                            }
                        })
                        .catch(function(error) {
                            resultDiv.innerHTML = '<span style="color: red;">Lỗi kết nối đến AI. Vui lòng thử lại sau.</span>';
                        })
                        .finally(function() {
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