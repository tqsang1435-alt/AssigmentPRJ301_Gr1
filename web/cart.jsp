<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <fmt:setLocale value="vi_VN" />
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

            <body>
                <jsp:include page="header.jsp" />

                <div class="container">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12 m-12 c-12">
                                <div class="main-title">
                                    <i class="ti-shopping-cart-full"></i>
                                    <h1>Giỏ hàng của bạn</h1>
                                    <p>Kiểm tra và hoàn tất đơn hàng để nhận ngàn ưu đãi.</p>
                                </div>
                                
                                <c:if test="${not empty error}">
                                    <div class="error-message" style="color: red; background: #ffe6e6; padding: 10px; border: 1px solid #ffcccc; border-radius: 4px; margin-bottom: 20px;">
                                        <i class="ti-alert" style="margin-right: 8px;"></i>
                                        ${error}
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty sessionScope.errorMessage}">
                                    <div class="error-message" style="color: red; background: #ffe6e6; padding: 10px; border: 1px solid #ffcccc; border-radius: 4px; margin-bottom: 20px;">
                                        <i class="ti-alert" style="margin-right: 8px;"></i>
                                        ${sessionScope.errorMessage}
                                    </div>
                                    <c:remove var="errorMessage" scope="session" />
                                </c:if>
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
                                                            <div class="cart-item__checkbox" style="margin-bottom: 10px;">
                                                                <input type="checkbox" 
                                                                       id="select-${item.product.productID}" 
                                                                       ${item.selected ? 'checked' : ''} 
                                                                       onchange="toggleSelect(${item.product.productID})">
                                                                <label for="select-${item.product.productID}" style="margin-left: 8px; cursor: pointer;">Chọn để mua</label>
                                                            </div>
                                                            <img src="${item.product.imageURL}" alt="Ảnh"
                                                                class="cart-item__img">
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
                                                                pattern="#,##0" /> đ
                                                        </div>
                                                    </div>
                                                    <div class="col l-3 m-3 c-12">
                                                        <form action="${pageContext.request.contextPath}/update-cart"
                                                            method="GET" class="cart-item__quantity" style="display: flex; align-items: center; border: 1px solid #ddd; border-radius: 4px; overflow: hidden; width: max-content;">
                                                            <input type="hidden" name="productID"
                                                                value="${item.product.productID}">
                                                                
                                                            <button type="button" 
                                                                    onclick="var input = this.nextElementSibling; if(input.value > 1) { input.value--; input.form.submit(); }" 
                                                                    style="width: 30px; height: 30px; background: #f5f5f5; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; font-size: 1.2rem; color: #555; transition: background 0.2s;">
                                                                <i class="ti-minus"></i>
                                                            </button>
                                                            
                                                            <input type="number" class="quantity-input" name="quantity"
                                                                value="${item.quantity}" min="1" 
                                                                onchange="this.form.submit()"
                                                                style="width: 40px; height: 30px; text-align: center; border: none; border-left: 1px solid #ddd; border-right: 1px solid #ddd; outline: none; font-size: 1.4rem; padding: 0;">
                                                                
                                                            <button type="button" 
                                                                    onclick="var input = this.previousElementSibling; input.value++; input.form.submit();" 
                                                                    style="width: 30px; height: 30px; background: #f5f5f5; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; font-size: 1.2rem; color: #555; transition: background 0.2s;">
                                                                <i class="ti-plus"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                    <div class="col l-2 m-2 c-12">
                                                        <div class="cart-item__subtotal"
                                                            style="justify-content: flex-end;">
                                                            <fmt:formatNumber value="${item.subtotal}"
                                                                pattern="#,##0" /> đ
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
                                                <fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0" /> đ
                                            </span>
                                        </div>
                                        <c:if test="${cart.discountPercent > 0}">
                                            <div class="cart-summary__row">
                                                <span>Giảm giá hạng thành viên (
                                                    <fmt:formatNumber value="${cart.discountPercent}" pattern="#,##0" />
                                                    %):
                                                </span>
                                                <span style="color: #28a745;">
                                                    -
                                                    <fmt:formatNumber value="${cart.totalPrice - cart.finalTotalPrice}"
                                                        pattern="#,##0" /> đ
                                                </span>
                                            </div>
                                        </c:if>
                                        <div class="cart-summary__row">
                                            <span>Phí giao hàng:</span>
                                            <span style="color: var(--primary-color);">Miễn phí</span>
                                        </div>
                                        <div class="cart-summary__total">
                                            <span>Tổng cộng:</span>
                                            <span>
                                                <fmt:formatNumber value="${cart.finalTotalPrice}" pattern="#,##0" /> đ
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

                <jsp:include page="footer.jsp" />

                <script>
                    function toggleSelect(productID) {
                        window.location.href = '${pageContext.request.contextPath}/toggle-select?productID=' + productID;
                    }
                </script>
            </body>

            </html>