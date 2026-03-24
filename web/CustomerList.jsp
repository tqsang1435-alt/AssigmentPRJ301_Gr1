<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:if test="${sessionScope.ACC == null || sessionScope.ACC.role != 'Admin'}">
    <c:redirect url="home" />
</c:if>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Quản lý Khách hàng - PhoneShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    <style>
        .pagination-bar {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 6px;
            padding: 16px 20px;
            flex-wrap: wrap;
            border-top: 1px solid #e9ecef;
            margin-top: 0;
        }
        .pagination-bar a,
        .pagination-bar span {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 36px;
            height: 36px;
            padding: 0 10px;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            border: 1px solid #dee2e6;
            color: #495057;
            background: #fff;
            transition: all .2s;
        }
        .pagination-bar a:hover {
            background: #f1f3f5;
            border-color: #adb5bd;
        }
        .pagination-bar a.active {
            background: var(--primary-color, #e83a3a);
            color: #fff;
            border-color: var(--primary-color, #e83a3a);
        }
        .pagination-bar span.disabled {
            color: #adb5bd;
            pointer-events: none;
        }
    </style>
</head>

<body>
    <div class="admin-container">
        <%-- SIDEBAR --%>
        <jsp:include page="admin-sidebar.jsp"></jsp:include>

        <div class="admin-main">
            <div class="admin-header">
                <h3>Quản lý Khách hàng</h3>
                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                </div>
            </div>

            <div class="admin-content">
                <div class="card">
                    <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
                        <h3 class="card-title">Danh sách khách hàng</h3>
                        <a href="add-customer" class="btn btn--primary">
                            <i class="ti-plus"></i> Thêm khách hàng
                        </a>
                    </div>
                    <div class="card-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>ID</th>
                                    <th>Họ và tên</th>
                                    <th>Email</th>
                                    <th>SĐT</th>
                                    <th>Địa chỉ</th>
                                    <th>Điểm</th>
                                    <th>Hạng</th>
                                    <th>Ngày tạo</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${requestScope.listC}" var="c" varStatus="loop">
                                    <tr>
                                        <td>${pageOffset + loop.index + 1}</td>
                                        <td>#${c.userID}</td>
                                        <td>${c.fullName}</td>
                                        <td>${c.email}</td>
                                        <td>${c.phoneNumber}</td>
                                        <td>${c.address}</td>
                                        <td style="font-weight: bold; color: var(--primary-color);">
                                            ${c.rewardPoints}</td>
                                        <td>
                                            <span class="badge badge--normal">${c.customerType}</span>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${c.createDate}"
                                                pattern="dd/MM/yyyy" />
                                        </td>
                                        <td>
                                            <a href="edit-customer?id=${c.userID}" class="action-btn"
                                                title="Chỉnh sửa"><i class="ti-pencil"></i></a>
                                            <a href="admin-delete-user?userID=${c.userID}"
                                                onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng này?');"
                                                class="action-btn action-btn--delete" title="Xóa"><i
                                                    class="ti-trash"></i></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <%-- Pagination --%>
                    <div class="pagination-bar">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="customer-list?page=${currentPage - 1}">&laquo; Trước</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">&laquo; Trước</span>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach begin="1" end="${totalPages}" var="p">
                            <a href="customer-list?page=${p}"
                               class="${p == currentPage ? 'active' : ''}">${p}</a>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="customer-list?page=${currentPage + 1}">Sau &raquo;</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">Sau &raquo;</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <%-- End Pagination --%>

                </div>
            </div>
        </div>
    </div>
</body>

</html>