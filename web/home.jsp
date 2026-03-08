<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ | PHONEShop</title>
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Inter', sans-serif;
                padding-bottom: 120px; /* Chừa chỗ cho Fixed Footer */
            }

            /* Container chứa 2 nút bấm ở dưới cùng */
            .action-buttons {
                display: flex;
                gap: 8px;          /* Khoảng cách giữa 2 nút */
                width: 100%;
                margin-top: 10px;
            }

            /* Định dạng chung để 2 nút bằng chiều cao và bo góc viên thuốc */
            .btn-action {
                display: flex;
                align-items: center;
                justify-content: center;
                height: 40px;       /* Chiều cao cố định chuẩn 40px */
                border-radius: 20px; /* Bo tròn hoàn toàn */
                font-weight: 600;
                font-size: 14px;
                text-decoration: none;
                transition: all 0.3s ease;
                border: none;
            }

            /* Nút Giỏ - Trắng viền xám */
            .btn-cart-outline {
                flex: 0 0 40%; /* Chiếm 40% chiều ngang */
                background-color: #fff;
                color: #495057;
                border: 1px solid #ced4da !important;
            }

            .btn-cart-outline i {
                margin-right: 5px;
            }

            /* Nút Mua ngay - Vàng */
            .btn-buy-now {
                flex: 1; /* Tự động lấp đầy phần còn lại */
                background-color: #ffc107; 
                color: #000;
            }

            /* Hiệu ứng hover cho nút */
            .btn-cart-outline:hover { background-color: #f8f9fa; border-color: #adb5bd !important; }
            .btn-buy-now:hover { background-color: #ffca2c; color: #000; }

            /* Sidebar Danh mục */
            .category-sidebar {
                background: white;
                border-radius: 15px;
                padding: 20px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                position: sticky;
                top: 20px;
            }
            .sidebar-title {
                font-weight: 800;
                font-size: 1.1rem;
                color: #2d3436;
                text-transform: uppercase;
                border-bottom: 3px solid #ffc107;
                padding-bottom: 10px;
                margin-bottom: 20px;
                display: block;
            }
            .cat-list { list-style: none; padding: 0; }
            .cat-item a {
                text-decoration: none;
                color: #636e72;
                display: flex;
                align-items: center;
                padding: 12px 15px;
                border-radius: 10px;
                transition: 0.3s;
                font-weight: 500;
            }
            .cat-item a:hover {
                background: #fff9e6;
                color: #ffc107;
                transform: translateX(5px);
            }
            .cat-item a i { width: 25px; color: #ffc107; font-size: 1.2rem; }

            /* Submenu */
            .has-sub .submenu { display: none; list-style: none; padding-left: 35px; margin-top: 5px; }
            .has-sub:hover .submenu { display: block; }
            .submenu a { font-size: 0.9rem; padding: 8px 0; color: #b2bec3; text-decoration: none; display: block; }
            .submenu a:hover { color: #ffc107; }

            /* Card Sản phẩm */
            .product-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
                background: white;
                overflow: hidden;
            }
            .product-card:hover { transform: translateY(-10px); box-shadow: 0 10px 30px rgba(0,0,0,0.1); }
            .img-container { height: 200px; padding: 20px; display: flex; align-items: center; justify-content: center; }
            .img-container img { max-width: 100%; max-height: 100%; object-fit: contain; }
            .product-name { font-size: 1rem; font-weight: 700; color: #2d3436; height: 40px; overflow: hidden; }
            .product-price { color: #d63031; font-size: 1.2rem; font-weight: 800; }

            /* Nút Chi tiết (Nằm trên hàng nút action) */
            .btn-detail-custom {
                border-radius: 50px;
                font-weight: 600;
                font-size: 0.85rem;
                padding: 8px 15px;
                transition: 0.3s;
                text-decoration: none;
                text-align: center;
                border: 1px solid #dfe6e9;
                color: #636e72;
                background: white;
                display: block;
            }
            .btn-detail-custom:hover { background: #f8f9fa; }

            /* Footer Fixed */
            .fixed-footer-wrapper {
                position: fixed;
                bottom: 0;
                left: 0;
                width: 100%;
                z-index: 9999;
                background: #1a1a1a;
                border-top: 3px solid #ffc107;
                box-shadow: 0 -5px 15px rgba(0,0,0,0.2);
            }
        </style>
    </head>
    <body>

        <jsp:include page="navbar.jsp"/>

        <main class="container-fluid px-lg-5 py-4">
            <div class="row g-4">

                <div class="col-lg-3 col-md-4">
                    <div class="category-sidebar">
                        <span class="sidebar-title"><i class="fa-solid fa-layer-group me-2"></i> Danh mục</span>
                        <ul class="cat-list">
                            <li class="cat-item"><a href="home"><i class="fa-solid fa-border-all"></i> Tất cả sản phẩm</a></li>
                            <li class="cat-item"><a href="home?cid=1"><i class="fa-solid fa-mobile-screen-button"></i> Điện thoại</a></li>
                            <li class="cat-item has-sub">
                                <a href="#"><i class="fa-solid fa-headset"></i> Phụ kiện <i class="fa-solid fa-chevron-down ms-auto" style="font-size: 0.7rem;"></i></a>
                                <ul class="submenu">
                                    <li><a href="home?cid=2">Tai nghe</a></li>
                                    <li><a href="home?cid=3">Sạc dự phòng</a></li>
                                    <li><a href="home?cid=4">Cáp sạc</a></li>
                                    <li><a href="home?cid=5">Ốp lưng</a></li>
                                    <li><a href="home?cid=6">Loa Bluetooth</a></li>
                                    <li><a href="home?cid=7">Đồng hồ thông minh</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="col-lg-9 col-md-8">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h4 class="fw-bold text-dark m-0">Sản phẩm nổi bật</h4>
                        <span class="text-muted small">Hiển thị ${listP.size()} kết quả</span>
                    </div>

                    <div class="row g-4">
                        <c:forEach items="${listP}" var="p">
                            <div class="col-xl-4 col-sm-6">
                                <div class="product-card shadow-sm h-100 p-3">
                                    <div class="img-container">
                                        <img src="${p.imageURL}" alt="${p.productName}">
                                    </div>
                                    <div class="card-body p-0 text-center">
                                        <h6 class="product-name mt-2 mb-2">${p.productName}</h6>
                                        <p class="product-price mb-3">
                                            <fmt:formatNumber value="${p.price}" groupingUsed="true"/> ₫
                                        </p>
                                        
                                        <a href="detail?id=${p.productID}" class="btn-detail-custom mb-2">Chi tiết</a>
                                        
                                        <div class="action-buttons">
                                            <a href="javascript:void(0)" 
                                               onclick="confirmAddCart('${p.productID}', '${p.productName}')" 
                                               class="btn-action btn-cart-outline">
                                                <i class="fa-solid fa-cart-shopping"></i> Giỏ
                                            </a>

                                            <a href="javascript:void(0)" 
                                               onclick="confirmBuyNow('${p.productID}', '${p.productName}')" 
                                               class="btn-action btn-buy-now">
                                                Mua ngay
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${listP.size() == 0}">
                            <div class="col-12 text-center py-5">
                                <i class="fa-regular fa-face-frown display-1 text-muted mb-3"></i>
                                <h5>Rất tiếc, không tìm thấy sản phẩm nào!</h5>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </main>

        <div class="fixed-footer-wrapper">
            <jsp:include page="footer.jsp"/>
        </div>

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
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>