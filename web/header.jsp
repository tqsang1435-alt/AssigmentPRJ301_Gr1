<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <header class="header">
            <div class="grid wide">
                <div class="header__navbar">
                    <!-- Logo -->
                    <a href="${pageContext.request.contextPath}/" class="header__logo">
                        <i class="ti-mobile"></i> <label>PhoneShop</label>
                    </a>

                    <!-- Search -->
                    <div class="header__search hide-on-mobile">
                        <input type="text" class="header__search-input"
                            placeholder="Tìm kiếm điện thoại, laptop, phụ kiện...">
                        <button class="header__search-btn"><i class="ti-search"></i></button>
                    </div>

                    <!-- Nav Links -->
                    <ul class="header__nav-list">
                        <li class="header__nav-item">
                            <a href="${pageContext.request.contextPath}/view-cart"
                                class="header__nav-link header__nav-link--warning">
                                <i class="ti-shopping-cart"></i> <label class="hide-on-mobile">Giỏ hàng</label>
                            </a>
                        </li>
                        <c:choose>
                            <c:when test="${not empty sessionScope.ACC}">
                                <li class="header__nav-item">
                                    <a href="${pageContext.request.contextPath}/user-profile" class="header__nav-link">
                                        <i class="ti-user"></i> <label class="hide-on-mobile">
                                            ${sessionScope.ACC.fullName}</label>
                                    </a>
                                </li>
                                <li class="header__nav-item">
                                    <a href="${pageContext.request.contextPath}/user-logout" class="header__nav-link"
                                        title="Đăng xuất">
                                        <i class="ti-power-off"></i>
                                    </a>
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