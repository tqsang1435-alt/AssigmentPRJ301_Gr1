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
                    <title>Kết quả tìm kiếm | PHONEShop</title>

                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
                    <style>
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

                        /* CSS Layout Shopee (Sidebar & Bộ lọc) */
                        .category {
                            background-color: var(--white-color, #fff);
                            border-radius: 2px;
                            padding: 20px 15px;
                        }

                        .category__heading {
                            font-size: 1.6rem;
                            color: var(--text-color, #333);
                            padding-bottom: 15px;
                            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
                            margin: 0 0 15px 0;
                            text-transform: uppercase;
                            font-weight: 500;
                        }

                        .category__heading-icon {
                            font-size: 1.4rem;
                            margin-right: 4px;
                        }

                        .category-item {
                            margin-bottom: 15px;
                        }

                        .category-item__label {
                            font-size: 1.4rem;
                            color: var(--text-color, #333);
                            display: block;
                            margin-bottom: 8px;
                            font-weight: 500;
                        }

                        .search-result-header {
                            display: flex;
                            align-items: center;
                            background-color: rgba(0, 0, 0, 0.03);
                            padding: 14px 20px;
                            border-radius: 2px;
                            margin-bottom: 20px;
                            justify-content: space-between;
                        }
                    </style>
                </head>

                <body>

                    <jsp:include page="header.jsp" />

                    <div class="container" id="product-section"
                        style="background-color: #f5f5f5; padding-top: 40px; padding-bottom: 40px; min-height: 50vh;">
                        <div class="grid wide">
                            <div class="row sm-gutter">
                                <div class="col l-12 m-12 c-12">
                                    <div class="search-result-header">
                                        <h2
                                            style="font-size: 1.6rem; font-weight: 500; margin: 0; color: var(--text-color);">
                                            <c:if test="${not empty searchName}">
                                                Kết quả tìm kiếm cho: <span
                                                    style="color: var(--primary-color);">"${searchName}"</span>
                                            </c:if>
                                            <c:if test="${empty searchName}">
                                                Tất cả sản phẩm
                                            </c:if>
                                        </h2>
                                        <form action="search" method="get"
                                            style="display: flex; align-items: center; gap: 15px; margin: 0; flex-wrap: wrap;">
                                            <input type="hidden" name="searchName" value="${searchName}">

                                            <div style="display: flex; align-items: center; gap: 5px;">
                                                <span style="font-size: 1.4rem; color: var(--text-color);">RAM:</span>
                                                <select name="ramFilter" onchange="this.form.submit()"
                                                    class="filter-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach items="${listRAM}" var="r">
                                                        <option value="${r}" ${r==selectedRam ? 'selected' : '' }>${r}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div style="display: flex; align-items: center; gap: 5px;">
                                                <span style="font-size: 1.4rem; color: var(--text-color);">ROM:</span>
                                                <select name="romFilter" onchange="this.form.submit()"
                                                    class="filter-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach items="${listROM}" var="r">
                                                        <option value="${r}" ${r==selectedRom ? 'selected' : '' }>${r}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div style="display: flex; align-items: center; gap: 5px;">
                                                <span style="font-size: 1.4rem; color: var(--text-color);">Sắp
                                                    xếp:</span>
                                                <select name="sortPrice" onchange="this.form.submit()"
                                                    class="filter-select">
                                                    <option value="">Mặc định</option>
                                                    <option value="asc" ${param.sortPrice=='asc' ? 'selected' : '' }>
                                                        Giá: Thấp đến Cao</option>
                                                    <option value="desc" ${param.sortPrice=='desc' ? 'selected' : '' }>
                                                        Giá: Cao đến Thấp</option>
                                                </select>
                                            </div>
                                        </form>
                                    </div>

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
                                            <c:set var="finalPrice"
                                                value="${p.price - (p.price * discountPercent / 100)}" />
                                            <div class="col l-2-4 m-4 c-6" style="margin-bottom: 20px;">
                                                <a href="detail?id=${p.productID}" class="home-product-item" onclick="trackProductClick('${p.productName}')">

                                                    <div class="home-product-item__img-wrap"
                                                        style="position: relative;">
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

                                                    <c:if test="${p.stockQuantity > 0}">
                                                        <div class="home-product-item__stock" style="font-size: 1.2rem; color: #28a745; margin-bottom: 8px;">
                                                            Còn ${p.stockQuantity} sản phẩm
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${p.stockQuantity <= 0}">
                                                        <div class="home-product-item__stock" style="font-size: 1.2rem; color: #dc3545; margin-bottom: 8px;">
                                                            Hết hàng
                                                        </div>
                                                    </c:if>

                                                    <div class="home-product-item__price"
                                                        style="display: flex; flex-wrap: wrap; align-items: baseline;">
                                                        <c:if test="${discountPercent > 0}">
                                                            <span class="home-product-item__price-old"
                                                                style="text-decoration: line-through; color: #999; font-size: 1.3rem; margin-right: 8px;">
                                                                <fmt:formatNumber value="${p.price}" pattern="#,##0" />
                                                                đ
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
                                                <p style="font-size: 1.6rem; margin-top: 20px;">Không tìm thấy sản phẩm
                                                    nào.</p>
                                            </div>
                                        </c:if>
                                    </div>

                                    <c:if test="${totalPages > 1}">
                                        <ul class="pagination">
                                            <li
                                                class="pagination-item ${currentPage == 1 ? 'pagination-item--disabled' : ''}">
                                                <a href="search?page=${currentPage - 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}&sortPrice=${param.sortPrice}#product-section"
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
                                                            <a href="search?page=${i}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}&sortPrice=${param.sortPrice}#product-section"
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
                                                <a href="search?page=${currentPage + 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}&sortPrice=${param.sortPrice}#product-section"
                                                    class="pagination-item__link">
                                                    <i class="ti-angle-right"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <jsp:include page="footer.jsp" />
                    <jsp:include page="chat-widget.jsp"></jsp:include>

                    <script>
                        // ======= PERSONALIZATION: Track Product Click =======
                        function trackProductClick(productName) {
                            try {
                                fetch('chat-bot?action=track&productName=' + encodeURIComponent(productName), { method: 'GET', keepalive: true }).catch(() => {});
                            } catch(e) {}
                        }

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