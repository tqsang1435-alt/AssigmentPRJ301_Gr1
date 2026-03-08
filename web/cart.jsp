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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

    <style>
        body { background-color: #f5f5f5; }
        .container { padding: 40px 0 60px; min-height: 80vh; }
        
        .main-title { margin-bottom: 25px; }
        .main-title h1 { display: inline-block; font-size: 2.8rem; margin: 0 0 5px 10px; color: var(--text-color); }
        .main-title i { font-size: 2.8rem; color: var(--primary-color); }
        .main-title p { font-size: 1.5rem; color: #666; margin-left: 45px; margin-top: 0; }

        .empty-cart-container { background: #fff; padding: 60px 20px; text-align: center; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .empty-cart-icon i { font-size: 8rem; color: #ddd; margin-bottom: 20px; display: inline-block; }
        .empty-cart-title { font-size: 2.2rem; color: var(--text-color); margin-bottom: 10px; }
        .empty-cart-desc { font-size: 1.5rem; color: #777; margin-bottom: 30px; }

        .cart-list-container { background: #fff; border-radius: 8px; padding: 20px 25px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 20px; }
        .cart-list__header { font-size: 1.4rem; font-weight: 600; color: #555; text-transform: uppercase; border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }
        
        .cart-item { padding-bottom: 20px; margin-bottom: 20px; border-bottom: 1px solid #eee; }
        .cart-item:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
        .cart-item__product { display: flex; align-items: center; }
        .cart-item__img { width: 80px; height: 80px; object-fit: contain; border: 1px solid #eee; border-radius: 4px; margin-right: 15px; }
        .cart-item__name { font-size: 1.6rem; color: var(--text-color); margin: 0 0 5px; font-weight: 500; }
        .cart-item__desc { font-size: 1.3rem; color: #888; margin: 0 0 10px; }
        .cart-item__remove-link { font-size: 1.3rem; color: #dc3545; text-decoration: none; display: inline-flex; align-items: center; transition: 0.2s; }
        .cart-item__remove-link:hover { opacity: 0.7; text-decoration: underline; }
        .cart-item__price, .cart-item__subtotal { font-size: 1.6rem; font-weight: 600; color: var(--primary-color); display: flex; align-items: center; height: 100%; }
        
        .cart-item__quantity { display: flex; align-items: center; height: 100%; gap: 10px; }
        .quantity-input { width: 60px; height: 32px; text-align: center; border: 1px solid var(--border-color); border-radius: 4px; font-size: 1.4rem; outline: none; }
        .quantity-input:focus { border-color: var(--primary-color); }
        
        .cart-summary-container { background: #fff; border-radius: 8px; padding: 25px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 80px; }
        .cart-summary__title { font-size: 1.8rem; margin: 0 0 20px; color: var(--text-color); font-weight: 600; border-bottom: 1px solid #eee; padding-bottom: 15px; }
        .cart-summary__row { display: flex; justify-content: space-between; font-size: 1.5rem; color: #555; margin-bottom: 15px; }
        .cart-summary__total { display: flex; justify-content: space-between; font-size: 1.8rem; font-weight: bold; color: var(--primary-color); margin: 20px 0; border-top: 1px solid #eee; padding-top: 20px; }
        
        .cart-actions { display: flex; justify-content: space-between; margin-top: 20px; }
        .btn--full-width { width: 100%; height: 44px; font-size: 1.6rem; text-transform: uppercase; font-weight: bold; border-radius: 4px; }
        .btn--small { height: 32px; font-size: 1.3rem; padding: 0 10px; border-radius: 4px; }
        .btn--secondary { border: 1px solid var(--primary-color); color: var(--primary-color); background: transparent; }
        .btn--secondary:hover { background: var(--primary-color); color: #fff; }
    </style>
</head>

<body>
    <header class="header">
        <div class="grid wide">
            <div class="header__navbar">
                <a href="${pageContext.request.contextPath}/" class="header__logo">
                    <i class="ti-mobile"></i> <label>PhoneShop</label>
                </a>

                <div class="header__search hide-on-mobile">
                    <input type="text" class="header__search-input" placeholder="Tìm kiếm điện thoại, laptop, phụ kiện...">
                    <button class="header__search-btn"><i class="ti-search"></i></button>
                </div>

                <ul class="header__nav-list">
                    <li class="header__nav-item">
                        <a href="${pageContext.request.contextPath}/view-cart" class="header__nav-link header__nav-link--warning">
                            <i class="ti-shopping-cart"></i> <label class="hide-on-mobile">Giỏ hàng</label>
                            <span style="background: var(--gold-color); color: var(--black-color); padding: 2px 6px; border-radius: 50%; font-size: 1.2rem; margin-left: 5px; font-weight: bold;">
                                ${cart.totalQuantity > 0 ? cart.totalQuantity : '0'}
                            </span>
                        </a>
                    </li>
                    <c:if test="${not empty sessionScope.ACC}">
                        <li class="header__nav-item">
                            <a href="${pageContext.request.contextPath}/user-profile" class="header__nav-link">
                                <i class="ti-user"></i> <label class="hide-on-mobile">Tài khoản</label>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${empty sessionScope.ACC}">
                        <li class="header__nav-item">
                            <a href="${pageContext.request.contextPath}/user-login" class="header__nav-link">
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
                            <a href="${pageContext.request.contextPath}/" class="btn btn--primary" style="height: 42px; border-radius: 4px;">
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
                                                <img src="${pageContext.request.contextPath}${item.product.imageURL}" alt="Ảnh" class="cart-item__img">
                                                <div class="cart-item__info">
                                                    <h3 class="cart-item__name">${item.product.productName}</h3>
                                                    <p class="cart-item__desc">Mã SP: ${item.product.productID}</p>
                                                    <a href="${pageContext.request.contextPath}/remove-from-cart?productID=${item.product.productID}" class="cart-item__remove-link">
                                                        <i class="ti-trash" style="margin-right: 4px;"></i> Xóa
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col l-2 m-2 c-0 hide-on-mobile">
                                            <div class="cart-item__price">
                                                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₫" pattern="#,##0" />
                                            </div>
                                        </div>
                                        <div class="col l-3 m-3 c-12">
                                            <form action="${pageContext.request.contextPath}/update-cart" method="GET" class="cart-item__quantity">
                                                <input type="hidden" name="productID" value="${item.product.productID}">
                                                <input type="number" class="quantity-input" name="quantity" value="${item.quantity}" min="1">
                                                <button type="submit" class="btn btn--primary btn--small"><i class="ti-reload"></i></button>
                                            </form>
                                        </div>
                                        <div class="col l-2 m-2 c-12">
                                            <div class="cart-item__subtotal" style="justify-content: flex-end;">
                                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" pattern="#,##0" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <div class="cart-actions">
                            <a href="${pageContext.request.contextPath}/" class="btn btn--secondary" style="border-radius: 4px;">
                                <i class="ti-arrow-left" style="margin-right: 6px;"></i> Tiếp tục mua sắm
                            </a>
                            <a href="${pageContext.request.contextPath}/clear-cart" class="btn" style="color: #dc3545;">
                                <i class="ti-trash" style="margin-right: 6px;"></i> Xóa toàn bộ giỏ hàng
                            </a>
                        </div>
                    </div>

                    <div class="col l-4 m-12 c-12">
                        <div class="cart-summary-container">
                            <h3 class="cart-summary__title">Tóm tắt đơn hàng</h3>
                            <div class="cart-summary__row">
                                <span>Tạm tính (${cart.totalQuantity} SP):</span>
                                <span style="font-weight: 500;"><fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="₫" pattern="#,##0" /></span>
                            </div>
                            <div class="cart-summary__row">
                                <span>Phí giao hàng:</span>
                                <span style="color: var(--primary-color);">Miễn phí</span>
                            </div>
                            <div class="cart-summary__total">
                                <span>Tổng cộng:</span>
                                <span><fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="₫" pattern="#,##0" /></span>
                            </div>
                            
                            <c:if test="${not empty sessionScope.ACC}">
                                <a href="${pageContext.request.contextPath}/checkout" class="btn btn--primary btn--full-width">
                                    Tiến hành đặt hàng
                                </a>
                            </c:if>
                            <c:if test="${empty sessionScope.ACC}">
                                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn--primary btn--full-width">
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