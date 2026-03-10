<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <fmt:setLocale value="vi_VN" />

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>${product.productName} | PhoneShop</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

            </head>

            <body>

                <jsp:include page="header.jsp" />

                <div class="app__container" style="background-color: #f5f5f5; padding: 40px 0;">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12 m-12 c-12">
                                <div class="product-detail-container">
                                    <div class="row">
                                        <!-- Product Image -->
                                        <div class="col l-5 m-6 c-12">
                                            <div class="product-img-box"
                                                style="background-image: url('${not empty product.imageURL ? product.imageURL : 'assets/img/default-phone.png'}');">
                                            </div>
                                        </div>

                                        <!-- Product Info -->
                                        <div class="col l-7 m-6 c-12">
                                            <div class="product-info">
                                                <h1 class="product-name">${product.productName}</h1>

                                                <div class="product-price">
                                                    <fmt:formatNumber value="${product.price}" pattern="#,##0" /> đ
                                                </div>

                                                <div class="product-meta">
                                                    <i class="ti-check-box"></i> Tình trạng:
                                                    <span
                                                        style="color: ${product.stockQuantity > 0 ? '#28a745' : '#dc3545'};">
                                                        ${product.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}
                                                    </span>
                                                </div>
                                                <div class="product-meta">
                                                    <i class="ti-tag"></i> Mã sản phẩm:
                                                    <span>#${product.productID}</span>
                                                </div>

                                                <c:if test="${not empty product.RAM}">
                                                    <div class="product-meta"><i class="ti-harddrives"></i> RAM:
                                                        <span>${product.RAM}</span>
                                                    </div>
                                                </c:if>
                                                <c:if test="${not empty product.ROM}">
                                                    <div class="product-meta"><i class="ti-save"></i> Bộ nhớ trong:
                                                        <span>${product.ROM}</span>
                                                    </div>
                                                </c:if>
                                                <c:if test="${not empty product.color}">
                                                    <div class="product-meta"><i class="ti-palette"></i> Màu sắc:
                                                        <span>${product.color}</span>
                                                    </div>
                                                </c:if>

                                                <div class="action-buttons">
                                                    <button
                                                        onclick="confirmAddCart('${product.productID}', '${product.productName}')"
                                                        class="btn-action btn-add-cart">
                                                        <i class="ti-shopping-cart" style="margin-right: 8px;"></i> Thêm
                                                        vào giỏ
                                                    </button>
                                                    <button
                                                        onclick="confirmBuyNow('${product.productID}', '${product.productName}')"
                                                        class="btn-action btn-buy-now">
                                                        Mua ngay
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Description -->
                                    <div class="row">
                                        <div class="col l-12">
                                            <div class="product-desc-box">
                                                <h3 class="product-desc-title">Mô tả sản phẩm</h3>
                                                <div class="product-desc-content">
                                                    ${product.description}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <jsp:include page="footer.jsp" />

                <script>
                    function confirmAddCart(id, name) {
                        Swal.fire({
                            title: 'Thêm vào giỏ hàng?',
                            text: "Bạn muốn thêm " + name + " vào giỏ hàng chứ?",
                            icon: 'question',
                            showCancelButton: true,
                            confirmButtonColor: '#ffc107',
                            cancelButtonColor: '#6c757d',
                            confirmButtonText: 'Đúng, thêm ngay!',
                            cancelButtonText: 'Hủy'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "add-cart?id=" + id + "&action=add";
                            }
                        })
                    }

                    function confirmBuyNow(id, name) {
                        Swal.fire({
                            title: 'Xác nhận mua ngay?',
                            text: "Bạn sẽ được chuyển đến trang thanh toán cho " + name,
                            icon: 'info',
                            showCancelButton: true,
                            confirmButtonColor: '#ffc107',
                            cancelButtonColor: '#6c757d',
                            confirmButtonText: 'Thanh toán ngay',
                            cancelButtonText: 'Để sau'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "add-cart?id=" + id + "&action=buynow";
                            }
                        })
                    }
                </script>
            </body>

            </html>