<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
    <c:redirect url="home" />
</c:if>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Thêm Khách hàng - PhoneShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
</head>

<body>
    <div class="admin-container">
        <%-- SIDEBAR --%>
        <jsp:include page="admin-sidebar.jsp"></jsp:include>

        <div class="admin-main">
            <div class="admin-header">
                <h3>Thêm Khách hàng mới</h3>
                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
            </div>

            <div class="admin-content">
                <div class="card" style="max-width: 700px; margin: 0 auto;">
                    <div class="card-body">
                        <c:if test="${not empty errorMsg}">
                            <div style="margin-bottom: 16px; padding: 12px 16px; background: #fff0f0; border-left: 4px solid #e74c3c; border-radius: 6px; color: #c0392b;">
                                <i class="ti-alert"></i> ${errorMsg}
                            </div>
                        </c:if>
                        <form action="add-customer" method="post">
                            <div class="form-group">
                                <label class="form-label">Họ và tên <span style="color:red">*</span></label>
                                <input type="text" name="fullName" class="form-control"
                                    placeholder="Nhập họ và tên" required>
                            </div>
                            <div class="row">
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Email <span style="color:red">*</span></label>
                                        <input type="email" name="email" class="form-control"
                                            placeholder="example@email.com" required>
                                    </div>
                                </div>
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Số điện thoại</label>
                                        <input type="text" name="phone" class="form-control"
                                            placeholder="VD: 0901234567">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Mật khẩu <span style="color:red">*</span></label>
                                        <input type="password" name="password" id="password" class="form-control"
                                            placeholder="Nhập mật khẩu" required>
                                    </div>
                                </div>
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Xác nhận mật khẩu <span style="color:red">*</span></label>
                                        <input type="password" name="confirmPassword" id="confirmPassword"
                                            class="form-control" placeholder="Nhập lại mật khẩu" required>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Địa chỉ</label>
                                <input type="text" name="address" class="form-control"
                                    placeholder="Nhập địa chỉ">
                            </div>
                            <div class="form-group" style="text-align: right; margin-top: 8px;">
                                <a href="customer-list" class="btn btn--normal">Hủy bỏ</a>
                                <button type="submit" class="btn btn--primary" onclick="return validateForm()">
                                    <i class="ti-plus"></i> Thêm khách hàng
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function validateForm() {
            const password = document.getElementById('password').value;
            const confirm = document.getElementById('confirmPassword').value;
            if (password !== confirm) {
                alert('Mật khẩu xác nhận không khớp!');
                return false;
            }
            return true;
        }
    </script>
</body>

</html>
