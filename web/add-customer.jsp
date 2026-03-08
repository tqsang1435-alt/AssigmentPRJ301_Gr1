<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thêm Khách Hàng</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Thêm Khách Hàng Mới</h2>
        <form action="add-customer" method="post">
            <div class="form-group">
                <label>Họ và Tên:</label>
                <input type="text" name="fullName" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" class="form-control" required>
            </div>
             <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Số điện thoại:</label>
                <input type="text" name="phone" class="form-control">
            </div>
            <div class="form-group">
                <label>Địa chỉ:</label>
                <input type="text" name="address" class="form-control">
            </div>
            <button type="submit" class="btn btn-primary">Lưu lại</button>
            <a href="customer-list" class="btn btn-secondary">Hủy</a>
        </form>
    </div>
</body>
</html>
