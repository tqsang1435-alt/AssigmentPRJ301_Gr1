<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Lịch sử mua hàng - PhoneShop</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                <style>
                    .history-wrap {
                        padding: 40px 0 60px 0;
                        background-color: #f5f5f5;
                        min-height: 70vh;
                    }

                    .history-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        margin-bottom: 25px;
                    }

                    .history-title {
                        font-size: 2.4rem;
                        color: var(--text-color);
                        font-weight: bold;
                        text-transform: uppercase;
                        margin: 0;
                    }

                    .history-card {
                        background: #fff;
                        border-radius: 8px;
                        padding: 25px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                        overflow-x: auto;
                    }

                    .history-table {
                        width: 100%;
                        border-collapse: collapse;
                        min-width: 800px;
                    }

                    .history-table th,
                    .history-table td {
                        padding: 16px 15px;
                        text-align: left;
                        font-size: 1.4rem;
                        border-bottom: 1px solid #eee;
                    }

                    .history-table th {
                        background-color: #f8f9fa;
                        font-weight: 600;
                        color: #555;
                        text-transform: uppercase;
                        font-size: 1.3rem;
                    }

                    .history-table tbody tr:hover {
                        background-color: #f1f5f9;
                    }

                    .price-col {
                        color: var(--primary-color);
                        font-weight: bold;
                    }

                    .badge-status {
                        padding: 6px 12px;
                        border-radius: 50px;
                        font-size: 1.2rem;
                        color: #fff;
                        font-weight: 600;
                        display: inline-block;
                        text-align: center;
                        min-width: 100px;
                    }

                    .status-0 {
                        background-color: #6c757d;
                    }

                    /* Hủy */
                    .status-1 {
                        background-color: #ffc107;
                        color: #000;
                    }

                    /* Chờ xác nhận */
                    .status-2 {
                        background-color: #17a2b8;
                    }

                    /* Đang đóng gói */
                    .status-3 {
                        background-color: #0d6efd;
                    }

                    /* Đang giao */
                    .status-4 {
                        background-color: #198754;
                    }

                    /* Hoàn thành */

                    .btn-view {
                        text-decoration: none;
                        color: var(--primary-color);
                        border: 1px solid var(--primary-color);
                        background-color: transparent;
                        padding: 6px 12px;
                        border-radius: 4px;
                        font-size: 1.3rem;
                        transition: all 0.2s;
                        display: inline-flex;
                        align-items: center;
                        gap: 5px;
                    }

                    .btn-view:hover {
                        background-color: var(--primary-color);
                        color: #fff;
                    }

                    .empty-state {
                        text-align: center;
                        padding: 60px 20px;
                    }

                    .empty-state i {
                        font-size: 6rem;
                        color: #ccc;
                        margin-bottom: 20px;
                        display: block;
                    }

                    .empty-state h3 {
                        font-size: 2rem;
                        color: #333;
                        margin-bottom: 10px;
                        font-weight: 500;
                    }

                    .empty-state p {
                        font-size: 1.4rem;
                        color: #777;
                        margin-bottom: 20px;
                    }

                    .btn-cancel {
                        color: #dc3545;
                        border-color: #dc3545;
                    }

                    .btn-cancel:hover {
                        background-color: #dc3545;
                        color: #fff;
                    }

                    .alert {
                        padding: 15px;
                        margin-bottom: 20px;
                        border: 1px solid transparent;
                        border-radius: 4px;
                        font-size: 1.4rem;
                    }

                    .alert-success {
                        color: #155724;
                        background-color: #d4edda;
                        border-color: #c3e6cb;
                    }

                    .alert-danger {
                        color: #721c24;
                        background-color: #f8d7da;
                        border-color: #f5c6cb;
                    }
                </style>
            </head>

            <body>
                <jsp:include page="header.jsp" />

                <div class="history-wrap">
                    <div class="grid wide">
                        <div class="history-header">
                            <h2 class="history-title"><i class="ti-receipt" style="color: var(--primary-color);"></i>
                                Lịch sử đơn hàng</h2>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn--primary"><i
                                    class="ti-shopping-cart"></i> &nbsp;Tiếp tục mua sắm</a>
                        </div>

                        <div class="history-card">
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success" role="alert">
                                    ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session" />
                            </c:if>
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${sessionScope.errorMessage}
                                </div>
                                <c:remove var="errorMessage" scope="session" />
                            </c:if>
                            <c:choose>
                                <c:when test="${empty listOrders}">
                                    <div class="empty-state">
                                        <i class="ti-package"></i>
                                        <h3>Bạn chưa có đơn hàng nào</h3>
                                        <p>Hãy tham khảo các sản phẩm tuyệt vời tại PhoneShop nhé!</p>
                                        <a href="${pageContext.request.contextPath}/home" class="btn btn--primary">Mua
                                            sắm ngay</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <table class="history-table">
                                        <thead>
                                            <tr>
                                                <th>Mã ĐH</th>
                                                <th>Ngày đặt</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền</th>
                                                <th>Trạng thái</th>
                                                <th style="text-align: center;">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${listOrders}" var="o">
                                                <tr>
                                                    <td style="font-weight: bold; color: var(--text-color);">
                                                        #${o.orderId}</td>
                                                    <td>
                                                        <fmt:formatDate value="${o.orderDate}"
                                                            pattern="dd/MM/yyyy HH:mm" />
                                                    </td>
                                                    <td style="color: #666;"><i class="ti-location-pin"
                                                            style="color: var(--primary-color);"></i> ${not empty
                                                        o.shippingAddress ? o.shippingAddress : 'Chưa cập nhật'}</td>
                                                    <td class="price-col">
                                                        <fmt:formatNumber value="${o.totalMoney}" pattern="#,##0" /> ₫
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${o.status == 0}"><span
                                                                    class="badge-status status-0">Đã hủy</span></c:when>
                                                            <c:when test="${o.status == 1}"><span
                                                                    class="badge-status status-1">Chờ xác nhận</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 2}"><span
                                                                    class="badge-status status-2">Đang đóng gói</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 3}"><span
                                                                    class="badge-status status-3">Đang giao</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 4}"><span
                                                                    class="badge-status status-4">Hoàn thành</span>
                                                            </c:when>
                                                            <c:otherwise><span class="badge-status status-1">Đang xử
                                                                    lý</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="text-align: center;">
                                                        <a href="order-detail?id=${o.orderId}" class="btn-view"><i
                                                                class="ti-eye"></i> Chi tiết</a>
                                                        <c:if test="${o.status == 1}">
                                                            <a href="cancel-order?id=${o.orderId}"
                                                                class="btn-view btn-cancel"
                                                                onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng #${o.orderId} không?');">
                                                                <i class="ti-close"></i> Hủy đơn
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <jsp:include page="footer.jsp" />
            </body>

            </html>