<%-- 
    Document   : AddProduct
    Created on : Mar 4, 2026, 5:46:30 PM
    Author     : tqsan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Sản Phẩm Mới</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <h2 class="text-center text-success mb-4">Thêm Điện Thoại Mới</h2>
            
            <form action="add-product" method="post" class="shadow p-4 bg-light rounded">
                <div class="row">
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">Tên Sản Phẩm:</label>
                        <input type="text" name="name" class="form-control" required placeholder="VD: iPhone 15 Pro Max">
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="font-weight-bold">Giá tiền (VND):</label>
                        <input type="number" name="price" class="form-control" required placeholder="VD: 34000000">
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="font-weight-bold">Số lượng kho:</label>
                        <input type="number" name="quantity" class="form-control" required value="0">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">RAM:</label>
                        <select name="ram" class="form-control">
                            <option value="4GB">4GB</option>
                            <option value="8GB" selected>8GB</option>
                            <option value="12GB">12GB</option>
                            <option value="16GB">16GB</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">ROM (Bộ nhớ):</label>
                        <select name="rom" class="form-control">
                            <option value="64GB">64GB</option>
                            <option value="128GB">128GB</option>
                            <option value="256GB" selected>256GB</option>
                            <option value="512GB">512GB</option>
                            <option value="1TB">1TB</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label class="font-weight-bold">Màu sắc:</label>
                        <input type="text" name="color" class="form-control" placeholder="VD: Titan Tự nhiên">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">ID Danh mục (Category):</label>
                        <input type="number" name="category" class="form-control" required value="1">
                    </div>
                    <div class="col-md-6 form-group">
                        <label class="font-weight-bold">ID Nhà cung cấp (Supplier):</label>
                        <input type="number" name="supplier" class="form-control" required value="1">
                    </div>
                </div>

                <div class="form-group">
                    <label class="font-weight-bold">Link Ảnh (URL):</label>
                    <input type="text" name="image" class="form-control" placeholder="https://...">
                </div>

                <div class="form-group">
                    <label class="font-weight-bold">Mô tả chi tiết:</label>
                    <textarea name="description" class="form-control" rows="3"></textarea>
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-success px-5">Lưu Sản Phẩm</button>
                    <a href="product-list" class="btn btn-secondary px-5 ml-2">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </body>
</html>
