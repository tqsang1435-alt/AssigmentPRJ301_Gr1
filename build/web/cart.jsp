<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giỏ hàng của bạn | PHONEShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Inter', sans-serif;
                padding-bottom: 150px;
            }
            .cart-container {
                background: white;
                border-radius: 15px;
                padding: 30px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                margin-top: 30px;
            }
            .cart-header {
                border-bottom: 2px solid #ffc107;
                padding-bottom: 15px;
                margin-bottom: 25px;
            }
            .product-img {
                width: 80px;
                height: 80px;
                object-fit: contain;
            }
            .btn-custom {
                border-radius: 50px;
                font-weight: 600;
                padding: 8px 20px;
                transition: 0.3s;
                text-decoration: none;
                display: inline-block;
            }
            .btn-checkout {
                background: #ffc107;
                color: black;
                border: none;
            }
            .btn-checkout:hover {
                background: #eab308;
                transform: translateY(-2px);
                color: black;
            }
            .btn-continue {
                border: 1px solid #dfe6e9;
                color: #636e72;
                background: white;
            }
            .btn-continue:hover {
                background: #f8f9fa;
                color: #333;
            }
            .fixed-footer-container {
                position: fixed;
                bottom: 0;
                left: 0;
                width: 100%;
                z-index: 9999;
                background: #1a1a1a;
                border-top: 3px solid #ffc107;
            }
        </style>
    </head>
    <body>

        <jsp:include page="navbar.jsp"/>

        <div class="container">
            <div class="cart-container">
                <div class="cart-header d-flex align-items-center">
                    <h3 class="fw-bold m-0 text-dark">
                        <i class="fa-solid fa-cart-shopping me-2 text-warning"></i> GIỎ HÀNG CỦA BẠN
                    </h3>
                </div>

                <%-- SỬA LOGIC KIỂM TRA: Dùng cartCount từ session cho dứt điểm --%>
                <c:choose>
                    <c:when test="${not empty sessionScope.cart and sessionScope.cartCount > 0}">
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th>Tên sản phẩm</th>
                                        <th>Giá</th>
                                        <th class="text-center">Số lượng</th>
                                        <th>Tổng cộng</th>
                                        <th>Xóa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%-- Lặp qua Map cart (key là ProductID, value là Quantity) --%>
                                    <c:forEach items="${sessionScope.cartItems}" var="item">
                                        <tr>
                                            <td>
                                                <img src="${item.product.imageURL}" class="product-img" alt="Product">
                                            </td>
                                            <td>
                                                <span class="fw-bold text-dark">${item.product.productName}</span>
                                            </td>
                                            <td>
                                                <span class="text-danger fw-bold">
                                                    <fmt:formatNumber value="${item.product.price}" groupingUsed="true"/> ₫
                                                </span>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center justify-content-center">
                                                    <a href="add-cart?id=${item.product.productID}&action=sub" class="btn btn-sm btn-outline-secondary px-2">-</a>
                                                    <span class="mx-3 fw-bold">${item.quantity}</span>
                                                    <a href="add-cart?id=${item.product.productID}&action=add" class="btn btn-sm btn-outline-secondary px-2">+</a>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="text-danger fw-bold">
                                                    <fmt:formatNumber value="${item.product.price * item.quantity}" groupingUsed="true"/> ₫
                                                </span>
                                            </td>
                                            <td>
                                                <a href="add-cart?id=${item.product.productID}&action=remove" class="text-muted text-decoration-none">
                                                    <i class="fa-solid fa-trash-can hover-danger"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="row mt-4 align-items-center">
                            <div class="col-md-6">
                                <a href="home" class="btn-custom btn-continue">
                                    <i class="fa-solid fa-arrow-left me-2"></i>Tiếp tục mua sắm
                                </a>
                            </div>
                            <%-- Tìm đoạn col-md-6 text-end và sửa nút bấm --%>
                            <div class="col-md-6 text-end">
                                <h4 class="fw-bold mb-3">Tổng tiền: 
                                    <span class="text-danger">
                                        <fmt:formatNumber value="${sessionScope.totalMoney}" groupingUsed="true"/> ₫
                                    </span>
                                </h4>
                                <a href="add-cart?action=buynow" class="btn-custom btn-checkout btn-lg px-5">THANH TOÁN NGAY</a>
                            </div>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fa-solid fa-cart-shopping display-1 text-muted mb-3" style="opacity: 0.2;"></i>
                            <h4 class="text-muted">Giỏ hàng của bạn đang trống!</h4>
                            <p>Hãy quay lại trang chủ để chọn sản phẩm ưng ý nhé.</p>
                            <a href="home" class="btn btn-warning rounded-pill px-4 mt-3 fw-bold">MUA SẮM NGAY</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="fixed-footer-container">
            <jsp:include page="footer.jsp"/>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>