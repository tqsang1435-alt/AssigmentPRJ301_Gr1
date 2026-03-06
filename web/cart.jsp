<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Giỏ hàng - PhoneShop</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=999">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body class="cart_body">
                <header class="header">
                    <div class="grid wide">
                        <div class="header__navbar">
                            <a href="${pageContext.request.contextPath}/" class="header__logo">
                                <i class="ti-mobile"></i> <label>PhoneShop</label>
                            </a>

                            <div class="header__search hide-on-mobile">
                                <input type="text" class="header__search-input"
                                    placeholder="Tìm kiếm điện thoại, laptop, phụ kiện...">
                                <button class="header__search-btn"><i class="ti-search"></i></button>
                            </div>

                            <ul class="header__nav-list">
                                <li class="header__nav-item">
                                    <a href="${pageContext.request.contextPath}/view-cart"
                                        class="header__nav-link header__nav-link--warning">
                                        <i class="ti-shopping-cart"></i> <label class="hide-on-mobile">Giỏ hàng</label>
                                        <span
                                            style="background: var(--gold-color); color: var(--black-color); padding: 2px 6px; border-radius: 50%; font-size: 1.2rem; margin-left: 5px; font-weight: bold;">
                                            ${cart.totalQuantity > 0 ? cart.totalQuantity : '0'}
                                        </span>
                                    </a>
                                </li>
                                <c:if test="${not empty sessionScope.ACC}">
                                    <li class="header__nav-item">
                                        <a href="${pageContext.request.contextPath}/profile" class="header__nav-link">
                                            <i class="ti-user"></i> <label class="hide-on-mobile">Tài khoản</label>
                                        </a>
                                    </li>
                                </c:if>
                                <c:if test="${empty sessionScope.ACC}">
                                    <li class="header__nav-item">
                                        <a href="${pageContext.request.contextPath}/login.jsp" class="header__nav-link">
                                            <i class="ti-user"></i> <label class="hide-on-mobile">Đăng nhập</label>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </header>

                <div class="container">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12 m-12 c-12">
                                <div class="main-title">
                                    <i class="ti-shopping-cart-full"></i>
                                    <h1>Giỏ hàng của bạn</h1>
                                    <p>Kiểm tra và hoàn tất đơn hàng để nhận ngàn ưu đãi.</p>
                                </div>
                            </div>
                        </div>

                        <c:if test="${empty cart or cart.cartSize == 0}">
                            <div class="row" style="justify-content: center;">
                                <div class="col l-8 m-10 c-12">
                                    <div class="empty-cart-container">
                                        <div class="empty-cart-icon"><i class="ti-bag"></i></div>
                                        <h2 class="empty-cart-title">Giỏ hàng của bạn đang trống</h2>
                                        <p class="empty-cart-desc">Có vẻ như bạn chưa chọn được sản phẩm nào ưng ý.</p>
                                        <a href="${pageContext.request.contextPath}/" class="btn btn--primary"
                                            style="height: 42px; border-radius: 4px;">
                                            <i class="ti-arrow-left" style="margin-right: 8px;"></i> Tiếp tục mua sắm
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty cart and cart.cartSize > 0}">
                            <div class="row">
                                <div class="col l-8 m-12 c-12">
                                    <div class="cart-list-container">
                                        <div class="cart-list__header hide-on-mobile">
                                            <div class="row no-gutters">
                                                <div class="col l-5 m-5 c-12">Sản phẩm</div>
                                                <div class="col l-2 m-2 c-0">Đơn giá</div>
                                                <div class="col l-3 m-3 c-0" style="text-align: center;">Số lượng</div>
                                                <div class="col l-2 m-2 c-0" style="text-align: right;">Thành tiền</div>
                                            </div>
                                        </div>

                                        <c:forEach items="${cart.cartItems}" var="item">
                                            <div class="cart-item">
                                                <div class="row no-gutters">
                                                    <div class="col l-5 m-5 c-12">
                                                        <div class="cart-item__product">
                                                            <img src="${pageContext.request.contextPath}${item.product.imageURL}"
                                                                alt="Ảnh" class="cart-item__img">
                                                            <div class="cart-item__info">
                                                                <h3 class="cart-item__name">${item.product.productName}
                                                                </h3>
                                                                <p class="cart-item__desc">Mã SP:
                                                                    ${item.product.productID}</p>
                                                                <a href="${pageContext.request.contextPath}/remove-from-cart?productID=${item.product.productID}"
                                                                    class="cart-item__remove-link">
                                                                    <i class="ti-trash" style="margin-right: 4px;"></i>
                                                                    Xóa
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col l-2 m-2 c-0 hide-on-mobile">
                                                        <div class="cart-item__price">
                                                            <fmt:formatNumber value="${item.product.price}"
                                                                type="currency" currencySymbol="₫" pattern="#,##0" />
                                                        </div>
                                                    </div>
                                                    <div class="col l-3 m-3 c-12">
                                                        <form action="${pageContext.request.contextPath}/update-cart"
                                                            method="GET" class="cart-item__quantity">
                                                            <input type="hidden" name="productID"
                                                                value="${item.product.productID}">
                                                            <input type="number" class="quantity-input" name="quantity"
                                                                value="${item.quantity}" min="1">
                                                            <button type="submit" class="btn btn--primary btn--small"><i
                                                                    class="ti-reload"></i></button>
                                                        </form>
                                                    </div>
                                                    <div class="col l-2 m-2 c-12">
                                                        <div class="cart-item__subtotal"
                                                            style="justify-content: flex-end;">
                                                            <fmt:formatNumber value="${item.subtotal}" type="currency"
                                                                currencySymbol="₫" pattern="#,##0" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <div class="cart-actions">
                                        <a href="${pageContext.request.contextPath}/" class="btn btn--secondary"
                                            style="border-radius: 4px;">
                                            <i class="ti-arrow-left" style="margin-right: 6px;"></i> Tiếp tục mua sắm
                                        </a>
                                        <a href="${pageContext.request.contextPath}/clear-cart" class="btn"
                                            style="color: #dc3545;">
                                            <i class="ti-trash" style="margin-right: 6px;"></i> Xóa toàn bộ giỏ hàng
                                        </a>
                                    </div>
                                </div>

                                <div class="col l-4 m-12 c-12">
                                    <div class="cart-summary-container">
                                        <h3 class="cart-summary__title">Tóm tắt đơn hàng</h3>
                                        <div class="cart-summary__row">
                                            <span>Tạm tính (${cart.totalQuantity} SP):</span>
                                            <span style="font-weight: 500;">
                                                <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                                    currencySymbol="₫" pattern="#,##0" />
                                            </span>
                                        </div>
                                        <div class="cart-summary__row">
                                            <span>Phí giao hàng:</span>
                                            <span style="color: var(--primary-color);">Miễn phí</span>
                                        </div>
                                        <div class="cart-summary__total">
                                            <span>Tổng cộng:</span>
                                            <span>
                                                <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                                    currencySymbol="₫" pattern="#,##0" />
                                            </span>
                                        </div>

                                        <c:if test="${not empty sessionScope.ACC}">
                                            <a href="${pageContext.request.contextPath}/checkout"
                                                class="btn btn--primary btn--full-width">
                                                Tiến hành đặt hàng
                                            </a>
                                        </c:if>
                                        <c:if test="${empty sessionScope.ACC}">
                                            <a href="${pageContext.request.contextPath}/login.jsp"
                                                class="btn btn--primary btn--full-width">
                                                Đăng nhập để đặt hàng
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </body>

            </html>