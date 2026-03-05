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
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

            <style>
                .profile-wrap {
                    padding: 60px 0;
                    background-color: #f8f9fa;
                    min-height: 80vh;
                }

                .profile-card {
                    background: #fff;
                    border-radius: 8px;
                    box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
                    overflow: hidden;
                }

                .profile-header {
                    background: var(--primary-color);
                    color: white;
                    padding: 40px 20px;
                    text-align: center;
                }

                .profile-avatar {
                    width: 100px;
                    height: 100px;
                    background: white;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 4rem;
                    color: var(--primary-color);
                    margin: 0 auto 15px;
                }

                .profile-body {
                    padding: 40px;
                }

                .info-group {
                    margin-bottom: 25px;
                    border-bottom: 1px solid #eee;
                    padding-bottom: 15px;
                }

                .info-label {
                    font-size: 1.3rem;
                    color: #888;
                    margin-bottom: 5px;
                    text-transform: uppercase;
                    font-weight: bold;
                }

                .info-value {
                    font-size: 1.8rem;
                    color: #333;
                }
            </style>
        </head>

        <body>
            <div class="profile-wrap">
                <div class="grid wide">
                    <div class="row" style="justify-content: center;">
                        <div class="col l-6 m-8 c-12">
                            <div class="profile-card">
                                <div class="profile-header">
                                    <div class="profile-avatar"><i class="ti-user"></i></div>
                                    <h2 style="margin: 0; font-size: 2.4rem;">${sessionScope.ACC.fullName}</h2>
                                    <p style="margin: 5px 0 0; opacity: 0.8; font-size: 1.4rem;">Vai trò:
                                        ${sessionScope.ACC.role}</p>
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

                                    <div style="text-align: center; margin-top: 30px;">
                                        <button class="btn btn--primary"><i class="ti-pencil-alt"></i> Cập nhật thông
                                            tin</button>
                                        <a href="user-logout" class="btn btn--normal" style="color: #dc3545;"><i
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