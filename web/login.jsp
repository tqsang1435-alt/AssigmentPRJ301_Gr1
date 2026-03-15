<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Đăng nhập - PhoneShop</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=999">

            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

            <style>
                #toast-success {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: #28a745;
                    color: white;
                    padding: 14px 24px;
                    border-radius: 4px;
                    font-size: 1.4rem;
                    font-weight: 500;
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                    z-index: 9999;
                    animation: fadeInOut 4s forwards;
                    display: flex;
                    align-items: center;
                }

                #toast-success i {
                    margin-right: 8px;
                    font-size: 1.8rem;
                }

                @keyframes fadeInOut {
                    0% {
                        opacity: 0;
                        transform: translateY(-20px);
                    }

                    10% {
                        opacity: 1;
                        transform: translateY(0);
                    }

                    80% {
                        opacity: 1;
                        transform: translateY(0);
                    }

                    100% {
                        opacity: 0;
                        transform: translateY(-20px);
                    }
                }
            </style>
        </head>

        <body>
            <jsp:include page="header.jsp" />

            <c:if test="${param.register == 'success'}">
                <div id="toast-success">
                    <i class="ti-check-box"></i> Đăng ký tài khoản thành công!
                </div>
            </c:if>

            <div class="login-container">
                <div class="login-box">
                    <a href="${pageContext.request.contextPath}/" class="login-logo">
                        <i class="ti-mobile"></i> <label>PhoneShop</label>
                    </a>

                    <form action="user-login" method="POST">

                        <div class="error-message">${requestScope.mess}</div>

                        <div class="form-group">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" placeholder="Nhập email"
                                value="${param.email}" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Mật khẩu</label>
                            <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu"
                                required>
                        </div>

                        <button type="submit" class="btn btn--primary btn-login">Đăng Nhập</button>

                        <div class="auth-switch">
                            <span>Chưa có tài khoản?</span>
                            <a href="${pageContext.request.contextPath}/register.jsp" class="auth-switch__link">Đăng ký
                                ngay</a>
                        </div>
                    </form>
                </div>
            </div>

            <jsp:include page="footer.jsp" />
        </body>

        </html>