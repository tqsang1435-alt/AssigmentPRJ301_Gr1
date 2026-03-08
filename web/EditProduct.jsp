<%-- 
    Document   : EditProduct
    Created on : Mar 4, 2026, 5:49:36 PM
    Author     : tqsan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sửa Sản Phẩm</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <h2 class="text-center text-warning mb-4">Chỉnh Sửa Điện Thoại</h2>
            
            <form action="update-product" method="post" class="shadow p-4 bg-light rounded">
                <input type="hidden" name="id" value="${detail.productID}">

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">Tên Sản Phẩm:</label>
                        <input type="text" name="name" class="form-control" required value="${detail.productName}">
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="font-weight-bold">Giá tiền (VND):</label>
                        <input type="number" name="price" class="form-control" required value="${detail.price}">
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="font-weight-bold">Số lượng kho:</label>
                        <input type="number" name="quantity" class="form-control" required value="${detail.stockQuantity}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">RAM:</label>
                        <select name="ram" class="form-control">
                            <option value="4GB" ${detail.ram == '4GB' ? 'selected' : ''}>4GB</option>
                            <option value="8GB" ${detail.ram == '8GB' ? 'selected' : ''}>8GB</option>
                            <option value="12GB" ${detail.ram == '12GB' ? 'selected' : ''}>12GB</option>
                            <option value="16GB" ${detail.ram == '16GB' ? 'selected' : ''}>16GB</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">ROM (Bộ nhớ):</label>
                        <select name="rom" class="form-control">
                            <option value="64GB" ${detail.rom == '64GB' ? 'selected' : ''}>64GB</option>
                            <option value="128GB" ${detail.rom == '128GB' ? 'selected' : ''}>128GB</option>
                            <option value="256GB" ${detail.rom == '256GB' ? 'selected' : ''}>256GB</option>
                            <option value="512GB" ${detail.rom == '512GB' ? 'selected' : ''}>512GB</option>
                            <option value="1TB" ${detail.rom == '1TB' ? 'selected' : ''}>1TB</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">Màu sắc:</label>
                        <input type="text" name="color" class="form-control" value="${detail.color}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">ID Danh mục (Category):</label>
                        <input type="number" name="category" class="form-control" required value="${detail.categoryID}">
                    </div>
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">ID Nhà cung cấp (Supplier):</label>
                        <input type="number" name="supplier" class="form-control" required value="${detail.supplierID}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="font-weight-bold">Link Ảnh (URL):</label>
                    <input type="text" name="image" class="form-control" value="${detail.imageURL}">
                </div>

                <div class="form-group">
                    <label class="font-weight-bold">Mô tả chi tiết:</label>
                    <textarea name="description" class="form-control" rows="3">${detail.description}</textarea>
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-warning px-5">Cập nhật</button>
                    <a href="product-list" class="btn btn-secondary px-5 ml-2">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </body>
</html>