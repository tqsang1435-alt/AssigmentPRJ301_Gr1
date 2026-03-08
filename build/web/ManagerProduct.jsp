<%-- 
    Document   : ManagerProduct
    Created on : Mar 4, 2026, 5:44:40 PM
    Author     : tqsan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Sản phẩm</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container-fluid mt-4">
            <h1 class="text-center mb-4">Quản lý Sản phẩm PhoneShop</h1>

            <div class="d-flex justify-content-between mb-3">
                <a href="add-product" class="btn btn-success">Thêm Sản phẩm mới</a>
                
                <form action="product-list" method="get" class="form-inline">
                    <label class="mr-2 font-weight-bold">Lọc theo:</label>
                    
                    <select name="ramFilter" class="form-control mr-2">
                        <option value="">-- Tất cả RAM --</option>
                        <option value="4GB" ${selectedRam == '4GB' ? 'selected' : ''}>4GB</option>
                        <option value="8GB" ${selectedRam == '8GB' ? 'selected' : ''}>8GB</option>
                        <option value="12GB" ${selectedRam == '12GB' ? 'selected' : ''}>12GB</option>
                    </select>
                    
                    <select name="romFilter" class="form-control mr-2">
                        <option value="">-- Tất cả ROM --</option>
                        <option value="64GB" ${selectedRom == '64GB' ? 'selected' : ''}>64GB</option>
                        <option value="128GB" ${selectedRom == '128GB' ? 'selected' : ''}>128GB</option>
                        <option value="256GB" ${selectedRom == '256GB' ? 'selected' : ''}>256GB</option>
                        <option value="512GB" ${selectedRom == '512GB' ? 'selected' : ''}>512GB</option>
                    </select>
                    
                    <button type="submit" class="btn btn-primary">Lọc</button>
                    <a href="product-list" class="btn btn-secondary ml-1">Xóa lọc</a>
                </form>
            </div>

            <table class="table table-bordered table-striped table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Tên Sản phẩm</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>RAM</th>
                        <th>ROM</th>
                        <th>Màu sắc</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listP}" var="p">
                        <tr>
                            <td>${p.productID}</td>
                            <td>${p.productName}</td>
                            <td>${p.price}</td>
                            <td>${p.stockQuantity}</td>
                            <td><span class="badge badge-info">${p.ram}</span></td>
                            <td><span class="badge badge-secondary">${p.rom}</span></td>
                            <td>${p.color}</td>
                            <td>
                                <a href="load-product?pid=${p.productID}" class="btn btn-warning btn-sm">Sửa</a>
                                <a href="delete-product?pid=${p.productID}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty listP}">
                        <tr>
                            <td colspan="8" class="text-center text-danger font-weight-bold">Không tìm thấy sản phẩm nào phù hợp với cấu hình bạn chọn!</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </body>
</html>
