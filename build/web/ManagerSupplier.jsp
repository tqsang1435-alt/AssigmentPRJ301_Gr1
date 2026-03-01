<%-- 
    Document   : ManagerSupplier
    Created on : Feb 8, 2026, 8:22:33 PM
    Author     : tqsan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Nhà cung cấp</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container">
            <h1 class="text-center my-4">Danh sách Nhà cung cấp</h1>

            <a href="add-supplier" class="btn btn-success mb-3">Thêm nhà cung cấp mới</a>

            <table class="table table-bordered table-striped">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Tên Nhà Cung Cấp</th>
                        <th>Số điện thoại</th>
                        <th>Email</th>
                        <th>Địa chỉ</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listS}" var="o">
                        <tr>
                            <td>${o.id}</td>
                            <td>${o.name}</td>
                            <td>${o.phone}</td>
                            <td>${o.email}</td>
                            <td>${o.address}</td>
                            <td>
                                <a href="load-supplier?pid=${o.id}" class="btn btn-warning btn-sm">Sửa</a>
                                <a href="delete-supplier?pid=${o.id}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa không?')">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>