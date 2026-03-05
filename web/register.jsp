<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng ký - PhoneShop</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    </head>

    <body>
        <div class="login-container">
            <div class="login-box register-box">

                <a href="${pageContext.request.contextPath}/" class="login-logo">
                    <i class="ti-mobile"></i> <label>PhoneShop</label>
                </a>

                <form action="user-register" method="POST">

                    <div class="error-message">${requestScope.mess}</div>

                    <div class="row">
                        <div class="col c-6">
                            <div class="form-group">
                                <label class="form-label">Họ và tên</label>
                                <input type="text" name="user" class="form-control" placeholder="Nhập họ và tên"
                                    required>
                            </div>
                        </div>
                        <div class="col c-6">
                            <div class="form-group">
                                <label class="form-label">Số điện thoại</label>
                                <input type="text" name="phone" class="form-control" placeholder="Nhập SĐT" required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" placeholder="Nhập email" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Địa chỉ</label>
                        <input type="text" name="address" class="form-control" placeholder="Nhập địa chỉ" required>
                    </div>

                    <div class="row">
                        <div class="col c-6">
                            <div class="form-group">
                                <label class="form-label">Mật khẩu</label>
                                <input type="password" name="pass" class="form-control" placeholder="Tạo mật khẩu"
                                    required>
                            </div>
                        </div>
                        <div class="col c-6">
                            <div class="form-group">
                                <label class="form-label">Xác nhận mật khẩu</label>
                                <input type="password" name="re_pass" class="form-control"
                                    placeholder="Nhập lại mật khẩu" required>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn--primary btn-login">Đăng Ký</button>

                    <div class="auth-switch">
                        <span>Đã có tài khoản?</span>
                        <a href="${pageContext.request.contextPath}/user-login" class="auth-switch__link">Đăng nhập tại
                            đây</a>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>