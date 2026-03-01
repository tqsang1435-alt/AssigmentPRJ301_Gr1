<%-- 
    Document   : AddSupplier
    Created on : Feb 8, 2026, 8:29:01 PM
    Author     : tqsan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Nhà Cung Cấp</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container">
            <h2 class="mt-4">Thêm Nhà Cung Cấp Mới</h2>
            <form action="add-supplier" method="post">
                <div class="form-group">
                    <label>Tên Nhà Cung Cấp:</label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input type="text" name="phone" class="form-control">
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" class="form-control">
                </div>
                <div class="form-group">
                    <label>Địa chỉ:</label>
                    <input type="text" name="address" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary">Lưu lại</button>
                <a href="supplier-list" class="btn btn-secondary">Hủy</a>
            </form>
        </div>
    </body>
</html>
