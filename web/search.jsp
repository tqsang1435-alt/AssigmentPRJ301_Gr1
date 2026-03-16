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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
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
    </style>
</head>

<body>

    <jsp:include page="header.jsp" />

    <div class="container" id="product-section"
        style="background-color: #f5f5f5; padding-top: 40px; padding-bottom: 40px; min-height: 50vh;">
        <div class="grid wide">
            <div class="row">
                <div class="col l-12">
                    <div class="home-section-title" style="text-align: center; margin-bottom: 30px;">
                        <h1 style="font-size: 2.8rem; font-weight: 700; color: var(--text-color); text-transform: uppercase;">
                            <c:if test="${not empty searchName}">
                                Kết quả tìm kiếm cho: "${searchName}"
                            </c:if>
                            <c:if test="${empty searchName}">
                                Tất cả sản phẩm
                            </c:if>
                        </h1>
                    </div>
                </div>
            </div>

            <div class="row" style="margin-bottom: 20px;">
                <div class="col l-12">
                    <form action="search" method="get" class="filter-group"
                        style="display: flex; gap: 15px; justify-content: flex-end; align-items: center;">
                        <input type="hidden" name="searchName" value="${searchName}">

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
            </div>

            <div class="row sm-gutter">
                <c:set var="userRank" value="${sessionScope.ACC.customerType}" />
                <c:set var="discountPercent" value="0" />
                <c:if test="${not empty sessionScope.ACC and not empty userRank}">
                    <c:choose>
                        <c:when test="${fn:contains(userRank, 'Diamond')}"><c:set var="discountPercent" value="15" /></c:when>
                        <c:when test="${fn:contains(userRank, 'Gold')}"><c:set var="discountPercent" value="10" /></c:when>
                        <c:when test="${fn:contains(userRank, 'Silver')}"><c:set var="discountPercent" value="5" /></c:when>
                        <c:when test="${fn:contains(userRank, 'Bronze')}"><c:set var="discountPercent" value="2" /></c:when>
                    </c:choose>
                </c:if>

                <c:forEach items="${listP}" var="p">
                    <c:set var="finalPrice" value="${p.price - (p.price * discountPercent / 100)}" />
                    <div class="col l-2-4 m-4 c-6" style="margin-bottom: 20px;">
                        <a href="detail?id=${p.productID}" class="home-product-item">

                            <div class="home-product-item__img-wrap" style="position: relative;">
                                <div class="home-product-item__img"
                                    style="background-image: url('${not empty p.imageURL ? p.imageURL : 'assets/img/default-phone.png'}');">
                                </div>
                                <c:if test="${discountPercent > 0}">
                                    <div class="home-product-item__sale-off" style="position: absolute; top: 10px; right: 10px; background-color: rgba(255,212,36,.9); padding: 4px 6px; border-radius: 4px; z-index: 10; font-weight: bold; color: #ee4d2d;">
                                        <span class="home-product-item__percent">-${discountPercent}%</span>
                                    </div>
                                </c:if>
                            </div>

                            <h4 class="home-product-item__name">${p.productName}</h4>

                            <div class="home-product-item__price" style="display: flex; flex-wrap: wrap; align-items: baseline;">
                                <c:if test="${discountPercent > 0}">
                                    <span class="home-product-item__price-old" style="text-decoration: line-through; color: #999; font-size: 1.3rem; margin-right: 8px;">
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
                        <a href="search?page=${currentPage - 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}#product-section"
                            class="pagination-item__link">
                            <i class="ti-angle-left"></i>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="pagination-item">
                            <a href="search?page=${i}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}#product-section"
                                class="pagination-item__link ${currentPage == i ? 'pagination-item__link--active' : ''}">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="pagination-item ${currentPage == totalPages ? 'pagination-item--disabled' : ''}">
                        <a href="search?page=${currentPage + 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}#product-section"
                            class="pagination-item__link">
                            <i class="ti-angle-right"></i>
                        </a>
                    </li>
                </ul>
            </c:if>

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
    </script>
</body>

</html>
