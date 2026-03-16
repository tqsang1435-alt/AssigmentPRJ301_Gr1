<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
    <c:redirect url="home" />
</c:if>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Quản Lý Loại Sản Phẩm - PhoneShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
</head>

<body>
    <div class="admin-container">
        <!-- Include Sidebar -->
        <jsp:include page="admin-sidebar.jsp"></jsp:include>

        <!-- Main Content -->
        <div class="admin-main">
            <div class="admin-header">
                <h3>Quản lý Loại Sản Phẩm</h3>
                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong></div>
            </div>

            <div class="admin-content">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Danh sách loại sản phẩm</h3>
                        <a href="add-category" class="btn btn--primary">
                            <i class="ti-plus"></i> Thêm mới
                        </a>
                    </div>
                    <div class="card-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên Loại Sản Phẩm</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listC}" var="c">
                                    <tr>
                                        <td>#${c.categoryID}</td>
                                        <td>${c.categoryName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>