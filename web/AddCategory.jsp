<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
    <c:redirect url="home" />
</c:if>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Loại Sản Phẩm - PhoneShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
</head>
<body>
    <div class="admin-container">
        <jsp:include page="admin-sidebar.jsp"></jsp:include>
        
        <div class="admin-main">
            <div class="admin-header">
                <h3>Quản lý Loại Sản Phẩm</h3>
                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
            </div>
            
            <div class="admin-content">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Thêm Loại Sản Phẩm Mới</h3>
                    </div>
                    <div class="card-body">
                        <form action="add-category" method="post" style="max-width: 500px;">
                            <div class="form-group" style="margin-bottom: 20px;">
                                <label style="display: block; margin-bottom: 8px; font-weight: bold;">Tên Loại Sản Phẩm:</label>
                                <input type="text" name="name" class="form-control" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                            </div>
                            <div style="display: flex; gap: 10px;">
                                <button type="submit" class="btn btn--primary">Lưu lại</button>
                                <a href="category-list" class="btn" style="background-color: #f1f1f1; color: #333;">Hủy</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
