<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <div class="admin-sidebar">
        <div class="admin-sidebar__header">
            <a href="home" class="admin-sidebar__logo">Phone<span>Shop</span> Admin</a>
        </div>
        <ul class="admin-menu">
            <li class="admin-menu__item">
                <a href="admin-dashboard"
                    class="admin-menu__link ${activePage == 'dashboard' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-dashboard admin-menu__icon"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="admin-customer-list"
                    class="admin-menu__link ${activePage == 'customer-management' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-user admin-menu__icon"></i>
                    <span>Quản lý Khách hàng</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="admin-product-list"
                    class="admin-menu__link ${activePage == 'product-management' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-mobile admin-menu__icon"></i>
                    <span>Quản lý Sản phẩm</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="category-list"
                    class="admin-menu__link ${activePage == 'category-management' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-view-list-alt admin-menu__icon"></i>
                    <span>Quản lý Loại sản phẩm</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="supplier"
                    class="admin-menu__link ${activePage == 'supplier-management' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-truck admin-menu__icon"></i>
                    <span>Quản lý nhà cung cấp</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="order-list"
                    class="admin-menu__link ${activePage == 'order-management' ? 'admin-menu__link--active' : ''}">
                    <i class="ti-shopping-cart admin-menu__icon"></i>
                    <span>Quản lý Đơn hàng</span>
                </a>
            </li>
            <li class="admin-menu__item">
                <a href="user-logout" onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');"
                    class="admin-menu__link">
                    <i class="ti-power-off admin-menu__icon"></i>
                    <span>Đăng xuất</span>
                </a>
            </li>
        </ul>
    </div>

    <!-- <jsp:include page="chat-widget.jsp" /> -->