<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Lịch sử mua hàng | PHONEShop</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <style>
                    /* Khớp mã màu với trạng thái từ Controller */
                    .status-1 {
                        background-color: #ffc107;
                        color: #000;
                    }

                    /* Chờ xác nhận */
                    .status-2 {
                        background-color: #0dcaf0;
                        color: #000;
                    }

                    /* Đang giao */
                    .status-3 {
                        background-color: #dc3545;
                        color: white;
                    }

                    /* Đã hủy */
                    .status-4 {
                        background-color: #198754;
                        color: white;
                    }

                    /* Hoàn thành */
                    .status-0 {
                        background-color: #6c757d;
                        color: white;
                    }

                    /* Khác */

                    .table-container {
                        border-radius: 15px;
                        overflow: hidden;
                        border: none;
                    }

                    .price-text {
                        color: #d63031;
                        font-weight: 700;
                    }

                    .empty-state {
                        padding: 80px 0;
                        text-align: center;
                        background: white;
                        border-radius: 15px;
                    }
                </style>
            </head>

            <body class="bg-light">
                <jsp:include page="navbar.jsp" />

                <div class="container mt-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2 class="fw-bold"><i class="fa-solid fa-clock-rotate-left me-2"></i>LỊCH SỬ ĐƠN HÀNG</h2>
                        <a href="home" class="btn btn-outline-primary rounded-pill px-4 text-decoration-none">
                            <i class="fa-solid fa-arrow-left me-1"></i> Tiếp tục mua sắm
                        </a>
                    </div>

                    <c:choose>
                        <c:when test="${empty orderList}">
                            <div class="empty-state shadow-sm">
                                <i class="fa-solid fa-box-open fa-4x text-muted mb-3"></i>
                                <h4 class="text-muted">Hiện tại bạn đang không có đơn hàng nào</h4>
                                <p class="text-secondary">Hãy khám phá thêm các sản phẩm tuyệt vời của chúng tôi!</p>
                                <a href="home" class="btn btn-primary rounded-pill px-5 mt-2">Mua sắm ngay</a>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="card table-container shadow-sm">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle mb-0">
                                        <thead class="table-dark">
                                            <tr>
                                                <th class="ps-4">STT</th>
                                                <th>Ngày đặt</th>
                                                <th>Tổng tiền</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Trạng thái</th>
                                                <th class="text-center">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${orderList}" var="o" varStatus="loop">
                                                <tr>
                                                    <td class="ps-4 fw-bold text-primary">${loop.count}</td>
                                                    <td>
                                                        <fmt:formatDate value="${o.orderDate}"
                                                            pattern="dd/MM/yyyy HH:mm" />
                                                    </td>
                                                    <td class="price-text">
                                                        <fmt:formatNumber value="${o.totalMoney}" groupingUsed="true" />
                                                        đ
                                                    </td>

                                                    <%-- FIX: Sử dụng đúng thuộc tính shippingAddress từ model Order
                                                        --%>
                                                        <td class="text-muted small">
                                                            <i class="fa-solid fa-location-dot me-1 text-danger"></i>
                                                            <c:out
                                                                value="${not empty o.shippingAddress ? o.shippingAddress : 'Chưa cập nhật'}" />
                                                        </td>

                                                        <td>
                                                            <span
                                                                class="badge rounded-pill status-${o.status} px-3 py-2">
                                                                <c:choose>
                                                                    <c:when test="${o.status == 1}">Chờ xác nhận
                                                                    </c:when>
                                                                    <c:when test="${o.status == 2}">Đang giao</c:when>
                                                                    <c:when test="${o.status == 3}">Đã hủy đơn</c:when>
                                                                    <c:when test="${o.status == 4}">Hoàn thành</c:when>
                                                                    <c:otherwise>Đang xử lý</c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </td>
                                                        <td class="text-center">
                                                            <a href="order-detail?id=${o.orderID}"
                                                                class="btn btn-sm btn-outline-dark rounded-pill px-3">
                                                                <i class="fa-solid fa-eye me-1"></i>Chi tiết
                                                            </a>
                                                        </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>