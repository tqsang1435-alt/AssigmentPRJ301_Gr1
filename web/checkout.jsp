<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <fmt:setLocale value="vi_VN" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Thanh toán</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body>
                <jsp:include page="header.jsp" />

                <div class="container" style="padding-top: 20px; padding-bottom: 50px;">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-7">
                                <h1>Thông tin thanh toán</h1>
                                <c:if test="${not empty mess}">
                                    <div
                                        style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; font-size: 1.4rem;">
                                        <i class="ti-alert"></i> ${mess}
                                    </div>
                                </c:if>
                                <form action="checkout" method="POST" class="form">
                                    <div class="form-group">
                                        <label for="name" class="form-label">Họ và tên</label>
                                        <input id="name" name="name" type="text" class="form-control"
                                            placeholder="Nhập họ và tên">
                                    </div>
                                    <div class="form-group">
                                        <label for="phone" class="form-label">Số điện thoại</label>
                                        <input id="phone" name="phone" type="text" class="form-control"
                                            placeholder="Nhập số điện thoại">
                                    </div>
                                    <div class="form-group">
                                        <label for="address" class="form-label">Địa chỉ giao hàng</label>
                                        <input id="address" name="address" type="text" class="form-control"
                                            placeholder="Nhập địa chỉ">
                                    </div>
                                    <button type="submit" class="btn btn--primary"
                                        style="min-width: 200px; height: 40px;">Hoàn tất đơn hàng</button>
                                </form>
                            </div>
                            <div class="col l-5">
                                <h2>Đơn hàng của bạn</h2>
                                <p>Phần này sẽ hiển thị tóm tắt các sản phẩm trong giỏ hàng.</p>

                                <div class="checkout-cart-list"
                                    style="background: #fff; padding: 15px; border-radius: 4px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
                                    <c:forEach items="${sessionScope.cart.cartItems}" var="item">
                                        <div class="checkout-item"
                                            style="display: flex; align-items: center; margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                                            <img src="${item.product.imageURL}" alt="${item.product.productName}"
                                                style="width: 50px; height: 50px; object-fit: cover; margin-right: 10px; border: 1px solid #ddd; border-radius: 4px;">
                                            <div class="checkout-item-info" style="flex: 1;">
                                                <h4 style="font-size: 1.4rem; margin: 0 0 5px;">
                                                    ${item.product.productName}</h4>
                                                <div style="font-size: 1.3rem; color: #666;">
                                                    <span>${item.quantity} x </span>
                                                    <span style="color: var(--primary-color); font-weight: 500;">
                                                        <fmt:formatNumber value="${item.product.price}"
                                                            pattern="#,##0" /> đ
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="checkout-item-total"
                                                style="font-size: 1.4rem; font-weight: bold; color: var(--text-color);">
                                                <fmt:formatNumber value="${item.subtotal}" pattern="#,##0" /> đ
                                            </div>
                                        </div>
                                    </c:forEach>

                                    <div class="checkout-summary-rows"
                                        style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee;">
                                        <div
                                            style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 1.4rem;">
                                            <span>Tạm tính:</span>
                                            <span>
                                                <fmt:formatNumber value="${sessionScope.cart.totalPrice}"
                                                    pattern="#,##0" /> đ
                                            </span>
                                        </div>
                                        <c:if test="${sessionScope.cart.discountAmount > 0}">
                                            <div
                                                style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 1.4rem; color: #28a745;">
                                                <span>Giảm giá hạng thành viên (
                                                    <fmt:formatNumber value="${sessionScope.cart.discountPercent}"
                                                        pattern="#,##0.#" />%):
                                                </span>
                                                <span>-
                                                    <fmt:formatNumber value="${sessionScope.cart.discountAmount}"
                                                        pattern="#,##0" /> đ
                                                </span>
                                            </div>
                                        </c:if>
                                        <div
                                            style="display: flex; justify-content: space-between; margin-top: 10px; padding-top: 10px; border-top: 2px solid #eee; font-size: 1.8rem; font-weight: bold;">
                                            <span>Tổng thanh toán:</span>
                                            <span style="color: var(--primary-color);">
                                                <fmt:formatNumber value="${sessionScope.cart.finalTotalPrice}"
                                                    pattern="#,##0" /> đ
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <jsp:include page="footer.jsp" />
            </body>

            </html>