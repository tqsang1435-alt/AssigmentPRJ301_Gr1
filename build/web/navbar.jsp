<%-- BƯỚC QUAN TRỌNG NHẤT: Khai báo UTF-8 ở dòng đầu tiên --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    /* Font chữ hiện đại và hiệu ứng nổi */
    .custom-navbar {
        background: #212529 !important;
        border-bottom: 2px solid #ffc107;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* Tạo độ nổi cho navbar */
    }
    .nav-logo {
        font-size: 1.4rem;
        letter-spacing: 1px;
        color: #ffc107 !important;
    }
    .user-dropdown-btn {
        background-color: #ffc107 !important;
        color: #000 !important;
        font-weight: 600;
        border-radius: 50px;
        transition: all 0.3s;
    }
    .user-dropdown-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(255, 193, 7, 0.3);
    }
    .dropdown-menu {
        border-radius: 12px;
        border: none;
        box-shadow: 0 8px 20px rgba(0,0,0,0.2); /* Đổ bóng menu xổ xuống */
    }
    .dropdown-item {
        padding: 10px 20px;
        font-size: 0.95rem;
    }
</style>

<nav class="navbar navbar-expand-lg navbar-dark custom-navbar sticky-top px-4 py-2">
    <div class="container">
        <a class="navbar-brand fw-bold nav-logo" href="home">
            <i class="fa-solid fa-mobile-button me-2"></i>PHONE<span class="text-white">Shop</span>
        </a>

        <div class="ms-auto d-flex align-items-center gap-3">
            <a href="cart" class="btn btn-outline-light border-0 position-relative p-2">

                <c:if test="${sessionScope.cartSize > 0}">
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                        ${sessionScope.cartSize}
                    </span>
                </c:if>
            </a>

            <div class="ms-auto d-flex align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <a href="cart" class="text-white position-relative me-4 text-decoration-none">
                            <i class="fa-solid fa-cart-shopping fs-4"></i>
                            <c:if test="${not empty sessionScope.cartCount && sessionScope.cartCount > 0}">
                                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" id="cart-badge">
                                    ${sessionScope.cartCount}
                                </span>
                            </c:if>
                        </a>

                        <div class="dropdown">
                            <button class="btn user-dropdown-btn dropdown-toggle px-3 fw-bold" type="button" data-bs-toggle="dropdown" style="background-color: #ffc107; border-radius: 50px;">
                                <i class="fa-solid fa-circle-user me-1"></i> ${sessionScope.username}
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end mt-2 shadow border-0" style="border-radius: 12px;">
                                <li><a class="dropdown-item py-2" href="profile.jsp"><i class="fa-solid fa-user-gear me-2 text-warning"></i>Hồ sơ cá nhân</a></li>
                                <li>
                                    <a class="dropdown-item py-2" href="order-history">
                                        <i class="fa-solid fa-bag-shopping me-2 text-warning"></i>Lịch sử mua hàng
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger fw-bold py-2" href="logout"><i class="fa-solid fa-power-off me-2"></i>Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="login.jsp" class="btn btn-outline-warning rounded-pill px-4 fw-bold">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>