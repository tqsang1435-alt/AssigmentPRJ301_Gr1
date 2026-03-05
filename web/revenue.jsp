<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thống kê Doanh thu - PhoneShop Admin</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>

    <body>
        <header class="header">
            <div class="grid wide">
                <div class="header__navbar">
                    <a href="${pageContext.request.contextPath}/" class="header__logo"><i class="ti-bar-chart"></i>
                        <label>PhoneShop Thống Kê</label></a>
                    <ul class="header__nav-list">
                        <li class="header__nav-item">
                            <a href="#" class="header__nav-link"><i class="ti-angle-left"></i> <label>Về
                                    Dashboard</label></a>
                        </li>
                    </ul>
                </div>
            </div>
        </header>

        <div class="admin-content">
            <div class="grid wide">
                <div class="row">
                    <div class="col l-12 m-12 c-12">
                        <div class="chart-card">

                            <div class="chart-header">
                                <h2 class="chart-title">Doanh thu 7 ngày gần nhất</h2>
                                <div class="filter-group">
                                    <select>
                                        <option value="7days">7 ngày qua</option>
                                        <option value="thisMonth">Tháng này</option>
                                        <option value="lastMonth">Tháng trước</option>
                                    </select>
                                    <button class="btn btn--primary btn--size-s">Lọc</button>
                                </div>
                            </div>

                            <div style="position: relative; height:400px; width:100%">
                                <canvas id="revenueChart"></canvas>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // 1. Tóm lấy cái thẻ canvas
            const ctx = document.getElementById('revenueChart').getContext('2d');

            // 2. Dữ liệu giả lập (Mốt ông thay cục này bằng data từ Database ném sang)
            // Mẹo: Dùng EL của JSP ném vào đây ví dụ: const data = ${revenueDataList};
            const labels = ['27/02', '28/02', '01/03', '02/03', '03/03', '04/03', '05/03'];
            const dataValues = [12000000, 19000000, 15000000, 22000000, 18000000, 25000000, 30000000]; // Đơn vị: VNĐ

            // 3. Cấu hình và vẽ biểu đồ
            new Chart(ctx, {
                type: 'bar', // có thể dùng 'line': biểu đồ đường
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Doanh thu (VNĐ)',
                        data: dataValues,
                        backgroundColor: 'rgba(238, 75, 43, 0.8)',
                        borderColor: 'rgba(238, 75, 43, 1)',
                        borderWidth: 1,
                        borderRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function (value) {
                                    return value.toLocaleString('vi-VN') + ' đ';
                                }
                            }
                        }
                    },
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    if (context.parsed.y !== null) {
                                        label += context.parsed.y.toLocaleString('vi-VN') + ' đ';
                                    }
                                    return label;
                                }
                            }
                        }
                    }
                }
            });
        </script>
    </body>

    </html>