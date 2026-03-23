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
            <style>
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
                                                            <fmt:formatNumber value="${o.totalMoney}" type="currency"
                                                                currencySymbol="₫" />
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${o.status == 0}"><span class="badge"
                                                                        style="background-color: #6c757d;">Đã hủy</span>
                                                                </c:when>
                                                                <c:when test="${o.status == 1}"><span class="badge"
                                                                        style="background-color: #ffc107; color: #000;">Chờ
                                                                        xác nhận</span></c:when>
                                                                <c:when test="${o.status == 2}"><span class="badge"
                                                                        style="background-color: #17a2b8;">Đang đóng
                                                                        gói</span></c:when>
                                                                <c:when test="${o.status == 3}"><span class="badge"
                                                                        style="background-color: #0d6efd;">Đang
                                                                        giao</span></c:when>
                                                                <c:when test="${o.status == 4}"><span class="badge"
                                                                        style="background-color: #198754;">Hoàn
                                                                        thành</span></c:when>
                                                                <c:otherwise><span class="badge"
                                                                        style="background-color: #6c757d;">Không
                                                                        rõ</span></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <a href="order-detail?id=${o.orderId}" class="action-btn"
                                                                title="Xem chi tiết"><i class="ti-eye"></i></a>
                                                            <c:if test="${o.status == 1}">
                                                                <a href="update-order-status?orderId=${o.orderId}&status=2"
                                                                    class="action-btn" title="Xác nhận đơn"><i
                                                                        class="ti-check-box"
                                                                        style="color: #17a2b8;"></i></a>
                                                            </c:if>
                                                            <c:if test="${o.status == 2}">
                                                                <a href="update-order-status?orderId=${o.orderId}&status=3"
                                                                    class="action-btn" title="Giao hàng"><i
                                                                        class="ti-truck"
                                                                        style="color: #0d6efd;"></i></a>
                                                            </c:if>
                                                            <c:if test="${o.status == 3}">
                                                                <a href="update-order-status?orderId=${o.orderId}&status=4"
                                                                    class="action-btn" title="Hoàn thành"><i
                                                                        class="ti-check"></i></a>
                                                            </c:if>
                                                            <c:if test="${o.status > 0 && o.status < 4}">
                                                                <a href="cancel-order?id=${o.orderId}&from=admin"
                                                                    class="action-btn action-btn--delete"
                                                                    title="Hủy đơn"
                                                                    onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng #${o.orderId} không?');"><i
                                                                        class="ti-close"></i></a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Pagination -->
                                        <c:if test="${totalPages > 1}">
                                            <ul class="pagination" style="margin-top: 20px;">
                                                <li class="pagination-item ${currentPage == 1 ? 'pagination-item--disabled' : ''}">
                                                    <a href="order-list?page=${currentPage - 1}" class="pagination-item__link">
                                                        <i class="ti-angle-left"></i>
                                                    </a>
                                                </li>

                                                <c:set var="showEllipsis" value="false" />
                                                <c:forEach begin="1" end="${totalPages}" var="i">
                                                    <c:choose>
                                                        <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                                            <c:set var="showEllipsis" value="false" />
                                                            <li class="pagination-item">
                                                                <a href="order-list?page=${i}"
                                                                    class="pagination-item__link ${currentPage == i ? 'pagination-item__link--active' : ''}">${i}</a>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="${!showEllipsis}">
                                                                <li class="pagination-item">
                                                                    <span class="pagination-item__link"
                                                                        style="cursor: default; background: transparent; color: #939393;">...</span>
                                                                </li>
                                                                <c:set var="showEllipsis" value="true" />
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>

                                                <li class="pagination-item ${currentPage == totalPages ? 'pagination-item--disabled' : ''}">
                                                    <a href="order-list?page=${currentPage + 1}" class="pagination-item__link">
                                                        <i class="ti-angle-right"></i>
                                                    </a>
                                                </li>
                                            </ul>
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </div>
                </div>
            </body>

            </html>