<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập - PhoneShop</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    </head>

    <body>
        <div class="login-container">
            <div class="login-box">
                <a href="${pageContext.request.contextPath}/" class="login-logo">
                    <i class="ti-mobile"></i> <span>PhoneShop</span>
                </a>

                <form action="MainController" method="POST">

                    <div class="error-message">${requestScope.ERROR}</div>

                    <div class="form-group">
                        <label class="form-label">Tài khoản</label>
                        <input type="text" name="txtUsername" class="form-control" placeholder="Nhập tên đăng nhập"
                            value="${param.txtUsername}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Mật khẩu</label>
                        <input type="password" name="txtPassword" class="form-control" placeholder="Nhập mật khẩu"
                            required>
                    </div>

                    <button type="submit" name="action" value="Login" class="btn btn--primary btn-login">Đăng
                        Nhập</button>
                </form>
            </div>
        </div>
    </body>

    </html>