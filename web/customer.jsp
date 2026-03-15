<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Quản lý Khách hàng - PhoneShop</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        </head>

        <body>
            <header class="header">
                <div class="grid wide">
                    <div class="header__navbar">
                        <a href="${pageContext.request.contextPath}/" class="header__logo">
                            <i class="ti-mobile"></i><label>PhoneShop Admin</label>
                        </a>
                        <ul class="header__nav-list">
                            <li class="header__nav-item"><a href="user-logout" onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');" class="header__nav-link">Đăng xuất</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </header>

            <div class="admin-content">
                <div class="grid wide">
                    <div class="row">
                        <div class="col l-12 m-12 c-12">
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">Danh sách Khách hàng</h3>
                                    <a href="add-customer" class="btn btn-primary">Thêm mới</a>
                                </div>
                                <div class="card-body">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Họ và tên</th>
                                                <th>Email</th>
                                                <th>Số điện thoại</th>
                                                <th>Địa chỉ</th>
                                                <th>Điểm thưởng</th>
                                                <th>Loại khách</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${listC}" var="c">
                                                <tr>
                                                    <td>${c.userID}</td>
                                                    <td>${c.fullName}</td>
                                                    <td>${c.email}</td>
                                                    <td>${c.phoneNumber}</td>
                                                    <td>${c.address}</td>
                                                    <td style="text-align: center;">${c.rewardPoints}</td>
                                                    <td>
                                                        <span class="badge badge--normal">${c.customerType}</span>
                                                    </td>
                                                    <td>
                                                        <a href="edit-customer?id=${c.userID}" class="btn-action btn-action--edit">Sửa</a>
                                                        <a href="delete-customer?id=${c.userID}" onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng này không?');" class="btn-action btn-action--delete">Xóa</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>