<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${product.productName} | PhoneShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body { background-color: #f8f9fa; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        
        /* Container mở rộng đều 2 bên */
        .product-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            margin: 40px auto;
            max-width: 1200px;
            display: flex;
            overflow: hidden;
            min-height: 500px;
        }

        .image-container {
            flex: 1;
            padding: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #fff;
            border-right: 1px solid #f0f0f0;
        }
        .image-container img { max-width: 100%; max-height: 400px; object-fit: contain; }

        .info-container { flex: 1; padding: 50px; display: flex; flex-direction: column; justify-content: center; }

        .product-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: #212529;
            margin-bottom: 10px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .status-badge {
            background-color: #198754;
            color: white;
            padding: 5px 15px;
            border-radius: 50px;
            font-size: 0.85rem;
            font-weight: 600;
            display: inline-block;
            margin-bottom: 20px;
        }

        .price-text {
            color: #d63031;
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 25px;
        }

        .description-text { color: #636e72; line-height: 1.6; margin-bottom: 35px; }

        /* Nút bấm thiết kế hiện đại */
        .btn-group-action { display: flex; gap: 15px; }
        
        .btn-action-detail {
            flex: 1;
            font-weight: 700;
            padding: 12px;
            border-radius: 10px;
            text-align: center;
            text-decoration: none;
            transition: 0.3s;
            cursor: pointer;
            border: none;
        }

        .btn-add-cart-outline {
            border: 2px solid #ffc107 !important;
            color: #ffc107;
            background: white;
        }
        .btn-add-cart-outline:hover { background: #fff9e6; color: #ffc107; }

        .btn-buy-now-solid {
            background: #ffc107;
            color: #000;
        }
        .btn-buy-now-solid:hover { background: #eab308; color: #000; }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <jsp:include page="navbar.jsp"/>

    <main class="flex-grow-1 container">
        <div class="product-card">
            <div class="image-container">
                <img src="${product.imageURL}" alt="${product.productName}">
            </div>

            <div class="info-container">
                <h1 class="product-title">${product.productName}</h1>
                
                <div>
                    <span class="status-badge">
                        <i class="fa-solid fa-check-circle me-1"></i> Còn hàng
                    </span>
                </div>

                <div class="price-text">
                    <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/> ₫
                </div>

                <div class="description-text">
                    <h6 class="fw-bold text-dark">Mô tả sản phẩm:</h6>
                    <p>${product.description}</p>
                </div>

                <div class="btn-group-action">
                    <a href="javascript:void(0)" 
                       onclick="confirmAddCart('${product.productID}', '${product.productName}')" 
                       class="btn-action-detail btn-add-cart-outline">
                        <i class="fa-solid fa-cart-plus me-2"></i>Thêm giỏ hàng
                    </a>
                    
                    <a href="javascript:void(0)" 
                       onclick="confirmBuyNow('${product.productID}', '${product.productName}')" 
                       class="btn-action-detail btn-buy-now-solid">
                        Mua ngay
                    </a>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>

    <script>
        function confirmAddCart(id, name) {
            Swal.fire({
                title: 'Thêm vào giỏ hàng?',
                text: "Bạn muốn thêm " + name + " vào giỏ chứ?",
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
                text: "Hệ thống sẽ chuyển đến trang thanh toán cho " + name,
                icon: 'info',
                showCancelButton: true,
                confirmButtonColor: '#ffc107',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Thanh toán ngay',
                cancelButtonText: 'Để sau'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Gọi action buynow để hiện QR và giá tiền
                    window.location.href = "add-cart?id=" + id + "&action=buynow";
                }
            })
        }
    </script>

</body>
</html>