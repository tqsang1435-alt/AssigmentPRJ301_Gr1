<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
    <c:redirect url="home" />
</c:if>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Sửa Thông Tin Khách Hàng - PhoneShop Admin</title>
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
                <h3>Chỉnh sửa thông tin khách hàng</h3>
                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
            </div>

            <div class="admin-content">
                <div class="card" style="max-width: 800px; margin: 0 auto;">
                    <div class="card-body">
                        <form action="update-customer" method="post">
                            <input type="hidden" name="id" value="${customer.userID}">

                            <div class="form-group">
                                <label class="form-label">Họ và Tên</label>
                                <input type="text" name="fullName" class="form-control" value="${customer.fullName}" required>
                            </div>
                            <div class="row">
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Email</label>
                                        <input type="email" name="email" class="form-control" value="${customer.email}" required>
                                    </div>
                                </div>
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Số điện thoại</label>
                                        <input type="text" name="phone" class="form-control" value="${customer.phoneNumber}">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Địa chỉ</label>
                                <input type="text" name="address" class="form-control" value="${customer.address}">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Mật khẩu mới (để trống nếu không đổi)</label>
                                <input type="password" name="password" class="form-control">
                            </div>
                            <div class="row">
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Điểm thưởng</label>
                                        <input type="number" name="rewardPoints" class="form-control" value="${customer.rewardPoints}" required>
                                    </div>
                                </div>
                                <div class="col l-6 m-6 c-12">
                                    <div class="form-group">
                                        <label class="form-label">Loại khách hàng</label>
                                        <input type="text" name="customerType" class="form-control" value="${customer.customerType}" disabled>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" style="text-align: right;">
                                <a href="customer-list" class="btn btn--normal">Hủy bỏ</a>
                                <button type="submit" class="btn btn--primary">Cập nhật</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>
