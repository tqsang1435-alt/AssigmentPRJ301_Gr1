<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Cập nhật hồ sơ | PHONEShop</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        </head>

        <body>
            <!-- HEADER -->
            <jsp:include page="header.jsp"></jsp:include>

            <div class="container" style="background-color: #f5f5f5; padding-top: 40px; padding-bottom: 60px;">
                <div class="grid wide">
                    <div class="row" style="justify-content: center;">
                        <div class="col l-6 m-8 c-12">
                            <div class="login-box" style="max-width: 100%; margin: 0 auto; text-align: left;">
                                <h2 class="login-logo" style="display: block; text-align: center; margin-bottom: 30px;">
                                    Cập nhật hồ sơ
                                </h2>

                                <form action="update-profile" method="post">
                                    <div class="form-group">
                                        <label for="fullName" class="form-label">Họ và tên</label>
                                        <input type="text" id="fullName" name="fullName" class="form-control"
                                            value="${sessionScope.ACC.fullName}" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="phone" class="form-label">Số điện thoại</label>
                                        <input type="tel" id="phone" name="phone" class="form-control"
                                            value="${sessionScope.ACC.phoneNumber}" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="address" class="form-label">Địa chỉ</label>
                                        <input type="text" id="address" name="address" class="form-control"
                                            value="${sessionScope.ACC.address}">
                                    </div>

                                    <div
                                        style="margin-top: 30px; display: flex; justify-content: space-between; align-items: center;">
                                        <a href="user-profile" class="auth-switch__link"
                                            style="font-weight: normal; margin-left: 0; font-size: 12px;"><i
                                                class="ti-arrow-left"></i>
                                            <span style="padding-bottom: 2px;">Quay lại</span>
                                        </a>
                                        <button type="submit" class="btn btn--primary"
                                            style="min-width: 120px; height: 40px;">Lưu thay đổi</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- FOOTER -->
            <jsp:include page="footer.jsp"></jsp:include>
        </body>

        </html>