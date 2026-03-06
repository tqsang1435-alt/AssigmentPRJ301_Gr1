<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - PhoneShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    <style>
        .cart-page {
            padding: 40px 0;
            min-height: 70vh;
        }

        .cart-title {
            text-align: center;
            margin-bottom: 40px;
        }

        .cart-title h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }

        .cart-container {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
        }

        .cart-items {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .cart-item {
            display: grid;
            grid-template-columns: 100px 1fr 100px 100px 50px;
            gap: 20px;
            align-items: center;
            padding: 20px 0;
            border-bottom: 1px solid #eee;
        }

        .cart-item:last-child {
            border-bottom: none;
        }

        .cart-item-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 4px;
        }

        .cart-item-info h3 {
            font-size: 16px;
            margin-bottom: 5px;
        }

        .cart-item-info p {
            font-size: 14px;
            color: #666;
        }

        .cart-item-price {
            text-align: center;
            font-weight: bold;
        }

        .cart-item-quantity {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .quantity-input {
            width: 50px;
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
        }

        .cart-item-subtotal {
            text-align: right;
            font-weight: bold;
            color: #e74c3c;
        }

        .cart-item-remove {
            text-align: center;
        }

        .remove-btn {
            background: none;
            border: none;
            color: #e74c3c;
            cursor: pointer;
            font-size: 18px;
            padding: 5px;
        }

        .remove-btn:hover {
            color: #c0392b;
        }

        .cart-summary {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            height: fit-content;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .summary-row:last-child {
            border-bottom: none;
            font-size: 18px;
            font-weight: bold;
            color: #e74c3c;
            margin-top: 10px;
        }

        .empty-cart {
            text-align: center;
            padding: 60px 20px;
            grid-column: 1 / -1;
        }

        .empty-cart-icon {
            font-size: 64px;
            color: #ddd;
            margin-bottom: 20px;
        }

        .empty-cart h2 {
            font-size: 24px;
            margin-bottom: 10px;
        }

        .empty-cart p {
            color: #666;
            margin-bottom: 30px;
        }

        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: 0.3s;
        }

        .btn-primary {
            background: #e74c3c;
            color: white;
        }

        .btn-primary:hover {
            background: #c0392b;
        }

        .btn-secondary {
            background: #95a5a6;
            color: white;
        }

        .btn-secondary:hover {
            background: #7f8c8d;
        }

        .btn-update {
            background: #3498db;
            color: white;
            padding: 5px 10px;
            font-size: 12px;
        }

        .btn-update:hover {
            background: #2980b9;
        }

        @media (max-width: 768px) {
            .cart-container {
                grid-template-columns: 1fr;
            }

            .cart-item {
                grid-template-columns: 80px 1fr;
                gap: 15px;
            }

            .cart-item-price,
            .cart-item-subtotal {
                display: none;
            }

            .cart-item-info {
                grid-column: 2;
            }

            .btn-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>

<body>
    <header class="header">
        <div class="grid wide">
            <div class="header__navbar">
                <a href="${pageContext.request.contextPath}/" class="header__logo">
                    <i class="ti-mobile"></i><label>PhoneShop</label>
                </a>
                <ul class="header__nav-list">
                    <li class="header__nav-item"><a href="${pageContext.request.contextPath}/" class="header__nav-link">Trang
                            chủ</a></li>
                    <li class="header__nav-item"><a href="${pageContext.request.contextPath}/view-cart"
                            class="header__nav-link">Giỏ hàng</a></li>
                    <c:if test="${not empty sessionScope.userID}">
                        <li class="header__nav-item"><a href="${pageContext.request.contextPath}/profile"
                                class="header__nav-link">Tài khoản</a></li>
                        <li class="header__nav-item"><a href="${pageContext.request.contextPath}/user-logout"
                                class="header__nav-link">Đăng xuất</a></li>
                    </c:if>
                    <c:if test="${empty sessionScope.userID}">
                        <li class="header__nav-item"><a href="${pageContext.request.contextPath}/login.jsp"
                                class="header__nav-link">Đăng nhập</a></li>
                        <li class="header__nav-item"><a href="${pageContext.request.contextPath}/register.jsp"
                                class="header__nav-link">Đăng ký</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </header>

    <div class="grid wide">
        <div class="cart-page">
            <div class="cart-title">
                <h1>Giỏ hàng của bạn</h1>
                <p>Quản lý các sản phẩm trong giỏ hàng</p>
            </div>

            <c:if test="${not empty cart and cart.cartSize > 0}">
                <div class="cart-container">
                    <div class="cart-items">
                        <c:forEach items="${cart.cartItems}" var="item">
                            <div class="cart-item">
                                <img src="${pageContext.request.contextPath}${item.product.imageURL}"
                                    alt="${item.product.productName}" class="cart-item-image">
                                <div class="cart-item-info">
                                    <h3>${item.product.productName}</h3>
                                    <p>Mã sản phẩm: ${item.product.productID}</p>
                                    <c:if test="${not empty item.product.color}">
                                        <p>Màu sắc: ${item.product.color}</p>
                                    </c:if>
                                </div>
                                <div class="cart-item-price">
                                    <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₫" pattern="###,###" />
                                </div>
                                <div class="cart-item-quantity">
                                    <form action="${pageContext.request.contextPath}/update-cart" method="GET"
                                        style="display: flex; gap: 5px;">
                                        <input type="hidden" name="productID" value="${item.product.productID}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1"
                                            class="quantity-input">
                                        <button type="submit" class="btn btn-update">Cập nhật</button>
                                    </form>
                                </div>
                                <div class="cart-item-subtotal">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" pattern="###,###" />
                                </div>
                                <div class="cart-item-remove">
                                    <a href="${pageContext.request.contextPath}/remove-from-cart?productID=${item.product.productID}"
                                        class="remove-btn" title="Xóa sản phẩm">
                                        <i class="ti-trash"></i>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="cart-summary">
                        <div class="summary-row">
                            <span>Số lượng sản phẩm:</span>
                            <span>${cart.totalQuantity}</span>
                        </div>
                        <div class="summary-row">
                            <span>Tổng tiền:</span>
                            <span>
                                <fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="₫" pattern="###,###" />
                            </span>
                        </div>

                        <div class="btn-group">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                                <i class="ti-angle-left"></i> Tiếp tục mua sắm
                            </a>
                            <a href="${pageContext.request.contextPath}/clear-cart" class="btn btn-secondary">
                                <i class="ti-trash"></i> Xóa giỏ hàng
                            </a>
                        </div>

                        <div class="btn-group" style="margin-top: 30px;">
                            <c:if test="${not empty sessionScope.userID}">
                                <button class="btn btn-primary" style="width: 100%;">
                                    <i class="ti-bag"></i> Thanh toán
                                </button>
                            </c:if>
                            <c:if test="${empty sessionScope.userID}">
                                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary"
                                    style="width: 100%;">
                                    <i class="ti-lock"></i> Đăng nhập để thanh toán
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty cart or cart.cartSize == 0}">
                <div class="cart-items">
                    <div class="empty-cart">
                        <div class="empty-cart-icon">
                            <i class="ti-bag"></i>
                        </div>
                        <h2>Giỏ hàng của bạn đang trống</h2>
                        <p>Hãy thêm một số sản phẩm vào giỏ hàng</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                            <i class="ti-angle-left"></i> Quay lại trang chủ
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <footer class="footer">
        <div class="grid wide">
            <p>&copy; 2024 PhoneShop. All rights reserved.</p>
        </div>
    </footer>
</body>

</html>
