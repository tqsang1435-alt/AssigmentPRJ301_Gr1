<%@ page pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

            <div class="row" style="margin-bottom: 30px; font-size: 1.5rem; line-height: 1.8;">
                <div class="col l-6 m-6 c-12">
                    <c:choose>
                        <c:when test="${sessionScope.ACC.role == 'Admin'}">
                            <p><strong>Khách hàng:</strong> ${order.customerName}</p>
                        </c:when>
                        <c:otherwise>
                            <p><strong>Khách hàng:</strong> ${sessionScope.ACC.fullName}</p>
                        </c:otherwise>
                    </c:choose>
                    <p><strong>Số điện thoại:</strong> ${sessionScope.ACC.phoneNumber}</p>
                    <p><strong>Địa chỉ:</strong> ${not empty order.shippingAddress ? order.shippingAddress : 'Chưa cập
                        nhật'}</p>
                </div>
                <div class="col l-6 m-6 c-12">
                    <p><strong>Ngày đặt:</strong>
                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                    </p>
                    <p><strong>Tổng tiền:</strong>
                        <span style="color: var(--primary-color); font-weight: bold;">
                            <fmt:formatNumber value="${order.totalMoney}" pattern="#,##0" /> ₫
                        </span>
                    </p>
                    <p><strong>Trạng thái:</strong>
                        <c:choose>
                            <c:when test="${order.status == 0}"><span class="badge"
                                    style="background-color: #6c757d;">Đã hủy</span></c:when>
                            <c:when test="${order.status == 1}"><span class="badge"
                                    style="background-color: #ffc107; color: #000;">Chờ xác nhận</span></c:when>
                            <c:when test="${order.status == 2}"><span class="badge"
                                    style="background-color: #17a2b8;">Đang đóng gói</span></c:when>
                            <c:when test="${order.status == 3}"><span class="badge"
                                    style="background-color: #0d6efd;">Đang giao</span></c:when>
                            <c:when test="${order.status == 4}"><span class="badge"
                                    style="background-color: #198754;">Hoàn thành</span></c:when>
                            <c:otherwise><span class="badge" style="background-color: #ffc107; color: #000;">Đang xử
                                    lý</span></c:otherwise>
                        </c:choose>
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
                    <c:forEach items="${detailList}" var="item">
                        <tr>
                            <td><img src="${not empty item.imageURL ? item.imageURL : 'assets/img/default-phone.png'}"
                                    alt="" style="width: 50px; height: 50px; object-fit: contain;"
                                    onerror="this.onerror=null; this.src='assets/img/default-phone.png';"></td>
                            <td>${not empty item.productName ? item.productName : productNames[item.productId]}</td>
                            <td>
                                <fmt:formatNumber value="${item.price}" pattern="#,##0" /> ₫
                            </td>
                            <td>${item.quantity}</td>
                            <td style="color: var(--primary-color); font-weight: bold;">
                                <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0" /> ₫
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>