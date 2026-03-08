<%-- 
    Document   : EditSupplier
    Created on : Feb 8, 2026, 8:36:27 PM
    Author     : tqsan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sửa Nhà Cung Cấp</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container">
            <h2 class="mt-4">Chỉnh sửa thông tin</h2>
            <form action="update-supplier" method="post">
                
                <input type="hidden" name="id" value="${detail.id}">
                
                <div class="form-group">
                    <label>Tên Nhà Cung Cấp:</label>
                    <input type="text" name="name" class="form-control" value="${detail.name}" required>
                </div>
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input type="text" name="phone" class="form-control" value="${detail.phone}">
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" class="form-control" value="${detail.email}">
                </div>
                <div class="form-group">
                    <label>Địa chỉ:</label>
                    <input type="text" name="address" class="form-control" value="${detail.address}">
                </div>
                <button type="submit" class="btn btn-warning">Cập nhật</button>
                <a href="supplier-list" class="btn btn-secondary">Hủy</a>
            </form>
        </div>
    </body>
</html>