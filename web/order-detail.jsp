<%-- 
    Document   : order-detail
    Created on : Mar 4, 2026, 12:13:40 AM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container { border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .card-header { background: #212529; color: white; font-weight: bold; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="navbar.jsp"/>
    <div class="container mt-5">
        <div class="card table-container border-0">
            <div class="card-header py-3 text-center">
                <h5 class="mb-0">SẢN PHẨM TRONG ĐƠN HÀNG</h5>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">STT</th>
                            <th>Tên sản phẩm</th>
                            <th class="text-center">Số lượng</th>
                            <th>Giá mua</th>
                            <th class="text-end pe-4">Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${detailList}" var="d" varStatus="loop">
                            <tr>
                                <%-- Hiển thị STT bắt đầu từ 1 --%>
                                <td class="ps-4 text-muted">${loop.count}</td>
                                
                                <%-- Tra cứu tên sản phẩm từ Map qua ProductID --%>
                                <td class="fw-bold text-primary">
                                    ${productNames[d.productID]}
                                </td>
                                
                                <td class="text-center">${d.quantity}</td>
                                <td><fmt:formatNumber value="${d.price}" groupingUsed="true"/> ₫</td>
                                <td class="text-end pe-4 fw-bold">
                                    <fmt:formatNumber value="${d.price * d.quantity}" groupingUsed="true"/> ₫
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="mt-4 text-center">
            <a href="order-history" class="btn btn-dark rounded-pill px-5 shadow-sm">
                <i class="fa-solid fa-arrow-left me-2"></i>Quay lại lịch sử
            </a>
        </div>
    </div>
</body>
</html>