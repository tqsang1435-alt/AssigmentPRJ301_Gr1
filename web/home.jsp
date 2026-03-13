<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

            </head>

            <body>

                <jsp:include page="header.jsp" />

                <!-- BOOTSTRAP CAROUSEL -->
                <div id="homeCarousel" class="carousel slide" data-bs-ride="carousel">
                    <!-- Indicators (Các dấu chấm tròn bên dưới) -->
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="0" class="active"
                            aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="1"
                            aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="2"
                            aria-label="Slide 3"></button>
                    </div>

                    <!-- Inner (Chứa các slide) -->
                    <div class="carousel-inner">
                        <!-- Slide 1 -->
                        <div class="carousel-item active">
                            <!-- Thay đổi đường dẫn ảnh tại src -->
                            <img src="https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?q=80&w=1920&auto=format&fit=crop"
                                class="d-block w-100" alt="Banner 1" style="height: 450px; object-fit: cover;">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Chào mừng đến với PhoneShop</h5>
                                <p>Điện thoại chính hãng, giá tốt nhất thị trường.</p>
                            </div>
                        </div>
                        <!-- Slide 2 -->
                        <div class="carousel-item">
                            <img src="https://images.unsplash.com/photo-1616348436168-de43ad0db179?q=80&w=1920&auto=format&fit=crop"
                                class="d-block w-100" alt="Banner 2" style="height: 450px; object-fit: cover;">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Sản phẩm mới nhất</h5>
                                <p>Cập nhật những mẫu flagship đỉnh cao.</p>
                            </div>
                        </div>
                        <!-- Slide 3 -->
                        <div class="carousel-item">
                            <img src="https://images.unsplash.com/photo-1592750475338-74b7b21085ab?q=80&w=1920&auto=format&fit=crop"
                                class="d-block w-100" alt="Banner 3" style="height: 450px; object-fit: cover;">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Khuyến mãi hấp dẫn</h5>
                                <p>Giảm giá cực sốc trong tháng này.</p>
                            </div>
                        </div>
                    </div>

                    <!-- Controls (Nút Previous/Next) -->
                    <button class="carousel-control-prev" type="button" data-bs-target="#homeCarousel"
                        data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#homeCarousel"
                        data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>


                <div class="container" id="product-section"
                    style="background-color: #f5f5f5; padding-top: 20px; padding-bottom: 12px;">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12">
                                <div class="home-section-title">
                                    <h1>Sản phẩm nổi bật</h1>
                                </div>
                            </div>
                        </div>

                        <!-- Bộ lọc RAM và ROM -->
                        <div class="grid wide" style="margin-top: 20px; margin-bottom: 10px;">
                            <form action="home" method="get" class="filter-group"
                                style="display: flex; gap: 15px; justify-content: flex-end; align-items: center;">
                                <!-- Giữ lại từ khóa tìm kiếm nếu người dùng đang tìm kiếm từ Header -->
                                <input type="hidden" name="txtSearch" value="${searchName}">
                                <!-- Dropdown chọn RAM -->
                                <select name="ramFilter" onchange="this.form.submit()" class="form-control"
                                    style="width: auto; height: 36px; cursor: pointer;">
                                    <option value="">Tất cả RAM</option>
                                    <c:forEach items="${listRAM}" var="r">
                                        <option value="${r}" ${r==selectedRam ? 'selected' : '' }>${r}</option>
                                    </c:forEach>
                                </select>
                                <!-- Dropdown chọn ROM -->
                                <select name="romFilter" onchange="this.form.submit()" class="form-control"
                                    style="width: auto; height: 36px; cursor: pointer;">
                                    <option value="">Tất cả ROM</option>
                                    <c:forEach items="${listROM}" var="r">
                                        <option value="${r}" ${r==selectedRom ? 'selected' : '' }>${r}</option>
                                    </c:forEach>
                                </select>
                            </form>
                        </div>


                        <div class="row sm-gutter">
                            <c:forEach items="${listP}" var="p">
                                <div class="col l-2-4 m-4 c-6" style="margin-bottom: 20px;">
                                    <a href="product-detail?id=${p.productID}" class="home-product-item">
                                        <div class="home-product-item__img"
                                            style="background-image: url('${not empty p.imageURL ? p.imageURL : 'assets/img/default-phone.png'}');">
                                        </div>

                                        <h4 class="home-product-item__name">${p.productName}</h4>

                                        <div class="home-product-item__price">
                                            <span class="home-product-item__price-current">
                                                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> đ
                                            </span>
                                        </div>

                                        <div class="home-product-item__action">
                                            <button
                                                onclick="event.preventDefault(); confirmAddCart('${p.productID}', '${p.productName}')"
                                                class="btn btn--primary btn-add-cart">
                                                <i class="ti-shopping-cart"></i>
                                            </button>
                                            <button
                                                onclick="event.preventDefault(); confirmBuyNow('${p.productID}', '${p.productName}')"
                                                class="btn btn-buy-now">
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

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="pagination-item">
                                        <a href="home?page=${i}&searchName=${searchName}#product-section"
                                            class="pagination-item__link ${currentPage == i ? 'pagination-item__link--active' : ''}">${i}</a>
                                    </li>
                                </c:forEach>

                                <li
                                    class="pagination-item ${currentPage == totalPages ? 'pagination-item--disabled' : ''}">
                                    <a href="home?page=${currentPage + 1}&searchName=${searchName}#product-section"
                                        class="pagination-item__link ">
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

                    document.addEventListener('DOMContentLoaded', function () {
                        const slider = document.querySelector('#home-slider');
                        if (!slider) return;

                        const wrapper = slider.querySelector('.hero-slider__wrapper');
                        const slides = slider.querySelectorAll('.hero-slider__item');
                        const prevBtn = slider.querySelector('.slider-prev');
                        const nextBtn = slider.querySelector('.slider-next');
                        const dotsContainer = slider.querySelector('.slider-dots');

                        let currentIndex = 0;
                        const totalSlides = slides.length;
                        let slideInterval;

                        // Tạo các dấu chấm (dots)
                        slides.forEach((_, index) => {
                            const dot = document.createElement('span');
                            dot.classList.add('slider-dot');
                            if (index === 0) dot.classList.add('active');
                            dot.addEventListener('click', () => {
                                goToSlide(index);
                                resetInterval();
                            });
                            dotsContainer.appendChild(dot);
                        });

                        const dots = dotsContainer.querySelectorAll('.slider-dot');

                        // Hàm chuyển slide "miễn nhiễm" với lỗi CSS
                        function updateSlider() {
                            const sliderWidth = slider.offsetWidth; // Lấy đúng chiều rộng thực tế
                            wrapper.style.transform = `translateX(-${currentIndex * sliderWidth}px)`;

                            // Cập nhật chấm
                            dots.forEach(dot => dot.classList.remove('active'));
                            if (dots[currentIndex]) dots[currentIndex].classList.add('active');
                        }

                        // Fix lỗi resize màn hình làm lệch slide
                        window.addEventListener('resize', updateSlider);

                        function goToSlide(index) {
                            currentIndex = index;
                            if (currentIndex < 0) currentIndex = totalSlides - 1;
                            if (currentIndex >= totalSlides) currentIndex = 0;
                            updateSlider();
                        }

                        function nextSlide() {
                            goToSlide(currentIndex + 1);
                        }

                        function prevSlide() {
                            goToSlide(currentIndex - 1);
                        }

                        function resetInterval() {
                            clearInterval(slideInterval);
                            slideInterval = setInterval(nextSlide, 5000); // Tự động chuyển sau 5 giây
                        }

                        // Gắn sự kiện click
                        if (nextBtn) nextBtn.addEventListener('click', () => {
                            nextSlide();
                            resetInterval();
                        });

                        if (prevBtn) prevBtn.addEventListener('click', () => {
                            prevSlide();
                            resetInterval();
                        });

                        resetInterval();
                    });
                </script>
            </body>

            </html>