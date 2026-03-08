<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Hồ sơ cá nhân - PhoneShop</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

            <style></style>
        </head>

        <body>
            <header class="header">
                <div class="grid wide">
                    <div class="header__navbar">
                        <a href="${pageContext.request.contextPath}/" class="header__logo" title="Về trang chủ">
                            <i class="ti-mobile"></i><label>PhoneShop</label>
                        </a>
                    </div>
                </div>
            </header>

            <div class="profile-wrap">
                <div class="grid wide">
                    <div class="row">
                        <div class="col l-6 l-o-3 m-8 m-o-2 c-12">
                            <div class="profile-card">
                                <div class="profile-header">
                                    <div class="profile-avatar"><i class="ti-user"></i></div>
                                    <h2 class="profile-header__name">${sessionScope.ACC.fullName}</h2>
                                    <p class="profile-header__role">Vai trò: ${sessionScope.ACC.role}</p>
                                </div>

                                <div class="profile-body">
                                    <div class="info-group">
                                        <div class="info-label"><i class="ti-id-badge"></i> Mã người dùng</div>
                                        <div class="info-value">${sessionScope.ACC.userID}</div>
                                    </div>
                                    <div class="info-group">
                                        <div class="info-label"><i class="ti-mobile"></i> Số điện thoại</div>
                                        <div class="info-value">${sessionScope.ACC.phoneNumber}</div>
                                    </div>
                                    <div class="info-group">
                                        <div class="info-label"><i class="ti-email"></i> Email</div>
                                        <div class="info-value">${sessionScope.ACC.email}</div>
                                    </div>

                                    <div class="profile-actions">
                                        <button class="btn btn--primary"><i class="ti-pencil-alt"></i> Cập nhật</button>
                                        <a href="user-logout" class="btn btn--outline-danger"><i
                                                class="ti-power-off"></i> Đăng xuất</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>