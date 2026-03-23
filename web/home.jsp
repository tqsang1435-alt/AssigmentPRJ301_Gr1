<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@taglib prefix="fn" uri="jakarta.tags.functions" %>
                <fmt:setLocale value="vi_VN" />

                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Trang chủ | PHONEShop</title>

                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
                    <style>
                        /* SLIDER */
                        .my-slider {
                            position: relative;
                            width: 100%;
                            height: 450px;
                            overflow: hidden;
                            background-color: #000;
                        }

                        .my-slide {
                            position: absolute;
                            top: 0;
                            left: 0;
                            width: 100%;
                            height: 100%;
                            opacity: 0;
                            transition: opacity 0.8s ease-in-out;
                            z-index: 1;
                        }

                        .my-slide.active {
                            opacity: 1;
                            z-index: 2;
                        }

                        .my-slide img {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                            filter: brightness(0.65);
                        }

                        .my-slide__content {
                            position: absolute;
                            top: 50%;
                            left: 50%;
                            transform: translate(-50%, -50%);
                            color: #fff;
                            z-index: 3;
                            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
                            max-width: 600px;
                            text-align: center;
                            width: 100%;
                        }

                        .my-slide__content h2 {
                            font-size: 4rem;
                            font-weight: bold;
                            margin-bottom: 15px;
                            text-transform: uppercase;
                        }

                        .my-slide__content p {
                            font-size: 1.8rem;
                            margin-bottom: 25px;
                            line-height: 1.4;
                        }

                        .my-slide__btn {
                            display: inline-block;
                            padding: 12px 35px;
                            background-color: var(--primary-color, #ee4d2d);
                            color: #fff;
                            text-decoration: none;
                            font-size: 1.6rem;
                            border-radius: 30px;
                            font-weight: bold;
                            transition: background-color 0.3s;
                            text-shadow: none;
                        }

                        .my-slide__btn:hover {
                            background-color: #d73a1e;
                        }

                        /* Nút Prev / Next */
                        .slider-nav {
                            position: absolute;
                            top: 50%;
                            transform: translateY(-50%);
                            background: rgba(255, 255, 255, 0.2);
                            border: none;
                            width: 50px;
                            height: 50px;
                            border-radius: 50%;
                            color: white;
                            font-size: 2rem;
                            cursor: pointer;
                            z-index: 10;
                            transition: all 0.3s;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .slider-nav:hover {
                            background: var(--primary-color, #ee4d2d);
                        }

                        .slider-nav.prev {
                            left: 20px;
                        }

                        .slider-nav.next {
                            right: 20px;
                        }

                        /* Chấm tròn Dots */
                        .slider-dots {
                            position: absolute;
                            bottom: 20px;
                            left: 50%;
                            transform: translateX(-50%);
                            display: flex;
                            gap: 12px;
                            z-index: 10;
                        }

                        .dot {
                            width: 14px;
                            height: 14px;
                            border-radius: 50%;
                            background: rgba(255, 255, 255, 0.5);
                            cursor: pointer;
                            transition: all 0.3s;
                        }

                        .dot.active {
                            background: var(--primary-color, #ee4d2d);
                            transform: scale(1.2);
                        }

                        .btn-add-cart-custom {
                            flex: 0 0 42px;
                            height: 36px;
                            font-size: 1.8rem;
                            background-color: rgba(238, 77, 43, 0.08);
                            color: var(--primary-color, #ee4d2d);
                            border: 1px solid var(--primary-color, #ee4d2d);
                            border-radius: 4px;
                            cursor: pointer;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            transition: all 0.2s;
                        }

                        .btn-add-cart-custom:hover {
                            background-color: var(--primary-color, #ee4d2d);
                            color: #fff;
                        }

                        .btn-buy-now-custom {
                            flex: 1;
                            height: 36px;
                            font-size: 1.3rem;
                            background-color: var(--primary-color, #ee4d2d);
                            color: #fff;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            text-transform: uppercase;
                            font-weight: 600;
                            transition: opacity 0.2s;
                        }

                        .btn-buy-now-custom:hover {
                            opacity: 0.9;
                        }

                        .pagination-item__link {
                            text-decoration: none !important;
                        }

                        select.filter-select {
                            padding: 8px 12px;
                            font-size: 1.4rem;
                            border-radius: 4px;
                            border: 1px solid #ccc;
                            outline: none;
                            cursor: pointer;
                            background-color: #fff;
                        }
                    </style>
                </head>

                <body>

                    <jsp:include page="header.jsp" />

                    <div class="my-slider">
                        <div class="my-slide active">
                            <img src="https://images.unsplash.com/photo-1605236453806-6ff36851218e?q=80&w=1920&auto=format&fit=crop"
                                alt="iPhone">
                            <div class="my-slide__content">
                                <h2>iPhone 15 Pro Max</h2>
                                <p>Thiết kế Titan tự nhiên. Chip A17 Pro mạnh mẽ vô song.</p>
                                <a href="#" class="my-slide__btn">Mua Ngay <i class="ti-arrow-right"></i></a>
                            </div>
                        </div>
                        <div class="my-slide">
                            <img src="https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?q=80&w=1920&auto=format&fit=crop"
                                alt="Samsung">
                            <div class="my-slide__content">
                                <h2>Samsung Galaxy S24 Ultra</h2>
                                <p>Quyền năng Galaxy AI. Zoom quang học vươn tầm mắt thần.</p>
                                <a href="#" class="my-slide__btn">Khám Phá <i class="ti-arrow-right"></i></a>
                            </div>
                        </div>
                        <div class="my-slide">
                            <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=1920&auto=format&fit=crop"
                                alt="Accessories">
                            <div class="my-slide__content">
                                <h2>Phụ kiện Âm thanh</h2>
                                <p>Tai nghe chống ồn cực đỉnh. Không gian âm nhạc sống động.</p>
                                <a href="#" class="my-slide__btn">Xem Thêm <i class="ti-arrow-right"></i></a>
                            </div>
                        </div>

                        <button class="slider-nav prev" onclick="changeSlide(-1)"><i class="ti-angle-left"></i></button>
                        <button class="slider-nav next" onclick="changeSlide(1)"><i class="ti-angle-right"></i></button>

                        <div class="slider-dots">
                            <span class="dot active" onclick="goToSlide(0)"></span>
                            <span class="dot" onclick="goToSlide(1)"></span>
                            <span class="dot" onclick="goToSlide(2)"></span>
                        </div>
                    </div>

                    <div class="container" id="product-section"
                        style="background-color: #f5f5f5; padding-top: 20px; padding-bottom: 12px;">
                        <div class="grid wide">
                            <div class="row">
                                <div class="col l-12">
                                    <div class="home-section-title" style="text-align: center; margin-bottom: 30px;">
                                        <h1
                                            style="font-size: 2.8rem; font-weight: 700; color: var(--text-color); text-transform: uppercase;">
                                            Sản phẩm nổi bật</h1>
                                    </div>
                                </div>
                            </div>

                            <!-- <div class="row" style="margin-bottom: 20px;">
                            <div class="col l-12">
                                <form action="home" method="get" class="filter-group"
                                    style="display: flex; gap: 15px; justify-content: flex-end; align-items: center;">
                                    <input type="hidden" name="txtSearch" value="${searchName}">

                                    <select name="ramFilter" onchange="this.form.submit()" class="filter-select">
                                        <option value="">Tất cả RAM</option>
                                        <c:forEach items="${listRAM}" var="r">
                                            <option value="${r}" ${r==selectedRam ? 'selected' : '' }>${r}</option>
                                        </c:forEach>
                                    </select>

                                    <select name="romFilter" onchange="this.form.submit()" class="filter-select">
                                        <option value="">Tất cả ROM</option>
                                        <c:forEach items="${listROM}" var="r">
                                            <option value="${r}" ${r==selectedRom ? 'selected' : '' }>${r}</option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </div>
                        </div> -->

                            <div class="row sm-gutter">
                                <c:set var="userRank" value="${sessionScope.ACC.customerType}" />
                                <c:set var="discountPercent" value="0" />
                                <c:if test="${not empty sessionScope.ACC and not empty userRank}">
                                    <c:choose>
                                        <c:when test="${fn:contains(userRank, 'Diamond')}">
                                            <c:set var="discountPercent" value="15" />
                                        </c:when>
                                        <c:when test="${fn:contains(userRank, 'Gold')}">
                                            <c:set var="discountPercent" value="10" />
                                        </c:when>
                                        <c:when test="${fn:contains(userRank, 'Silver')}">
                                            <c:set var="discountPercent" value="5" />
                                        </c:when>
                                        <c:when test="${fn:contains(userRank, 'Bronze')}">
                                            <c:set var="discountPercent" value="2" />
                                        </c:when>
                                    </c:choose>
                                </c:if>

                                <c:forEach items="${listP}" var="p">
                                    <c:set var="finalPrice" value="${p.price - (p.price * discountPercent / 100)}" />
                                    <div class="col l-2-4 m-4 c-6" style="margin-bottom: 20px;">
                                        <a href="detail?id=${p.productID}" class="home-product-item" onclick="trackProductClick('${p.productName}')">

                                            <div class="home-product-item__img-wrap" style="position: relative;">
                                                <div class="home-product-item__img"
                                                    style="background-image: url('${not empty p.imageURL ? p.imageURL : 'assets/img/default-phone.png'}');">
                                                    <img src="${not empty p.imageURL ? p.imageURL : 'assets/img/default-phone.png'}"
                                                        style="display: none;"
                                                        onerror="this.onerror=null; this.parentElement.style.backgroundImage='url(\'assets/img/default-phone.png\')';" />
                                                </div>
                                                <c:if test="${discountPercent > 0}">
                                                    <div class="home-product-item__sale-off"
                                                        style="position: absolute; top: 10px; right: 10px; background-color: rgba(255,212,36,.9); padding: 4px 6px; border-radius: 4px; z-index: 10; font-weight: bold; color: #ee4d2d;">
                                                        <span
                                                            class="home-product-item__percent">-${discountPercent}%</span>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <h4 class="home-product-item__name">${p.productName}</h4>

                                            <div class="home-product-item__price"
                                                style="display: flex; flex-wrap: wrap; align-items: baseline;">
                                                <c:if test="${discountPercent > 0}">
                                                    <span class="home-product-item__price-old"
                                                        style="text-decoration: line-through; color: #999; font-size: 1.3rem; margin-right: 8px;">
                                                        <fmt:formatNumber value="${p.price}" pattern="#,##0" /> đ
                                                    </span>
                                                </c:if>
                                                <span class="home-product-item__price-current">
                                                    <fmt:formatNumber value="${finalPrice}" pattern="#,##0" /> đ
                                                </span>
                                            </div>

                                            <div class="home-product-item__action">
                                                <button
                                                    onclick="event.preventDefault(); confirmAddCart('${p.productID}', '${p.productName}')"
                                                    class="btn-add-cart-custom">
                                                    <i class="ti-shopping-cart"></i>
                                                </button>
                                                <button
                                                    onclick="event.preventDefault(); confirmBuyNow('${p.productID}', '${p.productName}')"
                                                    class="btn-buy-now-custom">
                                                    Mua ngay
                                                </button>
                                            </div>
                                        </a>
                                    </div>
                                </c:forEach>

                                <c:if test="${listP == null || listP.size() == 0}">
                                    <div class="col l-12" style="text-align: center; padding: 50px;">
                                        <i class="ti-face-sad" style="font-size: 5rem; color: #ccc;"></i>
                                        <p style="font-size: 1.6rem; margin-top: 20px;">Không tìm thấy sản phẩm nào.</p>
                                    </div>
                                </c:if>
                            </div>

                            <c:if test="${totalPages > 1}">
                                <ul class="pagination">
                                    <li class="pagination-item ${currentPage == 1 ? 'pagination-item--disabled' : ''}">
                                        <a href="home?page=${currentPage - 1}&searchName=${searchName}#product-section"
                                            class="pagination-item__link">
                                            <i class="ti-angle-left"></i>
                                        </a>
                                    </li>

                                    <c:set var="showEllipsis" value="false" />
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <c:choose>
                                            <c:when
                                                test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                                <c:set var="showEllipsis" value="false" />
                                                <li class="pagination-item">
                                                    <a href="home?page=${i}&searchName=${searchName}&ramFilter=${param.ramFilter}&romFilter=${param.romFilter}#product-section"
                                                        class="pagination-item__link ${currentPage == i ? 'pagination-item__link--active' : ''}">${i}</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${!showEllipsis}">
                                                    <li class="pagination-item">
                                                        <span class="pagination-item__link"
                                                            style="cursor: default; background: transparent; color: #939393;">...</span>
                                                    </li>
                                                    <c:set var="showEllipsis" value="true" />
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <li
                                        class="pagination-item ${currentPage == totalPages ? 'pagination-item--disabled' : ''}">
                                        <a href="home?page=${currentPage + 1}&searchName=${searchName}#product-section"
                                            class="pagination-item__link">
                                            <i class="ti-angle-right"></i>
                                        </a>
                                    </li>
                                </ul>
                            </c:if>

                        </div>
                    </div>

                    <div class="features">
                        <div class="grid wide">
                            <div class="row">
                                <div class="col l-4 m-4 c-12">
                                    <div class="feature-box">
                                        <div class="feature-icon"><i class="ti-truck"></i></div>
                                        <h3 class="feature-title">Giao hàng miễn phí</h3>
                                        <p class="feature-desc">Cho đơn hàng trên 5 triệu đồng</p>
                                    </div>
                                </div>
                                <div class="col l-4 m-4 c-12">
                                    <div class="feature-box">
                                        <div class="feature-icon"><i class="ti-shield"></i></div>
                                        <h3 class="feature-title">Bảo hành chính hãng</h3>
                                        <p class="feature-desc">Cam kết 100% hàng chính hãng</p>
                                    </div>
                                </div>
                                <div class="col l-4 m-4 c-12">
                                    <div class="feature-box">
                                        <div class="feature-icon"><i class="ti-headphone-alt"></i></div>
                                        <h3 class="feature-title">Hỗ trợ 24/7</h3>
                                        <p class="feature-desc">Luôn sẵn sàng giải đáp thắc mắc</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <jsp:include page="footer.jsp" />
                    <jsp:include page="chat-widget.jsp"></jsp:include>

                    <script>
                        // ======= LOGIC SLIDER THUẦN (VANILLA JS) =======
                        let currentSlideIdx = 0;
                        const slides = document.querySelectorAll('.my-slide');
                        const dots = document.querySelectorAll('.dot');
                        let slideTimer;

                        function showSlide(index) {
                            // Xóa class active ở tất cả
                            slides.forEach(slide => slide.classList.remove('active'));
                            dots.forEach(dot => dot.classList.remove('active'));

                            // Cập nhật index mới
                            currentSlideIdx = index;
                            if (currentSlideIdx >= slides.length) currentSlideIdx = 0;
                            if (currentSlideIdx < 0) currentSlideIdx = slides.length - 1;

                            // Thêm class active
                            slides[currentSlideIdx].classList.add('active');
                            dots[currentSlideIdx].classList.add('active');
                        }

                        function changeSlide(step) {
                            showSlide(currentSlideIdx + step);
                            resetSlideTimer();
                        }

                        function goToSlide(index) {
                            showSlide(index);
                            resetSlideTimer();
                        }

                        function resetSlideTimer() {
                            clearInterval(slideTimer);
                            slideTimer = setInterval(() => {
                                changeSlide(1);
                            }, 4000); // 4 giây tự động đổi slide
                        }

                        // Bắt đầu chạy slider
                        resetSlideTimer();


                        // ======= PERSONALIZATION: Track Product Click =======
                        function trackProductClick(productName) {
                            try {
                                // Fire & forget - không chặn navigation
                                fetch('chat-bot?action=track&productName=' + encodeURIComponent(productName), { method: 'GET', keepalive: true }).catch(() => {});
                            } catch(e) {}
                        }

                        // ======= LOGIC GIỎ HÀNG =======
                        function confirmAddCart(id, name) {
                            Swal.fire({
                                title: 'Thêm vào giỏ hàng?',
                                text: "Bạn muốn thêm " + name + " vào giỏ hàng chứ?",
                                icon: 'question',
                                showCancelButton: true,
                                confirmButtonColor: '#ee4b2b',
                                cancelButtonColor: '#6c757d',
                                confirmButtonText: 'Đúng, thêm ngay!',
                                cancelButtonText: 'Hủy'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    window.location.href = "add-to-cart?pid=" + id;
                                }
                            })
                        }

                        function confirmBuyNow(id, name) {
                            Swal.fire({
                                title: 'Xác nhận mua ngay?',
                                text: "Bạn sẽ được chuyển đến trang thanh toán cho " + name,
                                icon: 'info',
                                showCancelButton: true,
                                confirmButtonColor: '#ee4b2b',
                                cancelButtonColor: '#6c757d',
                                confirmButtonText: 'Thanh toán ngay',
                                cancelButtonText: 'Để sau'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    window.location.href = "add-to-cart?pid=" + id + "&returnURL=view-cart";
                                }
                            })
                        };
                    </script>
                </body>

                </html>