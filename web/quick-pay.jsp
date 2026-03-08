<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán đơn hàng | PHONEShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* ẨN SỐ 1 TRÊN NAVBAR KHI TRONG TRANG THANH TOÁN */
        .navbar .badge, .navbar .rounded-pill { display: none !important; }
        
        .qr-card { max-width: 500px; border-radius: 20px; border: none; }
        .price-highlight { color: #d63031; font-size: 2.2rem; font-weight: 800; }
        .qr-box { background: white; border: 1px solid #eee; padding: 15px; border-radius: 10px; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="navbar.jsp"/>
    
    <div class="container mt-5">
        <div class="card qr-card shadow mx-auto">
            <div class="card-body p-5 text-center">
                <h3 class="fw-bold mb-4">THANH TOÁN ĐƠN HÀNG</h3>

                <c:choose>
                    <c:when test="${payMode == 'cart'}">
                        <p class="text-muted mb-1">Thanh toán giỏ hàng (${sessionScope.cartCount} sản phẩm)</p>
                        <h2 class="price-highlight mb-4">
                            <fmt:formatNumber value="${sessionScope.totalMoney}" groupingUsed="true"/> ₫
                        </h2>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted mb-1">Sản phẩm: <b>${quickProduct.productName}</b></p>
                        <h2 class="price-highlight mb-4">
                            <fmt:formatNumber value="${quickProduct.price}" groupingUsed="true"/> ₫
                        </h2>
                    </c:otherwise>
                </c:choose>

                <div class="qr-box mb-4">
                    <img src="images/z7584199724359_2ba6e8eb185f11c1be664a43d11653ad.jpg" 
                         alt="Mã QR" class="img-fluid" style="max-height: 250px;">
                </div>

                <p class="small text-muted mb-4">Vui lòng quét mã QR để hoàn tất thanh toán.</p>

                <div class="d-grid gap-2">
                    <form action="checkout" method="POST">
                        <input type="hidden" name="action" value="confirm">
                        <input type="hidden" name="payMode" value="${payMode}">
                        <c:if test="${payMode == 'single'}">
                            <input type="hidden" name="productId" value="${quickProduct.productID}">
                        </c:if>
                        <button type="submit" class="btn btn-warning rounded-pill fw-bold py-2 w-100">
                            Xác nhận đã chuyển tiền
                        </button>
                    </form>

                    <form action="checkout" method="POST">
                        <input type="hidden" name="action" value="cancel">
                        <button type="submit" class="btn btn-link text-muted text-decoration-none small w-100">
                            Quay lại trang chủ (Hủy đơn)
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>