<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Chi tiết Đơn hàng - PhoneShop Admin</title>
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
                                <h3>Chi tiết Đơn hàng #${order.orderID}</h3>
                                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                                </div>
                            </div>

                            <div class="admin-content">
                                <div class="card">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Thông tin giao hàng</h3>
                                        <a href="order-list" class="btn btn--normal"><i class="ti-arrow-left"></i> Quay
                                            lại</a>
                                    </div>
                                    <div class="card-body">
                                        <div class="row"
                                            style="margin-bottom: 30px; font-size: 1.5rem; line-height: 1.8;">
                                            <div class="col l-6 m-6 c-12">
                                                <p><strong>Khách hàng:</strong> ${order.customerName}</p>
                                                <p><strong>Số điện thoại:</strong> ${order.phone}</p>
                                                <p><strong>Địa chỉ:</strong> ${order.address}</p>
                                            </div>
                                            <div class="col l-6 m-6 c-12">
                                                <p><strong>Ngày đặt:</strong>
                                                    <fmt:formatDate value="${order.orderDate}"
                                                        pattern="dd/MM/yyyy HH:mm" />
                                                </p>
                                                <p><strong>Tổng tiền:</strong> <span
                                                        style="color: var(--primary-color); font-weight: bold;">
                                                        <fmt:formatNumber value="${order.totalMoney}" pattern="#,##0" />
                                                        ₫
                                                    </span></p>
                                                <p><strong>Trạng thái:</strong>
                                                    <span
                                                        class="badge ${order.status == 1 ? 'badge--tech' : 'badge--reseller'}">
                                                        ${order.status == 1 ? 'Đang xử lý' : 'Đã hoàn thành'}
                                                    </span>
                                                </p>
                                            </div>
                                        </div>

                                        <h3 class="card-title" style="margin-bottom: 15px;">Danh sách Sản phẩm</h3>
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>Hình ảnh</th>
                                                    <th>Tên sản phẩm</th>
                                                    <th>Đơn giá</th>
                                                    <th>Số lượng</th>
                                                    <th>Thành tiền</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${orderDetails}" var="item">
                                                    <tr>
                                                        <td><img src="${item.product.imageURL}" alt=""
                                                                style="width: 50px; height: 50px; object-fit: contain;">
                                                        </td>
                                                        <td>${item.product.productName}</td>
                                                        <td>
                                                            <fmt:formatNumber value="${item.price}" pattern="#,##0" /> ₫
                                                        </td>
                                                        <td>${item.quantity}</td>
                                                        <td style="color: var(--primary-color); font-weight: bold;">
                                                            <fmt:formatNumber value="${item.price * item.quantity}"
                                                                pattern="#,##0" /> ₫
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