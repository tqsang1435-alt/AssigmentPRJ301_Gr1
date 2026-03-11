<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
            <c:redirect url="home" />
        </c:if>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Thêm Sản phẩm - PhoneShop Admin</title>
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
                            <h3>Thêm Sản phẩm mới</h3>
                            <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
                        </div>

                        <div class="admin-content">
                            <div class="card" style="max-width: 800px; margin: 0 auto;">
                                <div class="card-body">
                                    <form action="add-product" method="post">
                                        <div class="form-group">
                                            <label class="form-label">Tên sản phẩm</label>
                                            <input type="text" name="name" class="form-control" required>
                                        </div>
                                        <div class="row">
                                            <div class="col l-6 m-6 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">Giá (VNĐ)</label>
                                                    <input type="number" name="price" class="form-control" required>
                                                </div>
                                            </div>
                                            <div class="col l-6 m-6 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">Số lượng kho</label>
                                                    <input type="number" name="quantity" class="form-control" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Hình ảnh (URL)</label>
                                            <input type="text" name="image" class="form-control" required>
                                        </div>
                                        <div class="row">
                                            <div class="col l-4 m-4 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">RAM</label>
                                                    <input type="text" name="ram" class="form-control"
                                                        placeholder="VD: 8GB">
                                                </div>
                                            </div>
                                            <div class="col l-4 m-4 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">ROM (Bộ nhớ trong)</label>
                                                    <input type="text" name="rom" class="form-control"
                                                        placeholder="VD: 128GB">
                                                </div>
                                            </div>
                                            <div class="col l-4 m-4 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">Màu sắc</label>
                                                    <input type="text" name="color" class="form-control"
                                                        placeholder="VD: Xanh">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col l-6 m-6 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">Danh mục</label>
                                                    <select name="category" class="form-control">
                                                        <option value="1">Điện thoại</option>
                                                        <option value="2">Phụ kiện</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col l-6 m-6 c-12">
                                                <div class="form-group">
                                                    <label class="form-label">Nhà cung cấp</label>
                                                    <select name="supplier" class="form-control">
                                                        <option value="1">Samsung</option>
                                                        <option value="2">Apple</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Mô tả</label>
                                            <textarea name="description" class="form-control"
                                                style="height: 100px;"></textarea>
                                        </div>
                                        <div class="form-group" style="text-align: right;">
                                            <a href="product-list" class="btn btn--normal">Hủy bỏ</a>
                                            <button type="submit" class="btn btn--primary">Lưu sản phẩm</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
        </body>

        </html>