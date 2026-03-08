<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sửa Thông Tin Khách Hàng</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Chỉnh sửa thông tin khách hàng</h2>
        <form action="update-customer" method="post">
            <input type="hidden" name="id" value="${customer.userID}">
            
            <div class="form-group">
                <label>Họ và Tên:</label>
                <input type="text" name="fullName" class="form-control" value="${customer.fullName}" required>
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" class="form-control" value="${customer.email}" required>
            </div>
            <div class="form-group">
                <label>Số điện thoại:</label>
                <input type="text" name="phone" class="form-control" value="${customer.phoneNumber}">
            </div>
            <div class="form-group">
                <label>Địa chỉ:</label>
                <input type="text" name="address" class="form-control" value="${customer.address}">
            </div>
            <div class="form-group">
                <label>Mật khẩu mới (để trống nếu không đổi):</label>
                <input type="password" name="password" class="form-control">
            </div>
            <div class="form-group">
                <label>Điểm thưởng:</label>
                <input type="number" name="rewardPoints" class="form-control" value="${customer.rewardPoints}" required>
            </div>
            <div class="form-group">
                <label>Loại khách hàng:</label>
                <input type="text" name="customerType" class="form-control" value="${customer.customerType}" required>
            </div>
            
            <button type="submit" class="btn btn-warning">Cập nhật</button>
            <a href="customer-list" class="btn btn-secondary">Hủy</a>
        </form>
    </div>
</body>
</html>
