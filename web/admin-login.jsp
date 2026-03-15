<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login - PhoneShop</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

    <style>
        body {
            background-color: #f4f6f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
    </style>
</head>

<body>
    <div class="login-container" style="min-height: auto; padding: 40px 0;">
        <div class="login-box" style="box-shadow: 0 5px 20px rgba(0,0,0,0.1);">
            <div class="login-logo" style="margin-bottom: 20px;">
                <i class="ti-lock" style="color: var(--primary-color);"></i> <label style="text-transform: uppercase;">Admin Portal</label>
            </div>

            <form action="admin-login" method="POST">

                <div class="error-message" style="color: red; margin-bottom: 10px; text-align: center;">${requestScope.mess}</div>

                <div class="form-group">
                    <label class="form-label">Email Quản trị</label>
                    <input type="email" name="email" class="form-control" placeholder="Nhập email" value="${param.email}" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
                </div>

                <button type="submit" class="btn btn--primary btn-login" style="margin-top: 10px;">Đăng Nhập Quản Trị</button>

                <div class="auth-switch" style="margin-top: 15px; text-align: center;">
                    <a href="${pageContext.request.contextPath}/" class="auth-switch__link">Quay lại trang cửa hàng</a>
                </div>
            </form>
        </div>
    </div>
</body>

</html>
