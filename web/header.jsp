<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <header class="header">
            <div class="grid wide">
                <div class="header__navbar">
                    <!-- Logo -->
                    <a href="${pageContext.request.contextPath}/home" class="header__logo">
                        <i class="ti-mobile"></i> <label>PhoneShop</label>
                    </a>

                    <!-- Nav Links Left -->
                    <ul class="header__nav-list" style="margin-left: 20px;">
                        <li class="header__nav-item header__nav-item--has-sub">
                            <a href="#" class="header__nav-link">
                                <i class="ti-menu"></i> <label class="hide-on-mobile">Danh mục</label> <i
                                    class="ti-angle-down"
                                    style="font-size: 1rem; margin-left: 4px; transition: transform 0.2s ease;"></i>
                            </a>
                            <ul class="header__sub-nav">
                                <li class="header__sub-nav-item"><a
                                        href="${pageContext.request.contextPath}/home?brand=Apple"
                                        class="header__sub-nav-link">Apple (iPhone)</a>
                                </li>
                                <li class="header__sub-nav-item"><a
                                        href="${pageContext.request.contextPath}/home?brand=Samsung"
                                        class="header__sub-nav-link">Samsung</a></li>
                                <li class="header__sub-nav-item"><a
                                        href="${pageContext.request.contextPath}/home?brand=Oppo"
                                        class="header__sub-nav-link">Oppo</a></li>
                                <li class="header__sub-nav-item"><a
                                        href="${pageContext.request.contextPath}/home?brand=Xiaomi"
                                        class="header__sub-nav-link">Xiaomi</a></li>
                            </ul>
                        </li>
                    </ul>

                    <!-- Search -->
                    <form action="search" method="get" class="header__search hide-on-mobile">
                        <input type="text" name="searchName" class="header__search-input"
                            placeholder="Tìm kiếm điện thoại, phụ kiện..." value="${searchName}">
                        <button class="header__search-btn"><i class="ti-search"></i></button>
                    </form>

                    <!-- Nav Links Right -->
                    <ul class="header__nav-list">

                        <li class="header__nav-item">
                            <a href="${pageContext.request.contextPath}/view-cart"
                                class="header__nav-link header__nav-link--warning">
                                <i class="ti-shopping-cart"></i> <label class="hide-on-mobile">Giỏ hàng</label>
                            </a>
                        </li>
                        <c:choose>
                            <c:when test="${not empty sessionScope.ACC}">
                                <li class="header__nav-item header__nav-item--has-sub">
                                    <a href="#" class="header__nav-link">
                                        <i class="ti-user"></i> <label class="hide-on-mobile">
                                            ${sessionScope.ACC.fullName}</label> <i class="ti-angle-down"
                                            style="font-size: 1rem; margin-left: 4px; transition: transform 0.2s ease;"></i>
                                    </a>
                                    <ul class="header__sub-nav">
                                        <li class="header__sub-nav-item">
                                            <a href="${pageContext.request.contextPath}/user-profile"
                                                class="header__sub-nav-link">
                                                <i class="ti-id-badge" style="margin-right: 8px;"></i> Hồ sơ của tôi
                                            </a>
                                        </li>
                                        <li class="header__sub-nav-item">
                                            <a href="${pageContext.request.contextPath}/purchase-history"
                                                class="header__sub-nav-link">
                                                <i class="ti-receipt" style="margin-right: 8px;"></i> Lịch sử mua hàng
                                            </a>
                                        </li>
                                        <li class="header__sub-nav-item">
                                            <a href="${pageContext.request.contextPath}/user-logout"
                                                onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');"
                                                class="header__sub-nav-link">
                                                <i class="ti-power-off" style="margin-right: 8px;"></i> Đăng xuất
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="header__nav-item">
                                    <a href="${pageContext.request.contextPath}/user-login" class="header__nav-link">
                                        <i class="ti-user"></i> <label class="hide-on-mobile">Đăng nhập</label>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </header>