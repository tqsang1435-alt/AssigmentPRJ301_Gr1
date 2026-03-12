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

            </head>

            <body>

                <jsp:include page="header.jsp" />

                <!-- HERO SLIDER -->
                <div class="hero-slider">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12 m-12 c-12">
                                <h1 class="hero__heading">Công nghệ đỉnh cao</h1>
                                <p class="hero__desc">Khám phá những mẫu điện thoại mới nhất 2024</p>
                                <a href="#product-section" class="btn hero__btn"
                                    style="background-color: var(--primary-color); color: white; text-decoration: none; display: inline-flex; align-items: center;">
                                    Mua ngay <label><i class="ti-arrow-right"></i></label>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container" id="product-section"
                    style="background-color: #f5f5f5; padding-top: 20px; padding-bottom: 50px;">
                    <div class="grid wide">
                        <div class="row">
                            <div class="col l-12">
                                <div class="main-title" style="text-align: center; margin-bottom: 30px;">
                                    <h1>Sản phẩm nổi bật</h1>
                                </div>
                            </div>
                        </div>

                        <div class="row sm-gutter">
                            <c:forEach items="${listP}" var="p">
                                <div class="col l-2-4 m-4 c-6" style="margin-bottom: 20px;">
                                    <a href="product-detail?id=${p.productID}" class="home-product-item"
                                        style="display: block; text-decoration: none; background-color: #fff; border-radius: 8px; box-shadow: 0 1px 5px rgba(0,0,0,0.1); padding-bottom: 15px; height: 100%; display: flex; flex-direction: column; transition: transform 0.2s, box-shadow 0.2s;"
                                        onmouseover="this.style.transform='translateY(-3px)'; this.style.boxShadow='0 4px 20px rgba(0,0,0,0.12)';"
                                        onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='0 1px 5px rgba(0,0,0,0.1)';">
                                        <div class="home-product-item__img"
                                            style="padding-top: 100%; background-image: url('${not empty p.imageURL ? p.imageURL : 'assets/img/default-phone.png'}'); background-size: contain; background-repeat: no-repeat; background-position: center; margin: 10px;">
                                        </div>

                                        <h4 class="home-product-item__name"
                                            style="font-size: 1.4rem; font-weight: 500; color: var(--text-color); margin: 10px 10px 6px; line-height: 1.8rem; height: 3.6rem; overflow: hidden; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2;">
                                            ${p.productName}</h4>

                                        <div class="home-product-item__price" style="margin: 0 10px 10px;">
                                            <span class="home-product-item__price-current"
                                                style="font-size: 1.6rem; color: var(--primary-color); font-weight: bold;">
                                                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> đ
                                            </span>
                                        </div>

                                        <div class="home-product-item__action"
                                            style="padding: 0 10px; margin-top: auto; display: flex; gap: 5px;">
                                            <button
                                                onclick="event.preventDefault(); confirmAddCart('${p.productID}', '${p.productName}')"
                                                class="btn btn--primary"
                                                style="flex: 1; font-size: 1.6rem; padding: 0; height: 36px; border-radius: 4px; cursor: pointer;">
                                                <i class="ti-shopping-cart"></i>
                                            </button>
                                            <button
                                                onclick="event.preventDefault(); confirmBuyNow('${p.productID}', '${p.productName}')"
                                                class="btn btn--primary"
                                                style="flex: 1; font-size: 1.2rem; padding: 0; height: 36px; border-radius: 4px; background-color: var(--gold-color); color: black; cursor: pointer;">
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
                            <ul class="pagination home-product__pagination"
                                style="margin: 40px 0 0 0; justify-content: center;">
                                <li class="pagination-item ${currentPage == 1 ? 'pagination-item--disabled' : ''}"
                                    style="${currentPage == 1 ? 'pointer-events: none; opacity: 0.4;' : ''}">
                                    <a href="home?page=${currentPage - 1}<c:if test='${not empty cid}'>&cid=${cid}</c:if>#product-section"
                                        class="pagination-item__link">
                                        <i class="ti-angle-left"></i>
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="pagination-item ${currentPage == i ? 'pagination-item--active' : ''}">
                                        <a href="home?page=${i}<c:if test='${not empty cid}'>&cid=${cid}</c:if>#product-section"
                                            class="pagination-item__link">${i}</a>
                                    </li>
                                </c:forEach>

                                <li class="pagination-item ${currentPage == totalPages ? 'pagination-item--disabled' : ''}"
                                    style="${currentPage == totalPages ? 'pointer-events: none; opacity: 0.4;' : ''}">
                                    <a href="home?page=${currentPage + 1}<c:if test='${not empty cid}'>&cid=${cid}</c:if>#product-section"
                                        class="pagination-item__link">
                                        <i class="ti-angle-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </c:if>

                    </div>
                </div>

                <!-- FEATURES -->
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
                    }
                </script>
            </body>

            </html>