<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .footer-ultra-mini {
        background-color: #1a1d20; /* Nền tối hơn để tạo cảm giác gọn */
        color: #95a5a6;
        padding: 10px 0; /* Giảm padding xuống mức tối thiểu */
        font-size: 0.75rem; /* Chữ nhỏ hơn xí nữa */
        border-top: 1px solid #ffc107; /* Đường kẻ mỏng hơn */
    }
    .footer-ultra-mini h6 {
        color: #ffc107;
        font-size: 0.8rem;
        font-weight: bold;
        margin-bottom: 5px; /* Sát lại gần nhau */
        text-transform: uppercase;
    }
    .footer-link-mini {
        color: #95a5a6;
        text-decoration: none;
        margin-right: 12px;
    }
    .footer-link-mini:hover { color: #ffc107; }
    .contact-item-mini { margin-bottom: 2px; }
    .contact-item-mini i { color: #ffc107; width: 14px; margin-right: 5px; }
    .copyright-tiny {
        border-top: 1px solid #2c3e50;
        margin-top: 8px;
        padding-top: 5px;
        font-size: 0.65rem;
        opacity: 0.7;
    }
</style>

<footer class="footer-ultra-mini mt-auto"> <div class="container">
        <div class="row align-items-start">
            <div class="col-md-3">
                <h6>PHONEShop</h6>
                <div class="d-flex gap-2 mt-1">
                    <a href="#" class="text-secondary"><i class="fa-brands fa-facebook-f"></i></a>
                    <a href="#" class="text-secondary"><i class="fa-brands fa-instagram"></i></a>
                    <a href="#" class="text-secondary"><i class="fa-brands fa-tiktok"></i></a>
                </div>
            </div>

            <div class="col-md-4">
                <h6>Liên kết nhanh</h6>
                <div class="d-flex flex-wrap">
                    <a href="home" class="footer-link-mini">Trang chủ</a>
                    <a href="profile.jsp" class="footer-link-mini">Hồ sơ</a>
                    <a href="cart" class="footer-link-mini">Giỏ hàng</a>
                    <a href="#" class="footer-link-mini">Chính sách</a>
                </div>
            </div>

            <div class="col-md-5">
                <h6>Liên hệ</h6>
                <div class="row g-0">
                    <div class="col-6 contact-item-mini">
                        <i class="fa-solid fa-location-dot"></i> Q. Ninh Kiều, Cần Thơ
                    </div>
                    <div class="col-6 contact-item-mini">
                        <i class="fa-solid fa-phone"></i> 0987 654 321
                    </div>
                    <div class="col-12 contact-item-mini">
                        <i class="fa-solid fa-envelope"></i> hotro@phoneshop.vn
                    </div>
                </div>
            </div>
        </div>

        <div class="text-center copyright-tiny">
            &copy; 2024 PHONEShop. Designed by Lenovo.
        </div>
    </div>
</footer>